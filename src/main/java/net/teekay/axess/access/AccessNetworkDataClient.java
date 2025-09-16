package net.teekay.axess.access;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AccessNetworkDataClient {

    private static final HashMap<UUID, AccessNetwork> networkRegistry = new HashMap<>();

    public static HashMap<UUID, AccessNetwork> getNetworkRegistry() {
        return networkRegistry;
    }

    public static void loadAllFromServer(AccessNetworkDataServer serverData) {
        networkRegistry.clear();

        for (HashMap.Entry<UUID, AccessNetwork> entry : serverData.getNetworkRegistry().entrySet()){
            networkRegistry.put(entry.getKey(), entry.getValue());
        }
    }

    public static AccessNetwork getNetwork(UUID uuid) {
        return networkRegistry.get(uuid);
    }

    public static void setNetwork(AccessNetwork network) {
        networkRegistry.put(network.getUUID(), network);
    }

    public static void removeNetwork(UUID uuid) {
        networkRegistry.remove(uuid);
    }
}
