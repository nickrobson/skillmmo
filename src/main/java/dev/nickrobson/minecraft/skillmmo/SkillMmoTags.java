package dev.nickrobson.minecraft.skillmmo;

import dev.nickrobson.minecraft.skillmmo.api.unlockable.UnlockableType;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevel;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

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
            TagKey.of(Registry.BLOCK_KEY, new Identifier(SkillMmoMod.MOD_ID, "interactable"));

    public static <T> TagKey<T> getUnlocksTag(SkillLevel skillLevel, UnlockableType<T> unlockableType) {
        return unlockableType.createTag(getLevelUnlocksIdentifier(skillLevel));
    }

    private static Identifier getLevelUnlocksIdentifier(SkillLevel skillLevel) {
        Identifier skillIdentifier = skillLevel.getSkill().getId();
        return new Identifier(
                skillIdentifier.getNamespace(),
                "skills/%s/%d".formatted(skillIdentifier.getPath(), skillLevel.getLevel()));
    }
}
