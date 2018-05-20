package io.github.jordieh.boosters.framework.commands;

import io.github.jordieh.boosters.common.GameUtil;
import io.github.jordieh.boosters.framework.booster.Booster;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.concurrent.TimeUnit;

public class BoosterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("boosters.create")) {
            sender.sendMessage("You have insufficient permission for this action");
            return true;
        }

        if (args.length < 3) { // Invalid command usage
            return false;
        }

        @SuppressWarnings("deprecation") // Specific use case is valid
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

        int percentage;
        try {
            percentage = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid percentage");
            return true;
        }

        long duration;
        try {
            duration = Long.parseLong(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid duration");
            return true;
        }
        duration = TimeUnit.SECONDS.toMillis(duration);

        int amount = 1; // TODO IMPLEMENTATION
        if (args.length > 3) {
            try {
                amount = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                sender.sendMessage("Invalid amount");
                return true;
            }
        }
        amount = amount < 1 ? 1 : amount;

        if (amount == 1) {
            Booster booster = new Booster(player.getUniqueId(), percentage, duration);
        } else {
            for (int i = 0; i < amount; i++) {
                Booster booster = new Booster(player.getUniqueId(), percentage, duration);
            }
        }

        sender.sendMessage("You have succesfully given " + player.getName() + " " + amount +
                (amount > 1 ? " boosters" : " booster") + " (" + percentage + "%) " +
                "(" + GameUtil.format(duration) + ")");

        return true;
    }

}
