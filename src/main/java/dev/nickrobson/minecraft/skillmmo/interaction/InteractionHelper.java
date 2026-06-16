package dev.nickrobson.minecraft.skillmmo.interaction;

import dev.nickrobson.minecraft.skillmmo.api.interaction.Interaction;
import dev.nickrobson.minecraft.skillmmo.api.interaction.InteractionType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

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
