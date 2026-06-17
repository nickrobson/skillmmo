package dev.nickrobson.minecraft.skillmmo.api.interaction;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import dev.nickrobson.minecraft.skillmmo.api.unlockable.VanillaUnlockables;

public class VanillaInteractionTypes {
    public static final InteractionType<Block> BLOCK_BREAK = new InteractionType<>(VanillaUnlockables.BLOCK) {
        @Override
        public Component getDenyText(Block block, Component requiredSkillName, int requiredSkillLevel) {
            return Component.translatable("skillmmo.feedback.deny.block.break", requiredSkillName, requiredSkillLevel, block.getName());
        }
    };

    public static final InteractionType<Block> BLOCK_INTERACT = new InteractionType<>(VanillaUnlockables.BLOCK) {
        @Override
        public Component getDenyText(Block block, Component requiredSkillName, int requiredSkillLevel) {
            return Component.translatable("skillmmo.feedback.deny.block.interact", requiredSkillName, requiredSkillLevel, block.getName());
        }
    };

    public static final InteractionType<Block> BLOCK_PLACE = new InteractionType<>(VanillaUnlockables.BLOCK) {
        @Override
        public Component getDenyText(Block block, Component requiredSkillName, int requiredSkillLevel) {
            return Component.translatable("skillmmo.feedback.deny.block.place", requiredSkillName, requiredSkillLevel, block.getName());
        }
    };

    public static final InteractionType<Item> ITEM_USE = new InteractionType<>(VanillaUnlockables.ITEM) {
        @Override
        public Component getDenyText(Item item, Component requiredSkillName, int requiredSkillLevel) {
            return Component.translatable("skillmmo.feedback.deny.item.use", requiredSkillName, requiredSkillLevel, item.getName());
        }
    };

    public static final InteractionType<EntityType<?>> ENTITY_INTERACT = new InteractionType<>(VanillaUnlockables.ENTITY_TYPE) {
        @Override
        public Component getDenyText(EntityType<?> target, Component requiredSkillName, int requiredSkillLevel) {
            return Component.translatable("skillmmo.feedback.deny.entity.interact", requiredSkillName, requiredSkillLevel, target.getDescription());
        }
    };
}
