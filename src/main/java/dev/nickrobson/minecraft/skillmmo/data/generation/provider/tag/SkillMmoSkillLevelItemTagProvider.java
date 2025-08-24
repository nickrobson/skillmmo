package dev.nickrobson.minecraft.skillmmo.data.generation.provider.tag;

import dev.nickrobson.minecraft.skillmmo.SkillMmoTags;
import dev.nickrobson.minecraft.skillmmo.api.unlockable.VanillaUnlockables;
import dev.nickrobson.minecraft.skillmmo.data.generation.spec.SkillMmoDefaultSkills;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class SkillMmoSkillLevelItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public SkillMmoSkillLevelItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    public String getName() {
        return "Default skill level item tags";
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        SkillMmoDefaultSkills.DEFAULT_SKILLS.forEach(skillSpec -> {
            skillSpec.levels().forEach((level, levelSpec) -> {
                if (levelSpec.hasItems()) {
                    FabricTagBuilder tagBuilder = this.getOrCreateTagBuilder(SkillMmoTags.getUnlocksTag(skillSpec.id(), level, VanillaUnlockables.ITEM)).setReplace(true);
                    levelSpec.items().forEach(tagBuilder::add);
                    levelSpec.itemTags().forEach(tagBuilder::forceAddTag);
                }
            });
        });
    }
}
