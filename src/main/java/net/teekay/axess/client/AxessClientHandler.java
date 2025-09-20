package net.teekay.axess.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.teekay.axess.Axess;
import net.teekay.axess.block.readers.KeycardReaderBlockEntityRenderer;
import net.teekay.axess.registry.AxessBlockEntityRegistry;
import net.teekay.axess.registry.AxessMenuRegistry;
import net.teekay.axess.screen.KeycardEditorScreen;

@Mod.EventBusSubscriber(modid = Axess.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AxessClientHandler {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(AxessMenuRegistry.KEYCARD_EDITOR_MENU.get(), KeycardEditorScreen::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(AxessBlockEntityRegistry.IRON_KEYCARD_READER.get(), KeycardReaderBlockEntityRenderer::new);
    }

}
