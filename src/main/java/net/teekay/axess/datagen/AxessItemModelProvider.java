package net.teekay.axess.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.teekay.axess.Axess;
import net.teekay.axess.registry.AxessIconRegistry;

public class AxessItemModelProvider extends ItemModelProvider {
    public AxessItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Axess.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }
}
