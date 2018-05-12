package io.github.jordieh.boosters.modules;

import io.github.jordieh.boosters.BoosterPlugin;
import io.github.jordieh.boosters.framework.database.Database;
import org.bukkit.configuration.file.FileConfiguration;

public class DatabaseModule {

    private static DatabaseModule instance;

    private final Database database;

    private DatabaseModule() {
        FileConfiguration config = BoosterPlugin.getInstance().getConfig();
        this.database = new Database(
                config.getString("database.username"), config.getString("database.password"),
                config.getString("database.hostname"), config.getString("database.database"),
                config.getInt("database.max-pool-size"));
    }

    public static DatabaseModule getInstance() {
        return instance == null ? instance = new DatabaseModule() : instance;
    }

    public Database getDatabase() {
        return database;
    }

}