package io.github.jordieh.boosters;

import io.github.jordieh.boosters.framework.booster.Booster;
import io.github.jordieh.boosters.framework.booster.BoosterHolder;
import io.github.jordieh.boosters.framework.booster.BoosterHolderListener;
import io.github.jordieh.boosters.framework.commands.BoosterCommand;
import io.github.jordieh.boosters.framework.commands.BoosterListCommand;
import io.github.jordieh.boosters.modules.BoosterModule;
import io.github.jordieh.boosters.modules.DatabaseModule;
import io.github.jordieh.boosters.modules.IncomeRunnable;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

@Slf4j
public final class BoosterPlugin extends JavaPlugin implements Listener {

    @Getter private static BoosterPlugin instance;

    @Getter private Economy economy;

    @Override
    public void onEnable() {
        instance = this;
        if (!getServer().getPluginManager().isPluginEnabled("Vault") || !economy()) {
            log.warn("Vault has not been initialized correctly or you are missing a economy plugin");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getConfig().options().copyDefaults(true);
        saveConfig();

        BoosterModule.getInstance().getLoader().loadBoosters(); // Initialize all boosters

        Bukkit.getPluginManager().registerEvents(new BoosterHolderListener(), this);
        Bukkit.getPluginManager().registerEvents(this, this);

        getCommand("createbooster").setExecutor(new BoosterCommand());
        getCommand("boosters").setExecutor(new BoosterListCommand());


//        Income scheduler

        double income = getConfig().getDouble("standard-income");
        long delay = getConfig().getLong("income-cycle-minutes");
        delay = TimeUnit.MINUTES.toSeconds(delay) * 20L; // 1 second = 20 Minecraft ticks

        getServer().getScheduler().runTaskTimer(this, new IncomeRunnable(income), delay, delay);
    }

    @Override
    public void onDisable() {
        DatabaseModule.getInstance().getDatabase().closeSource();
    }

    private boolean economy() {
        RegisteredServiceProvider<Economy> provider = getServer().getServicesManager().getRegistration(Economy.class);
        return (provider != null) && (economy = provider.getProvider()) != null;
    }

    /*
    Iám way to lazy to put these in separate classes atm :)
     */

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        BoosterModule.getInstance().getBossBar().addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        BoosterModule.getInstance().getBossBar().removePlayer(event.getPlayer());
    }

}
