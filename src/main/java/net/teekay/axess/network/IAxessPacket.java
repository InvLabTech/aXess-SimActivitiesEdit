package net.teekay.axess.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public interface IAxessPacket {
    void encode(FriendlyByteBuf buffer);
    void handle(Supplier<NetworkEvent.Context> contextSupplier);
}
