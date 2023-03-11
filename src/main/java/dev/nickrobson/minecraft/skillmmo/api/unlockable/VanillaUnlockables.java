package dev.nickrobson.minecraft.skillmmo.api.unlockable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

public class VanillaUnlockables {
    public static final UnlockableType<Block> BLOCK = new UnlockableType<>(Registries.BLOCK) {
        @Override
        protected Text getName(Block block) {
            return block.getName();
        }
    };

    public static final UnlockableType<Item> ITEM = new UnlockableType<>(Registries.ITEM) {
        @Override
        protected Text getName(Item item) {
            return item.getName();
        }
    };

    public static final UnlockableType<EntityType<?>> ENTITY_TYPE = new UnlockableType<>(Registries.ENTITY_TYPE) {
        @Override
        protected Text getName(EntityType<?> entityType) {
            return entityType.getName();
        }
    };

    private VanillaUnlockables() {
    }

    public static Unlockable<?> forBlock(BlockState blockState) {
        return forBlock(blockState.getBlock());
    }

    public static Unlockable<?> forBlock(Block block) {
        return BLOCK.createUnlockable(block);
    }

    public static Unlockable<?> forItem(Item item) {
        return item instanceof BlockItem blockItem
                ? forBlock(blockItem.getBlock())
                : ITEM.createUnlockable(item);
    }

    public static Unlockable<?> forItemStack(ItemStack itemStack) {
        return forItem(itemStack.getItem());
    }

    public static Unlockable<?> forEntity(Entity entity) {
        return entity instanceof ItemEntity itemEntity
                ? forItemStack(itemEntity.getStack())
                : ENTITY_TYPE.createUnlockable(entity.getType());
    }
}
