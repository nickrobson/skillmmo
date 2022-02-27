package dev.nickrobson.minecraft.skillmmo.interaction;

import dev.nickrobson.minecraft.skillmmo.api.interaction.Interaction;
import dev.nickrobson.minecraft.skillmmo.api.interaction.InteractionType;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;

public class InteractionHelper {
    private InteractionHelper() {
    }

    public static Interaction<Block> forBlock(Block block, InteractionType<Block> interactionType) {
        return interactionType.createInteraction(block);
    }

    public static Interaction<Item> forItem(Item item, InteractionType<Item> interactionType) {
        return interactionType.createInteraction(item);
    }

    public static Interaction<EntityType<?>> forEntity(Entity entity, InteractionType<EntityType<?>> interactionType) {
        return interactionType.createInteraction(entity.getType());
    }
}
