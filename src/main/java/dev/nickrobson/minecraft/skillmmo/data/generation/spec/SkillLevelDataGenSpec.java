package dev.nickrobson.minecraft.skillmmo.data.generation.spec;

import net.minecraft.block.Block;
import net.minecraft.block.CopperBlockSet;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;

import java.util.ArrayList;
import java.util.List;

public record SkillLevelDataGenSpec(
        List<Block> blocks,
        List<TagKey<Block>> blockTags,
        List<Item> items,
        List<TagKey<Item>> itemTags,
        List<EntityType<?>> entityTypes,
        List<TagKey<EntityType<?>>> entityTypeTags
) {
    public SkillLevelDataGenSpec() {
        this(List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
    }

    public boolean hasBlocks() {
        return !blocks.isEmpty() || !blockTags.isEmpty();
    }

    public boolean hasItems() {
        return !items.isEmpty() || !itemTags.isEmpty();
    }

    public boolean hasEntityTypes() {
        return !entityTypes.isEmpty() || !entityTypeTags.isEmpty();
    }

    public SkillLevelDataGenSpec withBlocks(Block... blocks) {
        return new SkillLevelDataGenSpec(List.of(blocks), this.blockTags, this.items, this.itemTags, this.entityTypes, this.entityTypeTags);
    }

    public SkillLevelDataGenSpec withBlockSets(CopperBlockSet... blockSets) {
        List<Block> newBlocks = new ArrayList<>(this.blocks);
        for (CopperBlockSet blockSet : blockSets) {
            newBlocks.addAll(blockSet.getAll());
        }
        return new SkillLevelDataGenSpec(newBlocks, this.blockTags, this.items, this.itemTags, this.entityTypes, this.entityTypeTags);
    }

    @SafeVarargs
    public final SkillLevelDataGenSpec withBlockTags(TagKey<Block>... blockTags) {
        return new SkillLevelDataGenSpec(this.blocks, List.of(blockTags), this.items, this.itemTags, this.entityTypes, this.entityTypeTags);
    }

    public SkillLevelDataGenSpec withItems(Item... items) {
        return new SkillLevelDataGenSpec(this.blocks, this.blockTags, List.of(items), this.itemTags, this.entityTypes, this.entityTypeTags);
    }

    @SafeVarargs
    public final SkillLevelDataGenSpec withItemTags(TagKey<Item>... itemTags) {
        return new SkillLevelDataGenSpec(this.blocks, this.blockTags, this.items, List.of(itemTags), this.entityTypes, this.entityTypeTags);
    }

    public SkillLevelDataGenSpec withEntityTypes(EntityType<?>... entityTypes) {
        return new SkillLevelDataGenSpec(this.blocks, this.blockTags, this.items, this.itemTags, List.of(entityTypes), this.entityTypeTags);
    }

    @SafeVarargs
    public final SkillLevelDataGenSpec withEntityTypeTags(TagKey<EntityType<?>>... entityTypeTags) {
        return new SkillLevelDataGenSpec(this.blocks, this.blockTags, this.items, this.itemTags, this.entityTypes, List.of(entityTypeTags));
    }
}
