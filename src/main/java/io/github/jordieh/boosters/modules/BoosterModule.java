package io.github.jordieh.boosters.modules;

import io.github.jordieh.boosters.BoosterPlugin;
import io.github.jordieh.boosters.common.BoosterSet;
import io.github.jordieh.boosters.framework.booster.Booster;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BoosterModule {

    private static final int MAX_PERCENTAGE = 100;
    private static BoosterModule instance;

    private BossBar bossBar;
    private Map<UUID, BoosterSet> map;
    private BoosterSet active;

    private BoosterModule() {
        active = new BoosterSet();
        bossBar = Bukkit.createBossBar("...", BarColor.BLUE, BarStyle.SOLID);
        map = new HashMap<>();

        try (Connection connection = DatabaseModule.getInstance().getDatabase().getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS boosters (\n" +
                    "  id int(11) NOT NULL AUTO_INCREMENT,\n" +
                    "  uuid varchar(36) NOT NULL,\n" +
                    "  owner varchar(36) NOT NULL,\n" +
                    "  duration bigint(20) NOT NULL,\n" +
                    "  percentage int(11) NOT NULL,\n" +
                    "  remaining bigint(20) NOT NULL,\n" +
                    "  PRIMARY KEY (id)\n" +
                    ") ENGINE = INNODB;");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(BoosterPlugin.getInstance(), () -> {
            active.clean();
            bossBar.setTitle("Booster % " + active.totalPercentage());
        }, 0L, 20L);
    }

    public static BoosterModule getInstance() {
        return instance == null ? instance = new BoosterModule() : instance;
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public BoosterSet getBoosters(UUID uuid) {
        if (!map.containsKey(uuid)) {
            map.put(uuid, new BoosterSet());
        }

        return map.get(uuid);
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

            UUID owner = booster.getOwner();
            map.put(owner, getBoosters(owner));
            return true;
        }

        return false;
    }

}
