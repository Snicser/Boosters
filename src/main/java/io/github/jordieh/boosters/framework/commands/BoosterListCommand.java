package io.github.jordieh.boosters.framework.commands;

import io.github.jordieh.boosters.framework.booster.BoosterHolder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BoosterListCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command");
            return true;
        }

        if (!sender.hasPermission("...")) {};

        Player player = (Player) sender;

        BoosterHolder holder = new BoosterHolder(player);
        player.openInventory(holder.getInventory());

        return true;
    }

}
