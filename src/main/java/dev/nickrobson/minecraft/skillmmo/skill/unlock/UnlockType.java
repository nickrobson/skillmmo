package dev.nickrobson.minecraft.skillmmo.skill.unlock;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public abstract class UnlockType<T> {
    public static final UnlockType<Block> BLOCK = new UnlockType<>(Registry.BLOCK, TagFactory.BLOCK) {
        @Override
        public Text getName(Block block) {
            return block.getName();
        }
    };

    public static final UnlockType<Item> ITEM = new UnlockType<>(Registry.ITEM, TagFactory.ITEM) {
        @Override
        public Text getName(Item item) {
            return item.getName();
        }
    };

    public static final UnlockType<EntityType<?>> ENTITY_TYPE = new UnlockType<>(Registry.ENTITY_TYPE, TagFactory.ENTITY_TYPE) {
        @Override
        public Text getName(EntityType<?> entityType) {
            return entityType.getName();
        }
    };

    private final Registry<T> registry;
    private final TagFactory<T> tagFactory;

    private UnlockType(Registry<T> registry, TagFactory<T> tagFactory) {
        this.registry = registry;
        this.tagFactory = tagFactory;
    }

    public Registry<T> getRegistry() {
        return registry;
    }

    public TagFactory<T> getTagFactory() {
        return tagFactory;
    }

    public Text getName(Identifier identifier) {
        T t = registry.get(identifier);
        return t == null ? null : getName(t);
    }

    abstract Text getName(T t);
}
