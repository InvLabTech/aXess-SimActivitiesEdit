package net.teekay.axess.block.readers;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;
import net.teekay.axess.Axess;
import net.teekay.axess.access.AccessCompareMode;
import net.teekay.axess.access.AccessLevel;
import net.teekay.axess.access.AccessNetwork;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KeycardReaderBlockEntity extends BlockEntity {

    public UUID networkID = UUID.randomUUID();
    public List<UUID> accessLevels = new ArrayList<>();
    public AccessCompareMode compareMode = AccessCompareMode.SPECIFIC;

    public static final String TAG_ACCESS_LEVELS = "levels";
    public static final String TAG_ACCESS_NETWORK = "network";
    public static final String TAG_COMPARE_MODE = "compare_mode";

    public KeycardReaderBlockEntity(BlockEntityType<?> type, BlockPos pPos, BlockState pBlockState) {
        super(type, pPos, pBlockState);
    }

    public static void setAccessMode(ItemStack stack, AccessCompareMode mode) {
        //stack.getOrCreateTag().getCompound(STORAGE_TAG).putString(TAG_ACCESS_MODE, mode.toString());
    }

    public static void unlinkNetwork(ItemStack stack) {
        //stack.getOrCreateTag().getCompound(STORAGE_TAG).remove(TAG_ACCESS_NETWORK);
    }

    public static void linkNetwork(ItemStack stack, AccessNetwork network) {
        //stack.getOrCreateTag().getCompound(STORAGE_TAG).putString(TAG_ACCESS_NETWORK, network.getUUID().toString());
    }

    public static void removeAccessLevel(ItemStack stack, AccessLevel level) {
        //stack.getOrCreateTag().getCompound(STORAGE_TAG).getCompound(TAG_ACCESS_LEVELS).putBoolean(level.getUUID().toString(), false);
    }

    public static void addAccessLevel(ItemStack stack, AccessLevel level) {
        //stack.getOrCreateTag().getCompound(STORAGE_TAG).getCompound(TAG_ACCESS_LEVELS).putBoolean(level.getUUID().toString(), true);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        CompoundTag modTag = new CompoundTag();

        modTag.putUUID(TAG_ACCESS_NETWORK, networkID);

        ListTag accessLevelsTag = new ListTag();
        for (UUID uuid :
                accessLevels) {
            accessLevelsTag.add(StringTag.valueOf(uuid.toString()));
        }

        modTag.put(TAG_ACCESS_LEVELS, accessLevelsTag);
        modTag.putString(TAG_COMPARE_MODE, compareMode.toString());

        pTag.put(Axess.MODID, modTag);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        CompoundTag modTag = pTag.getCompound(Axess.MODID);

        networkID = modTag.getUUID(TAG_ACCESS_NETWORK);

        ListTag accessLevelsTag = (ListTag) modTag.get(TAG_ACCESS_LEVELS);
        ArrayList<UUID> newAccessLevels = new ArrayList<>();
        for (int i = 0; i < accessLevelsTag.size(); i++) {
            newAccessLevels.add(UUID.fromString(accessLevelsTag.getString(i)));
        }
        accessLevels = newAccessLevels;

        compareMode = AccessCompareMode.valueOf(modTag.getString(TAG_COMPARE_MODE));

        super.load(pTag);
    }
}
