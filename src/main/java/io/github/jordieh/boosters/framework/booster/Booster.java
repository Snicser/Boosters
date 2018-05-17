package io.github.jordieh.boosters.framework.booster;

import io.github.jordieh.boosters.common.BoosterSet;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public final class Booster {

    private static final BoosterSet cache = new BoosterSet();

    @Getter private final int percentage;
    @Getter private final long duration;
    @Getter private final UUID owner;
    @Getter private final UUID uuid;

    private boolean finished;
    @Getter private boolean active;
    private long start;

    public Booster(UUID owner, int percentage, long duration) {
        this.owner = owner;
        this.percentage = percentage;
        this.duration = duration;
        this.uuid = UUID.randomUUID(); // Unique id for the booster
    }

    public boolean activate() {
        if (active || finished) {
            throw new IllegalArgumentException("Booster " + uuid + " is already active");
        }

        log.info("Activated the booster " + uuid + " (" + percentage + "%) - (" + duration + "ms)");

        active = true;
        start = System.currentTimeMillis();
        return cache.add(this);
    }

    public boolean deactivate() {
        if (!active) {
            throw new IllegalArgumentException("Booster " + uuid + " is already inactive");
        }

        log.info("Deactivated booster " + uuid);

        finished = true;
        active = false;
        return cache.remove(this);
    }

    public long getRemainingTime() {
        return (start + duration) - System.currentTimeMillis();
    }

    @Override
    public int hashCode() {
        return uuid.hashCode() ^ (percentage << 16);
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
