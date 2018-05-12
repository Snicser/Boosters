package io.github.jordieh.boosters.framework.booster;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class BoosterHolder implements InventoryHolder {

    private final Inventory inventory;

    public BoosterHolder() {
        this.inventory = Bukkit.createInventory(this, 54, "Boosters");
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
