package net.teekay.axess.screen.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.teekay.axess.Axess;
import net.teekay.axess.access.AccessLevel;
import net.teekay.axess.utilities.AxessColors;

import java.util.function.Consumer;

public class AccessLevelEntry extends AbstractWidget {
    public static ResourceLocation TRASH_BUTTON = ResourceLocation.fromNamespaceAndPath(Axess.MODID, "textures/gui/delete_button.png");
    public static ResourceLocation TRASH_BUTTON_DISABLED = ResourceLocation.fromNamespaceAndPath(Axess.MODID, "textures/gui/delete_button_disabled.png");
    public static ResourceLocation ASCEND_BUTTON = ResourceLocation.fromNamespaceAndPath(Axess.MODID, "textures/gui/ascend_button.png");
    public static ResourceLocation DESCEND_BUTTON = ResourceLocation.fromNamespaceAndPath(Axess.MODID, "textures/gui/descend_button.png");

    public static ResourceLocation NETWORK_EDITOR_TEX = ResourceLocation.fromNamespaceAndPath(Axess.MODID, "textures/gui/network_editor.png");
    private static final ResourceLocation EMPTY_BUTTON_TEXTURE = ResourceLocation.fromNamespaceAndPath(Axess.MODID, "textures/gui/empty_button.png");

    private static final Component NAME_TEXT = Component.translatable("gui."+Axess.MODID+".input.access_level_name");
    private static final Component DELETE_TEXT = Component.translatable("gui."+Axess.MODID+".button.shift_delete");
    private static final Component ICON_TEXT = Component.translatable("gui."+Axess.MODID+".button.change_icon");

    public AccessLevel accessLevel;

    public TexturedEditBox editBox;
    public HumbleImageButton trashButton;
    public HumbleImageButton dragButton;
    public HumbleImageButton iconButton;
    //public ModestImageButton priorityButtonUP;
    //public ModestImageButton priorityButtonDOWN;

    public float animatedYPosition = -1000;
    public float targetYPosition = -1000;

    public boolean dragging = false;

    private Consumer<AbstractWidget> childrenRemover;

    public AccessLevelEntry(Consumer<AbstractWidget> childrenAdder, Consumer<AbstractWidget> childrenRemover, AccessLevel accessLevel, int pX, int pY, int pWidth, int pHeight, Runnable onTrash, Consumer<AccessLevelEntry> onStartDrag, Consumer<AccessLevelEntry> onEndDrag, Consumer<AccessLevelEntry> onEditIcon)
    {
        super(pX, pY, pWidth, pHeight, Component.empty());

        this.childrenRemover = childrenRemover;

        this.editBox = new TexturedEditBox(Minecraft.getInstance().font, pX+4+1+20+1, pY, pWidth-20-20-4-3, pHeight, Component.literal(accessLevel.getDisplayName()));
        this.editBox.setTooltip(Tooltip.create(NAME_TEXT));
        this.editBox.setResponder(accessLevel::setDisplayName);
        this.editBox.setValue(accessLevel.getDisplayName());

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
                    if (!Screen.hasShiftDown()) return;
                    onTrash.run();
                    remove();
                }
        );
        this.trashButton.setTooltip(Tooltip.create(DELETE_TEXT));

        this.dragButton = new DraggableImageButton(pX, pY, 4, 20, 4, 198, NETWORK_EDITOR_TEX,
                btn -> { // PRESS
                    this.dragging = true;
                    onStartDrag.accept(this);
                },
                btn -> { // RELEASE
                    this.dragging = false;
                    onEndDrag.accept(this);
                });

        this.iconButton = new HumbleImageButton(
                pX + 4 + 1,
                pY,
                20,
                20,
                0,
                0,
                20,
                EMPTY_BUTTON_TEXTURE,
                32, 64,
                btn -> {
                    onEditIcon.accept(this);
                }
        );

        this.iconButton.setTooltip(Tooltip.create(ICON_TEXT));

        childrenAdder.accept(this.editBox);
        childrenAdder.accept(this.trashButton);
        childrenAdder.accept(this.dragButton);
        childrenAdder.accept(this.iconButton);


        /*this.priorityButtonUP = new ModestImageButton(
                pX + pWidth - 20 - 20 - 1,
                pY,
                20,
                20,
                0,
                0,
                20,
                ASCEND_BUTTON,
                32, 64,
                btn -> {
                    changePriority.accept(1);
                }
        );
        this.priorityButtonUP.setTooltip(Tooltip.create(PRIORITY_TEXT));

        this.priorityButtonDOWN = new ModestImageButton(
                pX + pWidth - 20 - 20 - 1,
                pY,
                20,
                20,
                0,
                0,
                20,
                DESCEND_BUTTON,
                32, 64,
                btn -> {
                    changePriority.accept(-1);
                }
        );
        this.priorityButtonDOWN.setTooltip(Tooltip.create(PRIORITY_TEXT));*/

        this.accessLevel = accessLevel;
    }

    public void forceUpdateYPos(int yPos, float partialTick) {
        targetYPosition = yPos;
        animatedYPosition = yPos;

        updateYPos(yPos, partialTick, 0);
    }

    public void updateYPos(int yPos, float partialTick, int offset) {
        targetYPosition = yPos;
        if (animatedYPosition == -1000) animatedYPosition = targetYPosition;

        //float dif = targetYPosition - animatedYPosition;
        //float move = dif * (partialTick * 3 / 20f);

        //animatedYPosition += move;

        animatedYPosition = targetYPosition;

        setY(Math.round(animatedYPosition) + offset);
        this.editBox.setY(Math.round(animatedYPosition) + offset);
        this.trashButton.setY(Math.round(animatedYPosition) + offset);
        this.dragButton.setY(Math.round(animatedYPosition) + offset);
        this.iconButton.setY(Math.round(animatedYPosition) + offset);
        //this.priorityButtonUP.setY(Math.round(animatedYPosition) + offset);
        //this.priorityButtonDOWN.setY(Math.round(animatedYPosition) + offset);
    }

    public void remove() {
        childrenRemover.accept(this.editBox);
        childrenRemover.accept(this.trashButton);
        childrenRemover.accept(this.dragButton);
        childrenRemover.accept(this.iconButton);
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        boolean shift = Screen.hasShiftDown();

        if (!shift) {
            //this.priorityButtonUP.visible = index != 0;
            //this.priorityButtonDOWN.visible = false;
            this.trashButton.visible = false;

            pGuiGraphics.blit(TRASH_BUTTON_DISABLED, this.trashButton.getX(), this.trashButton.getY(), 0, 0, this.trashButton.getWidth(), this.trashButton.getHeight(), 32, 64);
        } else {
            //this.priorityButtonUP.visible = false;
            //this.priorityButtonDOWN.visible = index != maxIndex;
            this.trashButton.visible = true;
        }

        this.editBox.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.trashButton.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.dragButton.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.iconButton.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        if (!this.iconButton.isHoveredOrFocused()) pGuiGraphics.setColor(AxessColors.MAIN.getRed(), AxessColors.MAIN.getGreen(), AxessColors.MAIN.getBlue(), 1f);
        pGuiGraphics.blit(this.accessLevel.getIcon().TEXTURE, this.iconButton.getX() + 1, this.iconButton.getY() + 1, 0, 0, 18, 18, 18, 18);
        pGuiGraphics.setColor(1f, 1f, 1f, 1f);
        //this.priorityButtonUP.render(graphics, mouseX, mouseY, partialTick);
        //this.priorityButtonDOWN.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        return;
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        this.dragButton.mouseReleased(pMouseX,pMouseY,pButton);
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }
}