package dev.nickrobson.minecraft.skillmmo.data.generation.provider.tag;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import dev.nickrobson.minecraft.skillmmo.SkillMmoTags;

public class SkillMmoInteractableBlocksTagProvider extends FabricTagsProvider.BlockTagsProvider {
    public SkillMmoInteractableBlocksTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public String getName() {
        return "Interactable blocks tag";
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        valueLookupBuilder(SkillMmoTags.interactableBlocks)
                .setReplace(false)
                .add(
                        Blocks.BARREL,
                        Blocks.CHEST,
                        Blocks.ENDER_CHEST,
                        Blocks.TRAPPED_CHEST,
                        Blocks.BLAST_FURNACE,
                        Blocks.FURNACE,
                        Blocks.SMOKER,
                        Blocks.CRAFTING_TABLE,
                        Blocks.BEACON,
                        Blocks.BREWING_STAND,
                        Blocks.ENCHANTING_TABLE,
                        Blocks.DRAGON_EGG,
                        Blocks.END_PORTAL_FRAME,
                        Blocks.LODESTONE,
                        Blocks.RESPAWN_ANCHOR,
                        Blocks.COMPARATOR,
                        Blocks.DAYLIGHT_DETECTOR,
                        Blocks.DISPENSER,
                        Blocks.DROPPER,
                        Blocks.JUKEBOX,
                        Blocks.LEVER,
                        Blocks.REPEATER,
                        Blocks.CAKE,
                        Blocks.SWEET_BERRY_BUSH,
                        Blocks.NOTE_BLOCK,
                        Blocks.SCAFFOLDING,
                        Blocks.BEEHIVE,
                        Blocks.BEE_NEST,
                        Blocks.BELL,
                        Blocks.CARTOGRAPHY_TABLE,
                        Blocks.COMPOSTER,
                        Blocks.FLETCHING_TABLE,
                        Blocks.GRINDSTONE,
                        Blocks.LECTERN,
                        Blocks.LOOM,
                        Blocks.SMITHING_TABLE,
                        Blocks.STONECUTTER)
                .forceAddTag(BlockTags.SHULKER_BOXES)
                .forceAddTag(BlockTags.CAULDRONS)
                .forceAddTag(BlockTags.BUTTONS)
                .forceAddTag(BlockTags.FENCE_GATES)
                .forceAddTag(BlockTags.WOODEN_DOORS)
                .forceAddTag(BlockTags.WOODEN_TRAPDOORS)
                .forceAddTag(BlockTags.CAMPFIRES)
                .forceAddTag(BlockTags.CANDLES)
                .forceAddTag(BlockTags.CANDLE_CAKES)
                .forceAddTag(BlockTags.BEDS)
                .forceAddTag(BlockTags.FLOWER_POTS);
    }
}
