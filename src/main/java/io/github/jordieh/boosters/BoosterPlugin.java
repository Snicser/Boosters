package io.github.jordieh.boosters;

import io.github.jordieh.boosters.framework.booster.Booster;
import io.github.jordieh.boosters.framework.booster.BoosterHolder;
import io.github.jordieh.boosters.modules.BoosterModule;
import lombok.extern.slf4j.Slf4j;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@Slf4j
public final class BoosterPlugin extends JavaPlugin {

    private static BoosterPlugin instance;

    private Economy economy;

    public static BoosterPlugin getInstance() {
        return instance;
    }

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
        BoosterModule.getInstance().activate(new Booster(((Player) sender).getUniqueId(), 50, 60000 ));
        ((Player) sender).openInventory(new BoosterHolder(((Player) sender).getPlayer()).getInventory());
        BoosterModule.getInstance().getBossBar().addPlayer(((Player) sender).getPlayer());
        return super.onCommand(sender, command, label, args);
    }
}
