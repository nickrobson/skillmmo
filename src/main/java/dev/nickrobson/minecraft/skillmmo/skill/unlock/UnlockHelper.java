package dev.nickrobson.minecraft.skillmmo.skill.unlock;

import dev.nickrobson.minecraft.skillmmo.util.IdentifierHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class UnlockHelper {
    private UnlockHelper() {}

    public static Unlock forBlock(Block block) {
        return new Unlock(UnlockType.BLOCK, IdentifierHelper.forBlock(block));
    }

    public static Unlock forItem(Item item) {
        return new Unlock(UnlockType.ITEM, IdentifierHelper.forItem(item));
    }

    public static Unlock forItemStack(ItemStack itemStack) {
        Item item = itemStack.getItem();
        return item instanceof BlockItem blockItem
                ? forBlock(blockItem.getBlock())
                : forItem(item);
    }

    public static Unlock forEntity(Entity entity) {
        return new Unlock(UnlockType.ENTITY, IdentifierHelper.forEntity(entity));
    }
}
