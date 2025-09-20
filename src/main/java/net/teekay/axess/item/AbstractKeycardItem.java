package net.teekay.axess.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.teekay.axess.Axess;
import net.teekay.axess.access.AccessLevel;
import net.teekay.axess.access.AccessNetwork;
import net.teekay.axess.access.AccessNetworkDataClient;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractKeycardItem extends Item {
    private static final String ACCESS_LEVEL_KEY = "AccessLevel";
    private static final String ACCESS_NETWORK_KEY = "AccessNetwork";

    public AbstractKeycardItem(Item.Properties properties) {
        super(properties);
    }

    @Nullable
    public AccessNetwork getAccessNetwork(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();

        if (!tag.contains(ACCESS_NETWORK_KEY)) {
            return null;
        }

        return AccessNetworkDataClient.getNetwork(tag.getUUID(ACCESS_NETWORK_KEY));
    }


    public AccessLevel getAccessLevel(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        AccessNetwork net = getAccessNetwork(stack);

        if (!tag.contains(ACCESS_LEVEL_KEY) || net == null) {
            return null;
        }

        return net.getAccessLevel(stack.getOrCreateTag().getUUID(ACCESS_LEVEL_KEY));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pLevel == null) {super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced); return;}
        if (!pLevel.isClientSide()) return;

        AccessNetwork net = getAccessNetwork(pStack);
        AccessLevel level = getAccessLevel(pStack);

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
