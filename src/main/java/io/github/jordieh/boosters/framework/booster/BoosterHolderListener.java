package io.github.jordieh.boosters.framework.booster;

import io.github.jordieh.boosters.BoosterPlugin;
import io.github.jordieh.boosters.common.GameUtil;
import io.github.jordieh.boosters.modules.BoosterModule;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BoosterHolderListener implements Listener {

    private static final ItemStack PANE = GameUtil.item(Material.STAINED_GLASS_PANE, null);
    private static final ItemStack PLAYER_BOOSTERS = GameUtil.item(Material.CHEST, "Check your boosters");
    private static final ItemStack NEXT_ARROW = GameUtil.item(Material.ARROW, "Next page");
    private static final ItemStack PREVIOUS_ARROW = GameUtil.item(Material.ARROW, "Previous page");
    private static final ItemStack BOOSTER_MENU = GameUtil.item(Material.BARRIER, "Go back");

    public static ItemStack getBoosterMenu() {
        return BOOSTER_MENU.clone();
    }

    public static ItemStack getPlayerBoosters() {
        return PLAYER_BOOSTERS.clone();
    }

    public static ItemStack getPane() {
        return PANE.clone();
    }

    public static ItemStack getNextArrow() {
        return NEXT_ARROW.clone();
    }

    public static ItemStack getPreviousArrow() {
        return PREVIOUS_ARROW.clone();
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        ItemStack stack = event.getCurrentItem();

        if (stack == null) {
            return;
        }

        HumanEntity entity = event.getWhoClicked();

        if (inventory.getHolder() instanceof BoosterHolder) {
            event.setCancelled(true);
            BoosterHolder holder = (BoosterHolder) inventory.getHolder();

            if (PREVIOUS_ARROW.isSimilar(stack)) {
                BoosterPlugin.getInstance().getServer().getScheduler().runTask(BoosterPlugin.getInstance(), () -> {
                    entity.closeInventory();
                    holder.previousPage();
                    entity.openInventory(holder.getInventory());
                });
                return;
            }

            if (PLAYER_BOOSTERS.isSimilar(stack)) {
                BoosterPlugin.getInstance().getServer().getScheduler().runTask(BoosterPlugin.getInstance(), () -> {
                    PlayerBoosterHolder inv = holder.getPlayerBoosters();
                    if (inv.isPresent()) {
                        entity.closeInventory();
                        entity.openInventory(inv.getInventory());
                    } else {
                        entity.sendMessage(ChatColor.RED + "You currently don't have any a boosters");
                    }
                });
                return;
            }

            if (NEXT_ARROW.isSimilar(stack)) {
                BoosterPlugin.getInstance().getServer().getScheduler().runTask(BoosterPlugin.getInstance(), () -> {
                    entity.closeInventory();
                    holder.nextPage();
                    entity.openInventory(holder.getInventory());
                });
                return;
            }

            return;
        }

        if (inventory.getHolder() instanceof PlayerBoosterHolder) {
            event.setCancelled(true);
            PlayerBoosterHolder holder = (PlayerBoosterHolder) inventory.getHolder();

            if (stack.getType() == Material.EMERALD) {
                UUID uuid = UUID.fromString(stack.getItemMeta().getLore().get(2).replace(ChatColor.GRAY + "UUID ", ""));
                Booster booster = BoosterModule.getInstance().getBooster(uuid);
                if (booster != null) {
                    if (!BoosterModule.getInstance().activate(booster)) {
                        return;
                    }
                    BoosterPlugin.getInstance().getServer().getScheduler().runTask(BoosterPlugin.getInstance(), () -> {
                        entity.closeInventory();
                        holder.updateBoosters();
                        if (holder.isPresent()) {
                            entity.openInventory(holder.getInventory());
                        } else {
                            entity.openInventory(holder.getParent().getInventory());
                        }
                    });
                }
                return;
            }

            if (PREVIOUS_ARROW.isSimilar(stack)) {
                BoosterPlugin.getInstance().getServer().getScheduler().runTask(BoosterPlugin.getInstance(), () -> {
                    entity.closeInventory();
                    holder.previousPage();
                    entity.openInventory(holder.getInventory());
                });
                return;
            }

            if (BOOSTER_MENU.isSimilar(stack)) {
                BoosterPlugin.getInstance().getServer().getScheduler().runTask(BoosterPlugin.getInstance(), () -> {
                    entity.closeInventory();
                    entity.openInventory(new BoosterHolder(entity).getInventory());
                });
                return;
            }

            if (NEXT_ARROW.isSimilar(stack)) {
                BoosterPlugin.getInstance().getServer().getScheduler().runTask(BoosterPlugin.getInstance(), () -> {
                    entity.closeInventory();
                    holder.nextPage();
                    entity.openInventory(holder.getInventory());
                });
            }
        }

    }

}
