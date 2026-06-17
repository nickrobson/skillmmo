package dev.nickrobson.minecraft.skillmmo.api.unlockable;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class VanillaUnlockables {
    public static final UnlockableType<Block> BLOCK = new UnlockableType<>(BuiltInRegistries.BLOCK) {
        @Override
        protected Component getName(Block block) {
            return block.getName();
        }
    };

    public static final UnlockableType<Item> ITEM = new UnlockableType<>(BuiltInRegistries.ITEM) {
        @Override
        protected Component getName(Item item) {
            return item.getDefaultInstance().getItemName();
        }
    };

    public static final UnlockableType<EntityType<?>> ENTITY_TYPE = new UnlockableType<>(BuiltInRegistries.ENTITY_TYPE) {
        @Override
        protected Component getName(EntityType<?> entityType) {
            return entityType.getDescription();
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
                ? forItemStack(itemEntity.getItem())
                : ENTITY_TYPE.createUnlockable(entity.getType());
    }

    public static Unlockable<?> forEntityType(EntityType<?> entityType) {
        return ENTITY_TYPE.createUnlockable(entityType);
    }
}
