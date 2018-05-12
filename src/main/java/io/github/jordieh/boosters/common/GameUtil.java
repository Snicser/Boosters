package io.github.jordieh.boosters.common;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public final class GameUtil {

    private GameUtil() {
    }

    public static ItemStack item(Material material) {
        return new ItemStack(material);
    }

    public static ItemStack skull(String owner) {
        ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.setOwner(owner);
        stack.setItemMeta(meta);
        return stack;
    }
}
