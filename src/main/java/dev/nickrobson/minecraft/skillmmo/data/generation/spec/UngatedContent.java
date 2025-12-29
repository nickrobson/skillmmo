package dev.nickrobson.minecraft.skillmmo.data.generation.spec;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.List;
import java.util.stream.Stream;

/**
 * Documentation of content that is intentionally NOT gated behind skill levels.
 * <p>
 * This class serves as explicit documentation that certain blocks and items have been
 * considered and deliberately excluded from skill requirements, rather than accidentally
 * forgotten during updates.
 * <p>
 * Generally, content is left ungated if it:
 * <ul>
 *   <li>Is purely decorative and found naturally in the world</li>
 *   <li>Provides no significant gameplay advantage</li>
 *   <li>Is part of the natural environment that players should be able to interact with freely</li>
 * </ul>
 */
public final class UngatedContent {
    private UngatedContent() {
    }

    /**
     * Decorative plant blocks added in 1.21.5 (Spring to Life update).
     * <p>
     * These are all naturally-generating decorative blocks that don't provide
     * any significant gameplay advantage. Players should be able to collect and
     * place these freely as part of world decoration.
     */
    public static final List<Block> SPRING_TO_LIFE_DECORATIVE_BLOCKS = List.of(
            Blocks.LEAF_LITTER,        // Decorative block found in forests, can be smelted from leaves
            Blocks.WILDFLOWERS,         // Flower found in birch forests and meadows
            Blocks.BUSH,                // Decorative plant found in various biomes
            Blocks.FIREFLY_BUSH,        // Decorative plant found in swamps with particle effects
            Blocks.CACTUS_FLOWER,       // Flower that grows on cacti in deserts
            Blocks.SHORT_DRY_GRASS,     // Grass variant for desert/badlands biomes
            Blocks.TALL_DRY_GRASS       // Taller grass variant for desert/badlands biomes
    );

    /**
     * Technical/debug blocks that should never be gated.
     * <p>
     * These blocks are not part of normal gameplay and are only used for
     * development, testing, and debugging purposes.
     */
    public static final List<Block> TECHNICAL_BLOCKS = List.of(
            Blocks.TEST_BLOCK,          // Technical block for game tests (added in 1.21.5)
            Blocks.TEST_INSTANCE_BLOCK  // Technical block for game tests (added in 1.21.5)
    );

    /**
     * Other naturally-generating decorative blocks that are not gated.
     * <p>
     * These blocks are part of the natural world generation and provide
     * primarily aesthetic value without significant gameplay impact.
     */
    public static final List<Block> OTHER_DECORATIVE_BLOCKS = List.of(
            Blocks.GRASS_BLOCK,         // Basic world generation block
            Blocks.DIRT,                // Basic world generation block
            Blocks.SAND,                // Basic world generation block
            Blocks.RED_SAND,            // Basic world generation block
            Blocks.GRAVEL,              // Basic world generation block
            Blocks.SNOW,                // Weather effect
            Blocks.SNOW_BLOCK,          // Weather/biome decoration
            Blocks.ICE,                 // Biome decoration
            Blocks.PACKED_ICE,          // Biome decoration
            Blocks.BLUE_ICE,            // Biome decoration
            Blocks.SHORT_GRASS,         // Natural ground cover
            Blocks.TALL_GRASS,          // Natural ground cover
            Blocks.FERN,                // Natural ground cover
            Blocks.LARGE_FERN,          // Natural ground cover
            Blocks.DEAD_BUSH,           // Desert decoration
            Blocks.SEAGRASS,            // Ocean decoration
            Blocks.TALL_SEAGRASS,       // Ocean decoration
            Blocks.VINE,                // Jungle decoration
            Blocks.GLOW_LICHEN,         // Cave decoration
            Blocks.LILY_PAD,            // Swamp decoration
            Blocks.SUGAR_CANE,          // River/ocean shore decoration
            Blocks.SEA_PICKLE,          // Ocean decoration
            Blocks.PINK_PETALS,         // Cherry grove decoration
            Blocks.PALE_MOSS_CARPET,    // Pale garden decoration
            Blocks.PALE_MOSS_BLOCK      // Pale garden decoration
    );

    /**
     * Flowers and small plants that are not gated.
     * <p>
     * These provide no significant gameplay advantage beyond basic aesthetics
     * and brewing ingredients (which are gated separately through the items).
     */
    public static final List<Block> FLOWERS = List.of(
            Blocks.DANDELION,
            Blocks.POPPY,
            Blocks.BLUE_ORCHID,
            Blocks.ALLIUM,
            Blocks.AZURE_BLUET,
            Blocks.RED_TULIP,
            Blocks.ORANGE_TULIP,
            Blocks.WHITE_TULIP,
            Blocks.PINK_TULIP,
            Blocks.OXEYE_DAISY,
            Blocks.CORNFLOWER,
            Blocks.LILY_OF_THE_VALLEY,
            Blocks.SUNFLOWER,
            Blocks.LILAC,
            Blocks.ROSE_BUSH,
            Blocks.PEONY,
            Blocks.TORCHFLOWER,
            Blocks.PITCHER_PLANT
    );

    /**
     * Mushrooms and fungal blocks that are not gated.
     * <p>
     * Basic mushroom blocks are left ungated as they're common food sources
     * and natural decorations.
     */
    public static final List<Block> MUSHROOMS = List.of(
            Blocks.BROWN_MUSHROOM,
            Blocks.RED_MUSHROOM,
            Blocks.CRIMSON_FUNGUS,
            Blocks.WARPED_FUNGUS,
            Blocks.CRIMSON_ROOTS,
            Blocks.WARPED_ROOTS,
            Blocks.NETHER_SPROUTS
    );

    /**
     * Items that are obtained from ungated blocks.
     * <p>
     * These items are the drop/pickup form of ungated blocks and should
     * also remain ungated for consistency.
     */
    public static final List<Item> UNGATED_ITEMS_FROM_BLOCKS = List.of(
            // Note: Most of these are automatically handled by the block unlocks,
            // but this list documents items that should remain ungated even if
            // their block form is different or unavailable
            Items.SNOWBALL,             // Thrown item from snow
            Items.WHEAT_SEEDS,          // Basic seeds from grass (though wheat farming is gated)
            Items.STICK                 // Basic crafting component from any wood
    );

    /**
     * Returns all ungated blocks as a single combined list.
     * Useful for validation or documentation purposes.
     */
    public static List<Block> getAllUngatedBlocks() {
        return Stream.of(
                        SPRING_TO_LIFE_DECORATIVE_BLOCKS,
                        TECHNICAL_BLOCKS,
                        OTHER_DECORATIVE_BLOCKS,
                        FLOWERS,
                        MUSHROOMS
                )
                .flatMap(List::stream)
                .toList();
    }
}
