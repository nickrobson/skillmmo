package dev.nickrobson.minecraft.skillmmo.api.interaction;

import dev.nickrobson.minecraft.skillmmo.api.unlockable.VanillaUnlockables;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.text.Text;

public class VanillaInteractionTypes {
    public static InteractionType<Block> BLOCK_BREAK = new InteractionType<>(VanillaUnlockables.BLOCK) {
        @Override
        public Text getDenyText(Block block, Text requiredSkillName, int requiredSkillLevel) {
            return Text.translatable("skillmmo.feedback.deny.block.break", requiredSkillName, requiredSkillLevel, block.getName());
        }
    };

    public static InteractionType<Block> BLOCK_INTERACT = new InteractionType<>(VanillaUnlockables.BLOCK) {
        @Override
        public Text getDenyText(Block block, Text requiredSkillName, int requiredSkillLevel) {
            return Text.translatable("skillmmo.feedback.deny.block.interact", requiredSkillName, requiredSkillLevel, block.getName());
        }
    };

    public static InteractionType<Block> BLOCK_PLACE = new InteractionType<>(VanillaUnlockables.BLOCK) {
        @Override
        public Text getDenyText(Block block, Text requiredSkillName, int requiredSkillLevel) {
            return Text.translatable("skillmmo.feedback.deny.block.place", requiredSkillName, requiredSkillLevel, block.getName());
        }
    };

    public static InteractionType<Item> ITEM_USE = new InteractionType<>(VanillaUnlockables.ITEM) {
        @Override
        public Text getDenyText(Item item, Text requiredSkillName, int requiredSkillLevel) {
            return Text.translatable("skillmmo.feedback.deny.item.use", requiredSkillName, requiredSkillLevel, item.getName());
        }
    };

    public static InteractionType<EntityType<?>> ENTITY_INTERACT = new InteractionType<>(VanillaUnlockables.ENTITY_TYPE) {
        @Override
        public Text getDenyText(EntityType<?> target, Text requiredSkillName, int requiredSkillLevel) {
            return Text.translatable("skillmmo.feedback.deny.entity.interact", requiredSkillName, requiredSkillLevel, target.getName());
        }
    };
}
