package net.teekay.axess.screen.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.teekay.axess.Axess;
import net.teekay.axess.access.AccessLevel;
import net.teekay.axess.access.AccessNetwork;
import net.teekay.axess.client.AxessClientMenus;
import net.teekay.axess.screen.NetworkEditorScreen;

import javax.security.auth.callback.Callback;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class AccessLevelEntry extends AbstractWidget {
    public static ResourceLocation TRASH_BUTTON = ResourceLocation.fromNamespaceAndPath(Axess.MODID, "textures/gui/delete_button.png");
    public static ResourceLocation TRASH_BUTTON_DISABLED = ResourceLocation.fromNamespaceAndPath(Axess.MODID, "textures/gui/delete_button_disabled.png");
    public static ResourceLocation ASCEND_BUTTON = ResourceLocation.fromNamespaceAndPath(Axess.MODID, "textures/gui/ascend_button.png");
    public static ResourceLocation DESCEND_BUTTON = ResourceLocation.fromNamespaceAndPath(Axess.MODID, "textures/gui/descend_button.png");

    public static ResourceLocation NETWORK_EDITOR_TEX = ResourceLocation.fromNamespaceAndPath(Axess.MODID, "textures/gui/network_editor.png");

    private static final Component NAME_TEXT = Component.translatable("gui."+Axess.MODID+".input.access_level_name");
    private static final Component DELETE_TEXT = Component.translatable("gui."+Axess.MODID+".button.shift_delete");
    private static final Component PRIORITY_TEXT = Component.translatable("gui."+Axess.MODID+".button.priority");

    public AccessLevel accessLevel;

    public TexturedEditBox editBox;
    public ImageButton trashButton;
    public ImageButton dragButton;
    //public ImageButton priorityButtonUP;
    //public ImageButton priorityButtonDOWN;

    public float animatedYPosition = -1000;
    public float targetYPosition = -1000;

    public boolean dragging = false;

    private Consumer<AbstractWidget> childrenRemover;

    public AccessLevelEntry(Consumer<AbstractWidget> childrenAdder, Consumer<AbstractWidget> childrenRemover, AccessLevel accessLevel, int pX, int pY, int pWidth, int pHeight, Runnable onTrash, Consumer<AccessLevelEntry> onStartDrag, Consumer<AccessLevelEntry> onEndDrag)
    {
        super(pX, pY, pWidth, pHeight, Component.empty());

        this.childrenRemover = childrenRemover;

        this.editBox = new TexturedEditBox(Minecraft.getInstance().font, pX+42, pY, pWidth-63, pHeight, Component.literal(accessLevel.getDisplayName()));
        this.editBox.setTooltip(Tooltip.create(NAME_TEXT));
        this.editBox.setResponder(accessLevel::setDisplayName);
        this.editBox.setValue(accessLevel.getDisplayName());

        this.trashButton = new ImageButton(
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

        childrenAdder.accept(this.editBox);
        childrenAdder.accept(this.trashButton);
        childrenAdder.accept(this.dragButton);


        /*this.priorityButtonUP = new ImageButton(
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

        this.priorityButtonDOWN = new ImageButton(
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
        //this.priorityButtonUP.setY(Math.round(animatedYPosition) + offset);
        //this.priorityButtonDOWN.setY(Math.round(animatedYPosition) + offset);
    }

    public void remove() {
        childrenRemover.accept(this.editBox);
        childrenRemover.accept(this.trashButton);
        childrenRemover.accept(this.dragButton);
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