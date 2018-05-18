package io.github.jordieh.boosters.common;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private final ItemStack stack;
    private final List<String> lores;
    private final ItemMeta meta;
    private String name;

    public ItemBuilder(ItemStack stack) {
        this.stack = stack;
        this.lores = new ArrayList<>();
        this.meta = stack.getItemMeta();
    }

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemStack retrieve() {
        meta.setLore(lores);
        meta.setDisplayName(name);
        stack.setItemMeta(meta);
        return stack;
    }

    public ItemBuilder addLore(String s) {
        lores.add(s);
        return this;
    }

    public ItemBuilder resetName() {
        name = ChatColor.RESET.toString();
        return this;
    }

}
