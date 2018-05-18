package io.github.jordieh.boosters.framework.booster;

import io.github.jordieh.boosters.common.GameUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BoosterHolderListener implements Listener {

    private static final ItemStack PANE = GameUtil.item(Material.STAINED_GLASS_PANE);
    private static final ItemStack PLAYER_BOOSTERS = GameUtil.skull("MHF_Chest");
    private static final ItemStack NEXT_ARROW = GameUtil.skull("MHF_ArrowRight");
    private static final ItemStack PREVIOUS_ARROW = GameUtil.skull("MHF_ArrowLeft");

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

        if (stack == null || !(inventory.getHolder() instanceof BoosterHolder)) {
            return;
        }

        event.setCancelled(true);

        BoosterHolder holder = (BoosterHolder) inventory.getHolder();

        if (PREVIOUS_ARROW.isSimilar(stack)) {
            holder.previousPage();
            return;
        }

        if (PLAYER_BOOSTERS.isSimilar(stack)) {
            // TODO Player boosters
            return;
        }

        if (NEXT_ARROW.isSimilar(stack)) {
            holder.nextPage();
            return;  //fixme, necessary?
        }


        // TODO code
    }

}
