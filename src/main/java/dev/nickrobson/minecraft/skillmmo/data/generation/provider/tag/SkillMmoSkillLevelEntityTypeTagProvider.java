package dev.nickrobson.minecraft.skillmmo.data.generation.provider.tag;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.world.entity.EntityType;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import dev.nickrobson.minecraft.skillmmo.SkillMmoTags;
import dev.nickrobson.minecraft.skillmmo.api.unlockable.VanillaUnlockables;
import dev.nickrobson.minecraft.skillmmo.data.generation.spec.SkillMmoDefaultSkills;

public class SkillMmoSkillLevelEntityTypeTagProvider extends FabricTagsProvider.EntityTypeTagsProvider {
    public SkillMmoSkillLevelEntityTypeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    public String getName() {
        return "Default skill level entity type tags";
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        SkillMmoDefaultSkills.DEFAULT_SKILLS.forEach(skillSpec -> {
            skillSpec.levels().forEach((level, levelSpec) -> {
                if (levelSpec.hasEntityTypes()) {
                    TagAppender<EntityType<?>, EntityType<?>> tagBuilder = this.valueLookupBuilder(SkillMmoTags.getUnlocksTag(skillSpec.id(), level, VanillaUnlockables.ENTITY_TYPE)).setReplace(true);
                    levelSpec.entityTypes().forEach(tagBuilder::add);
                    levelSpec.entityTypeTags().forEach(tagBuilder::forceAddTag);
                }
            });
        });
    }
}
