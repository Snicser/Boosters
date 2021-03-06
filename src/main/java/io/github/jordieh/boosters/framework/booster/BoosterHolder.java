package io.github.jordieh.boosters.framework.booster;

import io.github.jordieh.boosters.common.BoosterSet;
import io.github.jordieh.boosters.common.GameUtil;
import io.github.jordieh.boosters.modules.BoosterModule;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class BoosterHolder implements InventoryHolder {

    private static final int BOOSTERS_PER_PAGE = 45;
    private static final ItemStack NO_BOOSTERS = GameUtil.item(Material.RED_ROSE, "No active boosters");

    private PlayerBoosterHolder playerBoosters;
    private final List<Inventory> inventories;
    @Getter private final HumanEntity humanEntity;

    private int page;

    public BoosterHolder(HumanEntity humanEntity) {
        this.inventories = new ArrayList<>();
        this.humanEntity = humanEntity;

        List<Booster> boosters = BoosterModule.getInstance().getActiveBoosters().sorted();
        Inventory inventory = null;

        int i = 0;
        for (Iterator<Booster> iterator = boosters.iterator(); iterator.hasNext(); i++) { // todo Forloop? lol
            if (i % BOOSTERS_PER_PAGE == 0) {
                inventory = Bukkit.createInventory(this, 54, "Activated boosters");
                inventories.add(inventory);
                i = 0;
            }

            Booster booster = iterator.next();

            ItemStack stack = new ItemStack(Material.EMERALD);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN.toString() + booster.getPercentage() + "% Booster");

            List<String> list = new ArrayList<>(1);
            list.add(ChatColor.GRAY + "Remaining " + GameUtil.format(booster.getRemainingTime()));
            list.add(ChatColor.GRAY + "Owner " + Bukkit.getOfflinePlayer(booster.getOwner()).getName());
            meta.setLore(list);

            stack.setItemMeta(meta);
            inventory.setItem(i, stack);
        }

        if (inventories.isEmpty()) {
            inventory = Bukkit.createInventory(this, 54, "No active boosters");
            inventory.setItem(22, NO_BOOSTERS.clone());
            inventories.add(inventory);
        }

        i = 0;
        for (ItemStack stack = BoosterHolderListener.getPane(); i < inventories.size(); i++) {
            Inventory current = inventories.get(i);
            for (int x = 45; x < 54; x++) {
                current.setItem(x, stack);
            }

            if (getPlayerBoosters().isPresent()) {
                current.setItem(49, BoosterHolderListener.getPlayerBoosters());
            }

            if (i != 0) {
                current.setItem(46, BoosterHolderListener.getPreviousArrow());
            }

            if (i < (inventories.size() - 1)) {
                current.setItem(52, BoosterHolderListener.getNextArrow());
            }
        }

    }

    public PlayerBoosterHolder getPlayerBoosters() {
        return playerBoosters == null
                ? playerBoosters = new PlayerBoosterHolder(this)
                : playerBoosters;
    }

    @Override
    public Inventory getInventory() {
        return inventories.get(page);
    }

    public void nextPage() {
        page = Math.min(page + 1, inventories.size());
    }

    public void previousPage() {
        page = Math.max(page - 1, 0);
    }

}
