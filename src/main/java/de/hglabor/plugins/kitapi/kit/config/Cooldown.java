package de.hglabor.plugins.kitapi.kit.config;

public class Cooldown {
    private final long endTime;
    private boolean hasCooldown;
    private int additionalTime = 0;
    private final float cooldown;

    public Cooldown(boolean hasCooldown) {
        this(hasCooldown, 0);
    }

    public Cooldown(boolean hasCooldown, float cooldown) {
        this.hasCooldown = hasCooldown;
        this.endTime = System.currentTimeMillis() + (long) (cooldown * 1000L);
        this.cooldown = cooldown;
    }

    public float getCooldown() {
        return cooldown;
    }

    public boolean hasCooldown() {
        return hasCooldown;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setCooldown(boolean hasCooldown) {
        this.hasCooldown = hasCooldown;
    }

    public int getAdditionalTime() { //where tf is this used
        return additionalTime;
    }
}
