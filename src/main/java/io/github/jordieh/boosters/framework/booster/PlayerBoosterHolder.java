package io.github.jordieh.boosters.framework.booster;

import io.github.jordieh.boosters.common.BoosterSet;
import io.github.jordieh.boosters.common.GameUtil;
import io.github.jordieh.boosters.modules.BoosterModule;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class PlayerBoosterHolder implements InventoryHolder {

    private static final int BOOSTERS_PER_PAGE = 45;

    private final List<Inventory> inventories;
    @Getter private final BoosterHolder parent;

    private int page;

    public PlayerBoosterHolder(BoosterHolder parent) {
        this.parent = parent;
        this.inventories = new ArrayList<>();

        updateBoosters();
    }

    @Override
    public Inventory getInventory() {
        return inventories.get(page);
    }

    public boolean isPresent() {
        return !inventories.isEmpty();
    }

    public void nextPage() {
        page = Math.min(page + 1, inventories.size());
    }

    public void previousPage() {
        page = Math.max(page - 1, 0);
    }

    public void updateBoosters() {
        inventories.clear();
        List<Booster> boosters = BoosterModule.getInstance().getBoosters(parent.getHumanEntity().getUniqueId()).sorted();
        Inventory inventory = null;

        int i = 0;
        for (Iterator<Booster> iterator = boosters.iterator(); iterator.hasNext(); i++) { // todo Forloop? lol
            if (i % BOOSTERS_PER_PAGE == 0) {
                inventory = Bukkit.createInventory(this, 54, "Boosters of " + parent.getHumanEntity().getName());
                inventories.add(inventory);
                i = 0;
            }

            Booster booster = iterator.next();

            ItemStack stack = new ItemStack(Material.EMERALD);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN.toString() + booster.getPercentage() + "% Booster");

            List<String> list = new ArrayList<>(3);
            list.add(ChatColor.GRAY + "Duration " + GameUtil.format(booster.getDuration()));
            list.add(ChatColor.GRAY + "Click to activate!");
            list.add(ChatColor.GRAY + "UUID " + booster.getUuid());
            meta.setLore(list);

            stack.setItemMeta(meta);
            inventory.setItem(i, stack);
        }

        i = 0;
        for (ItemStack stack = BoosterHolderListener.getPane(); i < inventories.size(); i++) {
            Inventory current = inventories.get(i);
            for (int x = 45; x < 54; x++) {
                current.setItem(x, stack);
            }

            current.setItem(49, BoosterHolderListener.getBoosterMenu());

            if (i != 0) {
                current.setItem(46, BoosterHolderListener.getPreviousArrow());
            }

            if (i < (inventories.size() - 1)) {
                current.setItem(52, BoosterHolderListener.getNextArrow());
            }
        }

    }

}
