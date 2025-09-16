package net.teekay.axess.screen.component;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.teekay.axess.Axess;

public class TexturedEditBox extends EditBox {
    private static ResourceLocation EDIT_BOX_TEXTURE = ResourceLocation.fromNamespaceAndPath(Axess.MODID, "textures/gui/buttons.png");

    private boolean redIfBlank;

    public TexturedEditBox(Font pFont, int pX, int pY, int pWidth, int pHeight, Component pMessage) {
        this(pFont, pX, pY, pWidth, pHeight, pMessage, true);
    }

    public TexturedEditBox(Font pFont, int pX, int pY, int pWidth, int pHeight, Component pMessage, boolean redIfBlank) {
        super(pFont, pX, pY, pWidth, pHeight, pMessage);
        this.setBordered(false);
        this.redIfBlank = redIfBlank;
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        super.onClick(pMouseX, pMouseY);
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

        boolean isHovering = isHoveredOrFocused();
        boolean isBlank = redIfBlank && this.getValue().isEmpty();

        pGuiGraphics.blit(
                EDIT_BOX_TEXTURE,
                this.getX(),
                this.getY(),
                0,
                isBlank ? 40 : (isHovering ? 20 : 0),
                this.width,
                this.height,
                256,
                256
        );

        pGuiGraphics.blit(
                EDIT_BOX_TEXTURE,
                this.getX() + width - 1,
                this.getY(),
                0,
                isBlank ? 40 : (isHovering ? 20 : 0),
                1,
                this.height,
                256,
                256
        );

        int x = this.getX();
        int y = this.getY();
        this.setX(x + 4);
        this.setY(y + (this.height - 8) / 2 );
        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.setX(x);
        this.setY(y);
    }

}
