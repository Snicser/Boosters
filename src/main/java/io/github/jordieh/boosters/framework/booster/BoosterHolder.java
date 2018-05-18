package io.github.jordieh.boosters.framework.booster;

import io.github.jordieh.boosters.common.BoosterSet;
import io.github.jordieh.boosters.common.GameUtil;
import io.github.jordieh.boosters.modules.BoosterModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BoosterHolder implements InventoryHolder {

    private static final int MAX_PAGE_BOOSTERS = 45;

    private final List<Inventory> inventories;
    private final List<Inventory> playerBoosters;

    private int page;

    public BoosterHolder(Player player) {
        inventories = fillInventories(BoosterModule.getInstance().getActiveBoosters());
        playerBoosters = fillInventories(BoosterModule.getInstance().getBoosters(player));
    }

    @Override
    public Inventory getInventory() {
        return inventories.get(page);
    }

    private List<Inventory> fillInventories(BoosterSet set) {
        List<Inventory> inventories = new ArrayList<>();
        List<Booster> boosters = set.sorted();

        Inventory inventory = null;

        int i = 0;
        for (Iterator<Booster> iterator = boosters.iterator(); iterator.hasNext(); i++) {
            if (i % MAX_PAGE_BOOSTERS == 0) {
                inventory = Bukkit.createInventory(this, 54, "Boosters");
                inventories.add(inventory);
                i = 0;
            }

            Booster booster = iterator.next();

            String owner = Bukkit.getOfflinePlayer(booster.getOwner()).getName(); // FIXME This can be done better -_-

            ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta meta = (SkullMeta) stack.getItemMeta();

            meta.setDisplayName(ChatColor.DARK_AQUA + "Active booster");
            meta.setOwner(owner);

            ArrayList<String> list = new ArrayList<>();
            list.add(ChatColor.DARK_AQUA + "Percentage: " + ChatColor.AQUA + booster.getPercentage() + "%");
            list.add(ChatColor.DARK_AQUA + "Remaining: " + ChatColor.AQUA + GameUtil.format(booster.getRemainingTime()));
            list.add(ChatColor.DARK_AQUA + "Owner: " + ChatColor.AQUA + owner);
            meta.setLore(list);

            stack.setItemMeta(meta);
            inventory.setItem(i, stack);
        }


        for (int x = 0; x < inventories.size(); x++) {
            Inventory current = inventories.get(x);

            for (int z = 45; z < 54; z++) {
                current.setItem(z, BoosterHolderListener.getPane());
            }

            if (x != 0) {
                current.setItem(45, BoosterHolderListener.getPreviousArrow());
            }

            if (x < (inventories.size() - 1)) {
                current.setItem(53, BoosterHolderListener.getNextArrow());
            }
        }

        return inventories;
    }

    public void nextPage() {
        page = Math.min(page + 1, inventories.size());
    }

    public void previousPage() {
        page = Math.min(page - 1, 0);
    }

}
