package dev.nickrobson.minecraft.skillmmo.data.generation.spec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopperBlocks;

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
        return new SkillLevelDataGenSpec(Stream.concat(this.blocks.stream(), Arrays.stream(blocks)).toList(), this.blockTags, this.items, this.itemTags, this.entityTypes, this.entityTypeTags);
    }

    public SkillLevelDataGenSpec withBlockSets(WeatheringCopperBlocks... blockSets) {
        List<Block> newBlocks = new ArrayList<>(this.blocks);
        for (WeatheringCopperBlocks blockSet : blockSets) {
            newBlocks.addAll(blockSet.asList());
        }
        return new SkillLevelDataGenSpec(newBlocks, this.blockTags, this.items, this.itemTags, this.entityTypes, this.entityTypeTags);
    }

    @SafeVarargs
    public final SkillLevelDataGenSpec withBlockTags(TagKey<Block>... blockTags) {
        return new SkillLevelDataGenSpec(this.blocks, Stream.concat(this.blockTags.stream(), Arrays.stream(blockTags)).toList(), this.items, this.itemTags, this.entityTypes, this.entityTypeTags);
    }

    public SkillLevelDataGenSpec withItems(Item... items) {
        return new SkillLevelDataGenSpec(this.blocks, this.blockTags, Stream.concat(this.items.stream(), Arrays.stream(items)).toList(), this.itemTags, this.entityTypes, this.entityTypeTags);
    }

    @SafeVarargs
    public final SkillLevelDataGenSpec withItemTags(TagKey<Item>... itemTags) {
        return new SkillLevelDataGenSpec(this.blocks, this.blockTags, this.items, Stream.concat(this.itemTags.stream(), Arrays.stream(itemTags)).toList(), this.entityTypes, this.entityTypeTags);
    }

    public SkillLevelDataGenSpec withEntityTypes(EntityType<?>... entityTypes) {
        return new SkillLevelDataGenSpec(this.blocks, this.blockTags, this.items, this.itemTags, Stream.concat(this.entityTypes.stream(), Arrays.stream(entityTypes)).toList(), this.entityTypeTags);
    }

    @SafeVarargs
    public final SkillLevelDataGenSpec withEntityTypeTags(TagKey<EntityType<?>>... entityTypeTags) {
        return new SkillLevelDataGenSpec(this.blocks, this.blockTags, this.items, this.itemTags, this.entityTypes, Stream.concat(this.entityTypeTags.stream(), Arrays.stream(entityTypeTags)).toList());
    }
}
