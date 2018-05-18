package io.github.jordieh.boosters.modules;

import io.github.jordieh.boosters.BoosterPlugin;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class IncomeRunnable implements Runnable {

    private final double incomeAmount;

    public IncomeRunnable(double incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    @Override
    public void run() {
        double amount = amount();
        for (Player player : Bukkit.getOnlinePlayers()) {
            EconomyResponse response = BoosterPlugin.getInstance().getEconomy().depositPlayer(player, amount);
            if (response.transactionSuccess()) {
                player.sendMessage(ChatColor.DARK_AQUA + "You have been given " + amount + " coins");
            }
        }
    }

    private double amount() {
        int i = BoosterModule.getInstance().getActiveBoosters().totalPercentage();
        i = (i < 100) ? 100 : i; // If the percentage is lower than 100% the balance of the player will reduce
        return (i / 100) * incomeAmount;
    }

}
