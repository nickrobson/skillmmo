package dev.nickrobson.minecraft.skillmmo;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class SkillMmoTags {
    /**
     * This tag contains blocks that should be blocked from being clicked on
     * by another block.
     * That is, a block should be in this tag if a player can interact with it
     * in some way while holding another block. (Such as opening an inventory or GUI.)
     */
    public static final Tag<Block> interactionRestrictedBlocks =
            TagFactory.BLOCK.create(new Identifier(SkillMmoMod.MOD_ID, "interaction_restricted"));
}
