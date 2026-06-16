package dev.nickrobson.minecraft.skillmmo;

import dev.nickrobson.minecraft.skillmmo.api.unlockable.UnlockableType;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class SkillMmoTags {
    private SkillMmoTags() {
    }

    /**
     * This tag contains blocks that should be blocked from being clicked on
     * by another block.
     * That is, a block should be in this tag if a player can interact with it
     * in some way while holding another block.
     * This includes any form of right-click interaction like opening an inventory or GUI, sleeping in a bed, etc.
     */
    public static final TagKey<Block> interactableBlocks =
            TagKey.create(BuiltInRegistries.BLOCK.key(), Identifier.fromNamespaceAndPath(SkillMmoMod.MOD_ID, "interactable"));

    public static <T> TagKey<T> getUnlocksTag(SkillLevel skillLevel, UnlockableType<T> unlockableType) {
        return getUnlocksTag(skillLevel.getSkill().getId(), skillLevel.getLevel(), unlockableType);
    }

    public static <T> TagKey<T> getUnlocksTag(Identifier skillId, int skillLevel, UnlockableType<T> unlockableType) {
        return unlockableType.createTag(getLevelUnlocksIdentifier(skillId, skillLevel));
    }

    private static Identifier getLevelUnlocksIdentifier(Identifier skillId, int skillLevel) {
        return skillId.withPath("skills/%s/%d".formatted(skillId.getPath(), skillLevel));
    }
}
