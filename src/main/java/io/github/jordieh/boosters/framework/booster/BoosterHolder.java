package io.github.jordieh.boosters.framework.booster;

import io.github.jordieh.boosters.common.BoosterSet;
import io.github.jordieh.boosters.common.GameUtil;
import io.github.jordieh.boosters.modules.BoosterModule;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BoosterHolder implements InventoryHolder {

    private static final int BOOSTERS_PER_PAGE = 45;

    private static final ItemStack PANE = GameUtil.item(Material.STAINED_GLASS_PANE);
    private static final ItemStack HEAD_ACTIVATE = GameUtil.skull("MHF_Chest");
    private static final ItemStack NEXT_ARROW = GameUtil.skull("MHF_ArrowRight");
    private static final ItemStack PREVIOUS_ARROW = GameUtil.skull("MHF_ArrowLeft");

    private final List<Inventory> inventory;
    private final List<Inventory> playerBoosters;

    private int page;


    public BoosterHolder(Player player) {
        inventory = fillInventories(BoosterModule.getInstance().getActiveBoosters());
        playerBoosters = fillInventories(BoosterModule.getInstance().getBoosters(player));
    }

    @Override
    public Inventory getInventory() {
        return inventory.isEmpty() ? null : inventory.get(page);
    }

    private List<Inventory> fillInventories(BoosterSet set) {
        List<Booster> boosters = set.sorted();
        List<Inventory> inventories = new ArrayList<>();

        Inventory inventory = null;

        int i = 0;
        for (Iterator<Booster> iterator = boosters.iterator(); iterator.hasNext(); i++) {
            if (i % BOOSTERS_PER_PAGE == 0) {
                inventory = Bukkit.createInventory(this, 54, "Boosters");
                inventories.add(inventory);
                i = 0;
            }

            Booster booster = iterator.next();

            ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta meta = (SkullMeta) stack.getItemMeta();

            meta.setDisplayName(ChatColor.DARK_AQUA + "Active booster");
            meta.setOwner(Bukkit.getOfflinePlayer(booster.getOwner()).getName()); // FIXME --

            ArrayList<String> list = new ArrayList<>();
            list.add(ChatColor.DARK_AQUA + "Percentage: " + ChatColor.AQUA + booster.getPercentage() + "%");
            list.add(ChatColor.DARK_AQUA + "Remaining: " + ChatColor.AQUA + GameUtil.format(booster.getRemainingTime()));
            meta.setLore(list);

            stack.setItemMeta(meta);
            inventory.setItem(i, stack);
        }


        for (int x = 0; x < inventories.size(); x++) {
            Inventory current = inventories.get(x);

            if (x != 0) {
                current.setItem(45, PREVIOUS_ARROW.clone());
            }

            if (x < (inventories.size() - 1)) {
                current.setItem(53, NEXT_ARROW.clone());
            }

            for (int z = 45; z < 54; z++) {
                if (current.getItem(z) == null) {
                    current.setItem(z, PANE.clone());
                }
            }

        }

        return inventories;
    }




}
