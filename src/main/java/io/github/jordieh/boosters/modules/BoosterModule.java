package io.github.jordieh.boosters.modules;

import io.github.jordieh.boosters.framework.booster.Booster;
import io.github.jordieh.boosters.BoosterPlugin;
import io.github.jordieh.boosters.common.BoosterSet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BoosterModule {

    private static BoosterModule instance;

    private Map<UUID, BoosterSet> map;
    private BoosterSet active;

    private BoosterModule() {
        active = new BoosterSet();
        map = new HashMap<>();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(BoosterPlugin.getInstance(), active::clean, 0L, 20L);
    }

    public static BoosterModule getInstance() {
        return instance == null ? instance = new BoosterModule() : instance;
    }

    public BoosterSet getBoosters(Player player) {
        UUID uuid = player.getUniqueId();
        if (!map.containsKey(uuid)) {
            map.put(uuid, new BoosterSet());
        }

        return map.get(uuid);
    }

    public boolean activate(Booster booster) {
        if (active.add(booster)) {
            booster.activate();
            return true;
        }
        return false;
    }

}
