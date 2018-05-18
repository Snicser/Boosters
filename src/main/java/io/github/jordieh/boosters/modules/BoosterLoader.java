package io.github.jordieh.boosters.modules;

import io.github.jordieh.boosters.common.BoosterSet;
import io.github.jordieh.boosters.framework.booster.Booster;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
public final class BoosterLoader {

    /**
     * Retrieves a booster from the database
     *
     * @param uuid The UUID of the booster
     * @return Booster from the database with the specified UUID
     * @deprecated Accessing a database synchronised can freeze the server
     */
    @Deprecated
    private Booster loadBoosterSync(UUID uuid) {
        String s = uuid.toString();
        try (Connection connection = DatabaseModule.getInstance().getDatabase().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT owner, percentage, duration FROM boosters WHERE uuid=?")) {

            preparedStatement.setString(1, s);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Booster(
                            UUID.fromString(resultSet.getString("owner")),
                            uuid,
                            resultSet.getInt("percentage"),
                            resultSet.getLong("duration")
                    );
                }
            }

        } catch (SQLException e) {
            log.warn("Error occurred while loading booster {}", s, e);
        }

        return null;
    }

    @SuppressWarnings("deprecation")
    public CompletableFuture<Booster> loadBooster(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> loadBoosterSync(uuid));
    }

    @Deprecated
    private BoosterSet loadBoostersSync() {
        BoosterSet boosters = new BoosterSet();

        try (Connection connection = DatabaseModule.getInstance().getDatabase().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT uuid, owner, duration, percentage FROM boosters")) {

            while (resultSet.next()) {
                Booster booster = new Booster(
                        UUID.fromString(resultSet.getString("owner")),
                        UUID.fromString(resultSet.getString("uuid")),
                        resultSet.getInt("percentage"),
                        resultSet.getLong("duration")
                );
                boosters.add(booster);
            }

        } catch (SQLException e) {
            log.warn("Error occurred while loading complete booster list", e);
        }

        return boosters;
    }

    @SuppressWarnings("deprecation")
    public CompletableFuture<BoosterSet> loadBoosters() {
        return CompletableFuture.supplyAsync(this::loadBoostersSync);
    }

}
