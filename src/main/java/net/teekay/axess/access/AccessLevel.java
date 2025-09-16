package net.teekay.axess.access;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class AccessLevel {
    private String displayName;
    private String symbol;

    private final UUID uuid;
    private final UUID networkUUID;

    private int priority;

    public AccessLevel(UUID networkUUID) {
        this(networkUUID, UUID.randomUUID());
    }

    public AccessLevel(UUID networkUUID, UUID uuid) {
        this.uuid = uuid;
        this.displayName = "New Access Level";
        this.symbol = "1";
        this.priority = -1;
        this.networkUUID = networkUUID;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public UUID getUUID() { return this.uuid; }

    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();

        tag.putUUID("UUID", uuid);
        tag.putUUID("NetworkUUID", networkUUID);

        tag.putString("Name", displayName);
        tag.putString("Symbol", symbol);

        tag.putInt("Priority", priority);

        return tag;
    }

    public static AccessLevel fromNBT(CompoundTag tag) {
        UUID uuid = tag.getUUID("UUID");
        UUID networkUUID = tag.getUUID("NetworkUUID");

        AccessLevel newAccessLevel = new AccessLevel(networkUUID, uuid);

        newAccessLevel.displayName = tag.getString("Name");
        newAccessLevel.symbol = tag.getString("Symbol");

        newAccessLevel.priority = tag.getInt("Priority");

        return newAccessLevel;
    }

}
