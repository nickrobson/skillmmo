package dev.nickrobson.minecraft.skillmmo.data.generation;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.JsonKeySortOrderCallback;

import dev.nickrobson.minecraft.skillmmo.data.generation.provider.lang.SkillMmoEnglishLanguageProvider;
import dev.nickrobson.minecraft.skillmmo.data.generation.provider.skill.SkillMmoDefaultSkillProvider;
import dev.nickrobson.minecraft.skillmmo.data.generation.provider.tag.SkillMmoInteractableBlocksTagProvider;
import dev.nickrobson.minecraft.skillmmo.data.generation.provider.tag.SkillMmoSkillLevelBlockTagProvider;
import dev.nickrobson.minecraft.skillmmo.data.generation.provider.tag.SkillMmoSkillLevelEntityTypeTagProvider;
import dev.nickrobson.minecraft.skillmmo.data.generation.provider.tag.SkillMmoSkillLevelItemTagProvider;

public class SkillMmoDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        // lang
        pack.addProvider(SkillMmoEnglishLanguageProvider::new);

        // tags
        pack.addProvider(SkillMmoInteractableBlocksTagProvider::new);

        // skills
        pack.addProvider(SkillMmoDefaultSkillProvider::new);
        pack.addProvider(SkillMmoSkillLevelBlockTagProvider::new);
        pack.addProvider(SkillMmoSkillLevelItemTagProvider::new);
        pack.addProvider(SkillMmoSkillLevelEntityTypeTagProvider::new);
    }

    @Override
    public void addJsonKeySortOrders(JsonKeySortOrderCallback sortOrder) {
        sortOrder.add("replace", 0);
        sortOrder.add("enabled", 1);
        sortOrder.add("nameKey", 2);
        sortOrder.add("descriptionKey", 3);
        sortOrder.add("maxLevel", 4);
        sortOrder.add("icon", 5);
    }
}
