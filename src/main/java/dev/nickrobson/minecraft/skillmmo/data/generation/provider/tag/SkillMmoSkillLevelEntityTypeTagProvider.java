package dev.nickrobson.minecraft.skillmmo.data.generation.provider.tag;

import dev.nickrobson.minecraft.skillmmo.SkillMmoTags;
import dev.nickrobson.minecraft.skillmmo.api.unlockable.VanillaUnlockables;
import dev.nickrobson.minecraft.skillmmo.data.generation.spec.SkillMmoDefaultSkills;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class SkillMmoSkillLevelEntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
    public SkillMmoSkillLevelEntityTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    public String getName() {
        return "SkillMMO skill level entity type tags";
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        SkillMmoDefaultSkills.DEFAULT_SKILLS.forEach(skillSpec -> {
            skillSpec.levels().forEach((level, levelSpec) -> {
                if (levelSpec.hasEntityTypes()) {
                    FabricTagBuilder tagBuilder = this.getOrCreateTagBuilder(SkillMmoTags.getUnlocksTag(skillSpec.id(), level, VanillaUnlockables.ENTITY_TYPE)).setReplace(true);
                    levelSpec.entityTypes().forEach(tagBuilder::add);
                    levelSpec.entityTypeTags().forEach(tagBuilder::forceAddTag);
                }
            });
        });
    }
}
