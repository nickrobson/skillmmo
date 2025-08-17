package dev.nickrobson.minecraft.skillmmo.data.generation.provider.recipe;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class SkillMmoBundleRecipeProvider extends FabricRecipeProvider {
    public SkillMmoBundleRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public String getName() {
        return "Bundle recipe";
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, Items.BUNDLE)
                .pattern("shs")
                .pattern("h h")
                .pattern("hhh")
                .input('s', Items.STRING)
                .input('h', Items.RABBIT_HIDE)
                .criterion(FabricRecipeProvider.hasItem(Items.STRING),
                        FabricRecipeProvider.conditionsFromItem(Items.STRING))
                .criterion(FabricRecipeProvider.hasItem(Items.RABBIT_HIDE),
                        FabricRecipeProvider.conditionsFromItem(Items.RABBIT_HIDE))
                .offerTo(exporter);
    }
}
