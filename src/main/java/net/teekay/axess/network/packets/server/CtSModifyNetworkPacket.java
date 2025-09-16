package net.teekay.axess.network.packets.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.teekay.axess.access.AccessNetwork;
import net.teekay.axess.access.AccessNetworkDataClient;
import net.teekay.axess.access.AccessNetworkDataServer;
import net.teekay.axess.network.IAxessPacket;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public class CtSModifyNetworkPacket implements IAxessPacket {
    public AccessNetwork network;

    public CtSModifyNetworkPacket(AccessNetwork network) {
        this.network = network;
    }

    public CtSModifyNetworkPacket(FriendlyByteBuf buffer) {
        this.network = AccessNetwork.fromNBT(Objects.requireNonNull(buffer.readNbt()));
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeNbt(network.toNBT());
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        if (context.getDirection().getReceptionSide().isClient()) {
            context.setPacketHandled(false);
            return;
        }

        try {
            AccessNetworkDataServer serverNetworkData = AccessNetworkDataServer.get(Objects.requireNonNull(context.getSender()).server);
            AccessNetwork networkToChange = serverNetworkData.getNetwork(network.getUUID());
            ServerPlayer player = context.getSender();

            if (
                    (networkToChange == null && player.getUUID().equals(network.getOwnerUUID())) // network is being created
                    || (networkToChange != null && networkToChange.getOwnerUUID().equals(player.getUUID())) // network is being modified
            ) {
                serverNetworkData.setNetwork(network);
            } else {
                context.setPacketHandled(false);
                return;
            }

        } catch (Exception e) {
            context.setPacketHandled(false);
            return;
        }
    }
}
