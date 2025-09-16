package net.teekay.axess.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.teekay.axess.Axess;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Axess.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AxessDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new AxessRecipeProvider(packOutput));
        generator.addProvider(event.includeServer(), AxessLootTableProvider.create(packOutput));

        generator.addProvider(event.includeClient(), new AxessBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new AxessItemModelProvider(packOutput, existingFileHelper));

        AxessBlockTagProvider blockTagProvider = generator.addProvider(event.includeServer(), new AxessBlockTagProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new AxessItemTagsProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), existingFileHelper));
    }

}
