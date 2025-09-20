package net.teekay.axess.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.teekay.axess.Axess;
import net.teekay.axess.registry.AxessBlockRegistry;

public class AxessBlockStateProvider extends BlockStateProvider {
    public AxessBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Axess.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // KEYCARD READERS
        keycardReader(AxessBlockRegistry.IRON_KEYCARD_READER.get(), "iron_keycard_reader");

        // NETWORK MANAGER
        String networkManagerID = "network_manager";
        ModelFile networkManagerModel = getBlockModel(networkManagerID);
        horizontalBlock(AxessBlockRegistry.NETWORK_MANAGER.get(), networkManagerModel);
        itemModels().getBuilder(networkManagerID).parent(networkManagerModel);

        // KEYCARD EDITOR
        String keycardEditorID = "keycard_editor";
        ModelFile keycardEditorModel = getBlockModel(keycardEditorID);
        horizontalBlock(AxessBlockRegistry.KEYCARD_EDITOR.get(), keycardEditorModel);
        itemModels().getBuilder(keycardEditorID).parent(keycardEditorModel);
    }

    private ModelFile getBlockModel(String id) {
        ModelFile m = models().getExistingFile(modLoc("block/" + id));
        return m;
    }

    private void keycardReader(Block reader, String id) {
        ModelFile readerModel = models().getExistingFile(modLoc("block/" + id));
        horizontalFaceBlock(reader, readerModel);
        itemModels().getBuilder(id).parent(readerModel);
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
