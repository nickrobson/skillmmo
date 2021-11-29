package dev.nickrobson.minecraft.skillmmo.util;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class IdentifierHelper {
    private IdentifierHelper() {
    }

    public static Identifier forBlock(Block block) {
        return Registry.BLOCK.getId(block);
    }

    public static Identifier forItem(Item item) {
        return Registry.ITEM.getId(item);
    }

    public static Identifier forItemStack(ItemStack itemStack) {
        return itemStack.getItem() instanceof BlockItem blockItem
                ? forBlock(blockItem.getBlock())
                : forItem(itemStack.getItem());
    }

    public static Identifier forEntity(Entity entity) {
        return Registry.ENTITY_TYPE.getId(entity.getType());
    }
}
