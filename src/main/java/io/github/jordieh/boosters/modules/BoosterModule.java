package io.github.jordieh.boosters.modules;

import io.github.jordieh.boosters.BoosterPlugin;
import io.github.jordieh.boosters.common.BoosterSet;
import io.github.jordieh.boosters.framework.booster.Booster;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class BoosterModule {

    private static final int MAX_PERCENTAGE = BoosterPlugin.getInstance().getConfig().getInt("maximal-percentage");
    private static BoosterModule instance;

    @Getter private final BossBar bossBar;
    private final BoosterSet active;
    @Getter private final BoosterLoader loader;

    private BoosterModule() {
        this.active = new BoosterSet();
        this.bossBar = Bukkit.createBossBar("...", BarColor.BLUE, BarStyle.SOLID);
        this.loader = new BoosterLoader();

        Bukkit.getScheduler().runTaskAsynchronously(BoosterPlugin.getInstance(), () -> { // TODO - ???
            try (Connection connection = DatabaseModule.getInstance().getDatabase().getConnection();
                 Statement statement = connection.createStatement()) {

                statement.execute("CREATE TABLE IF NOT EXISTS boosters (\n" +
                        "  id int(11) NOT NULL AUTO_INCREMENT,\n" +
                        "  uuid varchar(36) NOT NULL,\n" +
                        "  owner varchar(36) NOT NULL,\n" +
                        "  duration bigint(20) NOT NULL,\n" +
                        "  percentage int(11) NOT NULL,\n" +
                        "  consumed tinyint(1) NOT NULL,\n" +
                        "  PRIMARY KEY (id)\n" +
                        ") ENGINE = INNODB;"
                );

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        Bukkit.getScheduler().runTaskTimer(BoosterPlugin.getInstance(), () -> {
            active.clean();

            int percentage = active.totalPercentage();
            if (percentage <= 0) {
                bossBar.setVisible(false);
            } else {
                bossBar.setVisible(true);
                bossBar.setTitle("Booster % " + percentage);
            }

        }, 0L, 20L);

    }

    public static BoosterModule getInstance() {
        return instance == null ? instance = new BoosterModule() : instance;
    }

    public BoosterSet getBoosters(UUID uuid) {
        BoosterSet boosters = new BoosterSet();
        for (Booster booster : Booster.getCache()) {
            if (!booster.isActivated() && booster.getOwner().equals(uuid)) {
                boosters.add(booster);
            }
        }
        return boosters;
    }

    public Booster getBooster(UUID uuid) {
        for (Booster booster : Booster.getCache()) {
            if (booster.getUuid().equals(uuid)) {
                return booster;
            }
        }
        return null;
    }

    public BoosterSet getBoosters(Player player) {
        return getBoosters(player.getUniqueId());
    }

    public BoosterSet getActiveBoosters() {
        return active; // TODO Wrapper?
    }

    public boolean activate(Booster booster) {
        if ((active.totalPercentage() + booster.getPercentage()) > MAX_PERCENTAGE) {
            return false;
        }

        if (active.add(booster)) {
            booster.activate();
            return true;
        }

        return false;
    }

}
