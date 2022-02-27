package dev.nickrobson.minecraft.skillmmo;

import dev.nickrobson.minecraft.skillmmo.skill.SkillLevel;
import dev.nickrobson.minecraft.skillmmo.api.unlockable.UnlockableType;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

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
    public static final Tag<Block> interactableBlocks =
            TagFactory.BLOCK.create(new Identifier(SkillMmoMod.MOD_ID, "interactable"));

    public static <T> Tag<T> getUnlocksTag(SkillLevel skillLevel, UnlockableType<T> unlockableType) {
        return unlockableType.getTagFactory().create(getLevelUnlocksIdentifier(skillLevel));
    }

    private static Identifier getLevelUnlocksIdentifier(SkillLevel skillLevel) {
        Identifier skillIdentifier = skillLevel.getSkill().getId();
        return new Identifier(
                skillIdentifier.getNamespace(),
                "skills/%s/%d".formatted(skillIdentifier.getPath(), skillLevel.getLevel()));
    }
}
