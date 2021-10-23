package dev.nickrobson.minecraft.skillmmo.interaction;

import dev.nickrobson.minecraft.skillmmo.util.IdentifierHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;

public class InteractionHelper {
    private InteractionHelper() {}

    public static Interaction forBlock(Block block, InteractionType interactionType) {
        return new Interaction(interactionType, IdentifierHelper.forBlock(block));
    }

    public static Interaction forItem(Item item, InteractionType interactionType) {
        return new Interaction(interactionType, IdentifierHelper.forItem(item));
    }

    public static Interaction forEntity(Entity entity, InteractionType interactionType) {
        return new Interaction(interactionType, IdentifierHelper.forEntity(entity));
    }
}
