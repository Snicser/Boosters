package io.github.jordieh.boosters.framework.booster;

import io.github.jordieh.boosters.framework.booster.BoosterHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class BoosterHolderListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (!(inventory.getHolder() instanceof BoosterHolder)) {
            return;
        }

        // TODO code
    }

}
