package net.teekay.axess.screen.component;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.teekay.axess.Axess;
import net.teekay.axess.access.AccessNetwork;
import net.teekay.axess.client.AxessClientMenus;

public class NetworkEntry {
    public static ResourceLocation TRASH_BUTTON = ResourceLocation.fromNamespaceAndPath(Axess.MODID, "textures/gui/delete_button.png");

    private static final Component EDIT_TEXT = Component.translatable("gui."+Axess.MODID+".button.edit");
    private static final Component DELETE_TEXT = Component.translatable("gui."+Axess.MODID+".button.delete");

    public AccessNetwork network;
    public TexturedButton button;
    public HumbleImageButton trashButton;

    public NetworkEntry(NetworkList list, AccessNetwork network)
    {
        int pX = list.leftPos;
        int pY = list.topPos;
        int pWidth = list.width;
        int pHeight = list.elemHeight;

        this.button = new TexturedButton(pX, pY, pWidth-21, pHeight, Component.literal(network.getName()), btn -> {
            AxessClientMenus.openNetworkEditorScreen(network);
        });

        this.trashButton = new HumbleImageButton(
                pX + pWidth - 20,
                pY,
                20,
                20,
                0,
                0,
                20,
                TRASH_BUTTON,
                32, 64,
                btn -> {
                    AxessClientMenus.openNetworkDeletionScreen(network);
                });

        this.button.setTooltip(Tooltip.create(EDIT_TEXT));
        this.trashButton.setTooltip(Tooltip.create(DELETE_TEXT));

        this.button.setBounds(pX, pY, pX+pWidth, pY+list.height);
        this.trashButton.setBounds(pX, pY, pX+pWidth, pY+list.height);

        this.network = network;
    }

}