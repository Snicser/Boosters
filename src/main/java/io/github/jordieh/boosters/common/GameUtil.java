package io.github.jordieh.boosters.common;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public final class GameUtil {

    private GameUtil() {
    }

    public static ItemStack item(Material material) {
        return new ItemStack(material);
    }

    public static ItemStack item(Material material, String s) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(s == null ? ChatColor.RESET.toString() : s);
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack skull(String owner, String name, String lore) {
        ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.setOwner(owner);

        if (name != null) meta.setDisplayName(name);
        if (lore != null) meta.setLore(Collections.singletonList(lore));

        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack skull(String owner) {
        ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.setOwner(owner);
        stack.setItemMeta(meta);
        return stack;
    }

    public static String format(long millis) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - (minutes * 60);

        Format format = new DecimalFormat("00");
        return format.format(minutes) + ":" + format.format(seconds);
    }

}
