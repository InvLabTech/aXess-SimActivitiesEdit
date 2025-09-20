package net.teekay.axess.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import net.teekay.axess.Axess;
import net.teekay.axess.access.AccessLevel;
import net.teekay.axess.access.AccessNetwork;
import net.teekay.axess.access.AccessNetworkDataClient;
import net.teekay.axess.block.keycardeditor.KeycardEditorBlock;
import net.teekay.axess.block.keycardeditor.KeycardEditorBlockEntity;
import net.teekay.axess.item.AbstractKeycardItem;
import net.teekay.axess.screen.component.HumbleImageButton;
import net.teekay.axess.screen.component.TexturedButton;
import net.teekay.axess.utilities.AxessColors;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KeycardEditorScreen extends AbstractContainerScreen<KeycardEditorMenu> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Axess.MODID, "textures/gui/keycard_editor.png");
    private static final ResourceLocation CONFIRM_BUTTON_TEXTURE = ResourceLocation.fromNamespaceAndPath(Axess.MODID, "textures/gui/confirm_button.png");

    public static final Component TITLE_LABEL = Component.translatable("gui."+Axess.MODID+".keycard_editor");
    public static final Component NO_KEYCARD_LABEL = Component.translatable("gui."+Axess.MODID+".keycard_editor.no_keycard");
    public static final Component NO_NETWORK_LABEL = Component.translatable("gui."+Axess.MODID+".keycard_editor.no_network");
    public static final Component NO_LEVEL_LABEL = Component.translatable("gui."+Axess.MODID+".keycard_editor.no_level");
    public static final Component APPLY_CHANGES_LABEL = Component.translatable("gui."+Axess.MODID+".keycard_editor.apply_changes");

    public static final int KEYCARD_SLOT = 36 + KeycardEditorBlockEntity.KEYCARD_SLOT;

    private AccessNetwork selectedNetwork;
    private AccessLevel selectedLevel;

    private ArrayList<SelectableNetworkEntry> networkEntries = new ArrayList<>();
    private ArrayList<SelectableLevelEntry> levelEntries = new ArrayList<>();
    private ImageButton applyButton;

    private int scrollPosNetworks = 0;
    private int scrollPosLevels = 0;

    private ItemStack lastItemStack;

    public KeycardEditorScreen(KeycardEditorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        this.inventoryLabelY = 10000;
        this.imageWidth = 256;
        this.imageHeight = 233;

        super.init();

        this.applyButton = addRenderableWidget(
                new HumbleImageButton(
                        this.leftPos + 70,
                        this.topPos + 126,
                        20,
                        20,
                        0,
                        0,
                        20,
                        CONFIRM_BUTTON_TEXTURE,
                        32, 96,
                        btn -> {

                        })
        );
        this.applyButton.active = false;

        updateEntries(this.menu.getSlot(KEYCARD_SLOT).getItem());
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        pGuiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        if (!this.menu.getSlot(KEYCARD_SLOT).hasItem()) {
            pGuiGraphics.blit(TEXTURE, this.leftPos+48, this.topPos + 128, 0, 233, 16, 16 );
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        pGuiGraphics.drawString(this.font, TITLE_LABEL, this.leftPos+8, this.topPos+8, AxessColors.MAIN.getRGB(), false);

        // NETWORK ENTRIES
        pGuiGraphics.enableScissor(leftPos + NETWORKS_X, topPos + NETWORKS_Y, leftPos + NETWORKS_X + NETWORKS_WIDTH, topPos + NETWORKS_Y + NETWORKS_HEIGHT);
        for (SelectableNetworkEntry entry:
             networkEntries) {
            entry.render(pGuiGraphics,pMouseX,pMouseY,pPartialTick);
        }
        pGuiGraphics.disableScissor();

        // LEVEL ENTRIES
        pGuiGraphics.enableScissor(leftPos + LEVELS_X, topPos + LEVELS_Y, leftPos + LEVELS_X + LEVELS_WIDTH, topPos + LEVELS_Y + LEVELS_HEIGHT);
        for (SelectableLevelEntry entry:
                levelEntries) {
            entry.render(pGuiGraphics,pMouseX,pMouseY,pPartialTick);
        }
        pGuiGraphics.disableScissor();

        applyButton.active = false;

        // tip
        Component textComp = Component.empty();
        int color = AxessColors.MAIN.getRGB();
        if (!this.menu.getSlot(KEYCARD_SLOT).hasItem()) {
            textComp = NO_KEYCARD_LABEL;
        } else if (selectedNetwork == null) {
            textComp = NO_NETWORK_LABEL;
        } else if (selectedLevel == null) {
            textComp = NO_LEVEL_LABEL;
        } else {
            ItemStack item = this.menu.getSlot(KEYCARD_SLOT).getItem();
            AbstractKeycardItem keycard = (AbstractKeycardItem) item.getItem();
            if (keycard.getAccessNetwork(item) != selectedNetwork || keycard.getAccessLevel(item) != selectedLevel) {
                textComp = APPLY_CHANGES_LABEL;
                applyButton.active = true;
            }
        }
        pGuiGraphics.drawString(this.font, textComp, this.leftPos+94, this.topPos+132, color, false);

        ItemStack item = this.menu.getSlot(KEYCARD_SLOT).getItem();
        if (item != lastItemStack) {
            if (item == null) {
                clearEntries();
            } else if (item.getItem() instanceof AbstractKeycardItem) {
                updateEntries(item);
            } else {
                clearEntries();
            }
            lastItemStack = item;
        }

        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        return;
    }

    public void selectNetwork(AccessNetwork network) {
        selectedNetwork = network;
        selectedLevel = null;
        updateLevelEntries();
    }

    public void selectLevel(AccessLevel level) {
        selectedLevel = level;
    }

    public void updateLevelEntries() {
        clearLevelEntries();
        if (selectedNetwork == null) return;

        ArrayList<AccessLevel> levels = selectedNetwork.getAccessLevels();
        for (int index = 0; index < levels.size(); index++) { // backwards!!!
            SelectableLevelEntry entry = new SelectableLevelEntry(levels.get(levels.size() - index - 1), index);
            levelEntries.add(entry);
            addWidget(entry);
        }
    }

    public void updateNetworkEntries() {
        clearNetworkEntries();

        List<AccessNetwork> networks = AccessNetworkDataClient.getNetworks();
        for (int index = 0; index < networks.size(); index++) {
            SelectableNetworkEntry entry = new SelectableNetworkEntry(networks.get(index), index);
            networkEntries.add(entry);
            addWidget(entry);
        }
    }

    public void updateEntries(ItemStack itemStack) {
        selectedNetwork = ((AbstractKeycardItem)itemStack.getItem()).getAccessNetwork(itemStack);
        selectedLevel = ((AbstractKeycardItem)itemStack.getItem()).getAccessLevel(itemStack);

        updateNetworkEntries();
        updateLevelEntries();
    }

    public void clearNetworkEntries() {
        for (SelectableNetworkEntry entry : networkEntries) {
            removeWidget(entry);
        }
        networkEntries.clear();
    }

    public void clearLevelEntries() {
        for (SelectableLevelEntry entry : levelEntries) {
            removeWidget(entry);
        }
        levelEntries.clear();
    }

    public void clearEntries() {
        // clear networks and levels
        clearLevelEntries();
        clearNetworkEntries();
    }

    // CONSTANTS

    private static int ENTRY_HEIGHT = 18;
    private static int ENTRY_PADDING = 1;

    private static int SCROLLER_WIDTH = 2;
    private static int SCROLLER_HEIGHT = 18;

    private static int NETWORKS_X = 9;
    private static int NETWORKS_Y = 27;
    private static int NETWORKS_WIDTH = 101;
    private static int NETWORKS_HEIGHT = 93;

    private static int LEVELS_X = 120;
    private static int LEVELS_Y = 27;
    private static int LEVELS_WIDTH = 124;
    private static int LEVELS_HEIGHT = 93;

    private static int BOTH_Y = 27;

    private class SelectableNetworkEntry extends TexturedButton {
        private AccessNetwork network;
        private int index;

        public SelectableNetworkEntry(AccessNetwork network, int index) {
                super(leftPos + NETWORKS_X, topPos + NETWORKS_Y, NETWORKS_WIDTH, ENTRY_HEIGHT, Component.literal(network.getName()), btn -> {
                    selectNetwork(network);
                }
            );

            this.network = network;
            this.index = index;
        }

        @Override
        public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
            this.setY(topPos + NETWORKS_Y + index * (ENTRY_HEIGHT + ENTRY_PADDING) - scrollPosNetworks);
            super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }

        @Override
        public boolean isHoveredOrFocused() {
            return selectedNetwork == this.network || super.isHovered;
        }
    }

    private class SelectableLevelEntry extends TexturedButton {
        private AccessLevel level;
        private int index;

        public SelectableLevelEntry(AccessLevel level, int index) {
            super(leftPos + LEVELS_X, topPos + LEVELS_Y, LEVELS_WIDTH, ENTRY_HEIGHT, Component.literal(level.getName()), btn -> {
                        selectLevel(level);
                    }
            );

            this.level = level;
            this.index = index;
        }

        @Override
        public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
            this.setY(topPos + LEVELS_Y + index * (ENTRY_HEIGHT + ENTRY_PADDING) - scrollPosLevels);
            super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }

        @Override
        public boolean isHoveredOrFocused() {
            return selectedLevel == this.level || super.isHovered;
        }
    }
}
