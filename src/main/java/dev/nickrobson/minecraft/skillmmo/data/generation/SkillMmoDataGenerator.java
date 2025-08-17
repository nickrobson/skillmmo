package dev.nickrobson.minecraft.skillmmo.data.generation;

import dev.nickrobson.minecraft.skillmmo.data.generation.provider.lang.SkillMmoEnglishLanguageProvider;
import dev.nickrobson.minecraft.skillmmo.data.generation.provider.recipe.SkillMmoBundleRecipeProvider;
import dev.nickrobson.minecraft.skillmmo.data.generation.provider.tag.SkillMmoInteractableBlocksTagProvider;
import dev.nickrobson.minecraft.skillmmo.data.generation.provider.tag.SkillMmoSkillLevelBlockTagProvider;
import dev.nickrobson.minecraft.skillmmo.data.generation.provider.tag.SkillMmoSkillLevelEntityTypeTagProvider;
import dev.nickrobson.minecraft.skillmmo.data.generation.provider.tag.SkillMmoSkillLevelItemTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class SkillMmoDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        // lang
        pack.addProvider(SkillMmoEnglishLanguageProvider::new);

        // recipe
        pack.addProvider(SkillMmoBundleRecipeProvider::new);

        // tags
        pack.addProvider(SkillMmoInteractableBlocksTagProvider::new);

        // skills
        pack.addProvider(SkillMmoSkillLevelBlockTagProvider::new);
        pack.addProvider(SkillMmoSkillLevelItemTagProvider::new);
        pack.addProvider(SkillMmoSkillLevelEntityTypeTagProvider::new);
    }
}
