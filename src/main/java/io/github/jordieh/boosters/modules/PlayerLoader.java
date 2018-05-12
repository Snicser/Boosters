package io.github.jordieh.boosters.modules;

import io.github.jordieh.boosters.framework.booster.Booster;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
public final class PlayerLoader {

    @Deprecated
    private Booster loadBoosterSync(UUID uuid) {
        String s = uuid.toString();
        try (Connection connection = DatabaseModule.getInstance().getDatabase().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `boosters` WHERE `uuid`=?")) {
            preparedStatement.setString(1, s);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? Booster.of(null)
            }
            return null; // TODO
        } catch (SQLException e) {
            log.warn("Error occured while loading player {}", s, e);
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    public CompletableFuture<Booster> loadBooster(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> loadBoosterSync(uuid));
    }
}
