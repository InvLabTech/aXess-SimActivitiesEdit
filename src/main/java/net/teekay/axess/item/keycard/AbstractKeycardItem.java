package net.teekay.axess.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.teekay.axess.Axess;
import net.teekay.axess.access.AccessLevel;
import net.teekay.axess.access.AccessNetwork;
import net.teekay.axess.access.AccessNetworkDataClient;
import net.teekay.axess.access.AccessNetworkDataServer;
import org.checkerframework.checker.index.qual.PolyUpperBound;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractKeycardItem extends Item {
    private static final String ACCESS_LEVEL_KEY = "AccessLevel";
    private static final String ACCESS_NETWORK_KEY = "AccessNetwork";

    public AbstractKeycardItem(Item.Properties properties) {
        super(properties);
    }

    @Nullable
    public AccessNetwork getAccessNetwork(ItemStack stack, @Nullable Level level) {
        CompoundTag tag = stack.getOrCreateTag();

        if (!tag.contains(ACCESS_NETWORK_KEY)) {
            return null;
        }

        if (level == null || level.isClientSide()) {
            return AccessNetworkDataClient.getNetwork(tag.getUUID(ACCESS_NETWORK_KEY));
        } else {
            return AccessNetworkDataServer.get(level.getServer()).getNetwork(tag.getUUID(ACCESS_NETWORK_KEY));
        }
    }


    @Nullable
    public AccessLevel getAccessLevel(ItemStack stack, @Nullable Level level) {
        CompoundTag tag = stack.getOrCreateTag();
        AccessNetwork net = getAccessNetwork(stack, level);

        if (!tag.contains(ACCESS_LEVEL_KEY) || net == null) {
            return null;
        }

        return net.getAccessLevel(stack.getOrCreateTag().getUUID(ACCESS_LEVEL_KEY));
    }

    public void setAccessNetwork(ItemStack stack, AccessNetwork network) {
        CompoundTag tag = stack.getOrCreateTag();

        tag.putUUID(ACCESS_NETWORK_KEY, network.getUUID());
    }

    public void setAccessLevel(ItemStack stack, AccessLevel level) {
        CompoundTag tag = stack.getOrCreateTag();

        tag.putUUID(ACCESS_LEVEL_KEY, level.getUUID());
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pLevel == null) {super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced); return;}
        if (!pLevel.isClientSide()) return;

        AccessNetwork net = getAccessNetwork(pStack, pLevel);
        AccessLevel level = getAccessLevel(pStack, pLevel);

        if (net != null && level != null) {
            pTooltipComponents.add(
                    Component.translatable("tooltip."+ Axess.MODID + ".keycard.access_network")
                            .append(": ")
                            .append(net.getName())
                            .withStyle(ChatFormatting.GRAY)
            );

            pTooltipComponents.add(
                    Component.translatable("tooltip."+ Axess.MODID + ".keycard.access_level")
                            .append(": ")
                            .append(level.getName())
                            .withStyle(ChatFormatting.GRAY)
            );
        } else {
            pTooltipComponents.add(
                    Component.translatable("tooltip."+ Axess.MODID + ".keycard.unconfigured")
                        .withStyle(ChatFormatting.GRAY)
            );
        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
