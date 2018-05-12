package io.github.jordieh.boosters.framework.booster;

import io.github.jordieh.boosters.BoosterPlugin;
import io.github.jordieh.boosters.common.BoosterSet;
import io.github.jordieh.boosters.common.GameUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public final class Booster {

    private static final BoosterSet cache = new BoosterSet();

    private final ItemStack stack;
    private final int percentage;
    private final long duration;
    private final String name;
    private final UUID owner;
    private final UUID uuid;

    private boolean finished;
    private boolean active;
    private long start;

    private Booster(UUID owner, ItemStack stack, int percentage, int duration, String name) {
        this.owner = owner;
        this.stack = stack;
        this.percentage = percentage;
        this.duration = duration;
        this.name = name;
        this.uuid = UUID.randomUUID(); // Unique id for the booster
        BoosterPlugin.getInstance().getLogger().info("$ Booster " + uuid + " (duration: " + duration + ")");
    }

    public static Booster of(Player player, int percentage, int duration, String name) {
        ItemStack stack = GameUtil.skull(player.getName());
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        stack.setItemMeta(meta);
        stack.setAmount(percentage);
        return new Booster(player.getUniqueId(), stack, percentage, duration, name);
    }

    public boolean activate() {
        if (active || finished) {
            throw new IllegalArgumentException("Booster " + uuid + " is already active");
        }

        BoosterPlugin.getInstance().getLogger().info("+ Booster " + uuid);

        active = true;
        start = System.currentTimeMillis();
        return cache.add(this);
    }

    public boolean deactivate() {
        if (!active) {
            throw new IllegalArgumentException("Booster " + uuid + " is already inactive");
        }

        BoosterPlugin.getInstance().getLogger().info("- Booster " + uuid);

        finished = true;
        active = false;
        return cache.remove(this);
    }

    public long getRemainingTime() {
        return (start + duration) - System.currentTimeMillis();
    }

    @Override
    public int hashCode() {
        return (int) (uuid.hashCode() ^ (duration >>> 16));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Booster)) {
            return false;
        }
        return this.hashCode() == obj.hashCode();
    }

}
