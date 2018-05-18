package io.github.jordieh.boosters;

import io.github.jordieh.boosters.framework.booster.Booster;
import io.github.jordieh.boosters.framework.booster.BoosterHolder;
import io.github.jordieh.boosters.modules.BoosterModule;
import io.github.jordieh.boosters.modules.IncomeRunnable;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

@Slf4j
public final class BoosterPlugin extends JavaPlugin {

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

        BoosterModule.getInstance();

        double income = getConfig().getDouble("standard-income");
        long delay = getConfig().getLong("income-cycle-minutes");
        delay = TimeUnit.MINUTES.toSeconds(delay) * 20L; // 1 second = 20 Minecraft ticks

        getServer().getScheduler().runTaskTimer(this, new IncomeRunnable(income), delay, delay);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private boolean economy() {
        RegisteredServiceProvider<Economy> provider = getServer().getServicesManager().getRegistration(Economy.class);
        return (provider != null) && (economy = provider.getProvider()) != null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        BoosterModule.getInstance().activate(new Booster(((Player) sender).getUniqueId(), 50, 60000));
        ((Player) sender).openInventory(new BoosterHolder(((Player) sender).getPlayer()).getInventory());
        BoosterModule.getInstance().getBossBar().addPlayer(((Player) sender).getPlayer());
        return super.onCommand(sender, command, label, args);
    }

}
