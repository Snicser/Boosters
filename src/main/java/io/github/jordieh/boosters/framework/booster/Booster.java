package io.github.jordieh.boosters.framework.booster;

import io.github.jordieh.boosters.common.BoosterSet;
import io.github.jordieh.boosters.common.Logger;
import lombok.Getter;

import java.util.UUID;

public final class Booster {

    @Getter private final static BoosterSet cache = new BoosterSet(); //todo

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
        cache.add(this);
        Logger.info("Created booster " + uuid + " with owner " + owner);
    }

    public Booster(UUID owner, UUID uuid, int percentage, long duration) {
        this.owner = owner;
        this.percentage = percentage;
        this.duration = duration;
        this.uuid = (uuid == null) ? UUID.randomUUID() : uuid; // Unique id for the booster
        cache.add(this);
        Logger.info("Created booster " + uuid + " with owner " + owner);
    }

    public boolean activate() {
        if (active || finished) {
            throw new IllegalArgumentException("Booster " + uuid + " is already active");
        }

        Logger.info("Activated the booster " + uuid + " (" + percentage + "%) - (" + duration + "ms)");

        active = true;
        start = System.currentTimeMillis();
        return true;
    }

    public boolean deactivate() {
        if (!active) {
            throw new IllegalArgumentException("Booster " + uuid + " is already inactive");
        }

        Logger.info("Deactivated booster " + uuid);

        finished = true;
        active = false;
        return true;
    }

    /**
     * For database use
     *
     * @return true if the booster has been activated
     */
    public boolean isActivated() {
        return finished || active;
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
