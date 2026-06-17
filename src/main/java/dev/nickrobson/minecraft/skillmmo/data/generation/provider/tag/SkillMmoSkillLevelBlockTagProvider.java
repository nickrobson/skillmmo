package dev.nickrobson.minecraft.skillmmo.data.generation.provider.tag;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.world.level.block.Block;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import dev.nickrobson.minecraft.skillmmo.SkillMmoTags;
import dev.nickrobson.minecraft.skillmmo.api.unlockable.VanillaUnlockables;
import dev.nickrobson.minecraft.skillmmo.data.generation.spec.SkillMmoDefaultSkills;

public class SkillMmoSkillLevelBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {
    public SkillMmoSkillLevelBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    public String getName() {
        return "Default skill level block tags";
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        SkillMmoDefaultSkills.DEFAULT_SKILLS.forEach(skillSpec -> {
            skillSpec.levels().forEach((level, levelSpec) -> {
                if (levelSpec.hasBlocks()) {
                    TagAppender<Block, Block> tagBuilder = this.valueLookupBuilder(SkillMmoTags.getUnlocksTag(skillSpec.id(), level, VanillaUnlockables.BLOCK)).setReplace(true);
                    levelSpec.blocks().forEach(tagBuilder::add);
                    levelSpec.blockTags().forEach(tagBuilder::forceAddTag);
                }
            });
        });
    }
}
