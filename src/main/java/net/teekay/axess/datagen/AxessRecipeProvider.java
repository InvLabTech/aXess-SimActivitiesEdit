package net.teekay.axess.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.teekay.axess.registry.AxessBlockRegistry;

import java.util.function.Consumer;

public class AxessRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public AxessRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, AxessBlockRegistry.IRON_KEYCARD_READER.get())
                .pattern("SSS")
                .pattern("S S")
                .pattern("SSS")
                .define('S', Items.IRON_INGOT)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(pWriter);

    }
}
