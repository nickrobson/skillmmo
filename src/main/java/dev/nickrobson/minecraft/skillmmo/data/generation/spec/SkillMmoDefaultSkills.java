package dev.nickrobson.minecraft.skillmmo.data.generation.spec;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

import java.util.List;

/**
 * Defines the default skills and their unlock requirements.
 * <p>
 * For blocks and items that are intentionally NOT gated behind skill levels,
 * see {@link SkillMmoDefaultUngatedContent} for documentation of what has been deliberately excluded.
 */
public final class SkillMmoDefaultSkills {

    public static final List<SkillDataGenSpec> DEFAULT_SKILLS = List.of(
            // AGRICULTURE
            new SkillDataGenSpec(
                    Identifier.of("skillmmo", "agriculture"),
                    "skillmmo.skill.agriculture.name",
                    "skillmmo.skill.agriculture.description",
                    Items.WHEAT,
                    levelsBuilder()
                            .put(
                                    1,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.OAK_SAPLING,
                                                    Blocks.SPRUCE_SAPLING,
                                                    Blocks.BIRCH_SAPLING,
                                                    Blocks.JUNGLE_SAPLING,
                                                    Blocks.ACACIA_SAPLING,
                                                    Blocks.DARK_OAK_SAPLING,
                                                    Blocks.CHERRY_SAPLING,
                                                    Blocks.PALE_OAK_SAPLING,
                                                    Blocks.MANGROVE_PROPAGULE
                                            )
                                            .withItems(Items.WOODEN_AXE)
                            )
                            .put(
                                    2,
                                    new SkillLevelDataGenSpec().withBlocks(
                                            Blocks.OAK_LEAVES,
                                            Blocks.SPRUCE_LEAVES,
                                            Blocks.BIRCH_LEAVES,
                                            Blocks.JUNGLE_LEAVES,
                                            Blocks.ACACIA_LEAVES,
                                            Blocks.DARK_OAK_LEAVES,
                                            Blocks.CHERRY_LEAVES,
                                            Blocks.PALE_OAK_LEAVES,
                                            Blocks.MANGROVE_LEAVES,
                                            Blocks.MANGROVE_ROOTS,
                                            Blocks.MUDDY_MANGROVE_ROOTS,
                                            Blocks.PACKED_MUD,
                                            Blocks.AZALEA_LEAVES,
                                            Blocks.FLOWERING_AZALEA_LEAVES
                                    )
                            )
                            .put(
                                    3,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.WHEAT,
                                                    Blocks.HAY_BLOCK,
                                                    Blocks.CARROTS,
                                                    Blocks.POTATOES,
                                                    Blocks.BEETROOTS,
                                                    Blocks.SWEET_BERRY_BUSH,
                                                    Blocks.COMPOSTER,
                                                    Blocks.BONE_BLOCK
                                            )
                                            .withItems(
                                                    Items.WOODEN_HOE,
                                                    Items.BONE_MEAL,
                                                    Items.WHEAT,
                                                    Items.WHEAT_SEEDS,
                                                    Items.BREAD,
                                                    Items.CARROT,
                                                    Items.POTATO,
                                                    Items.BAKED_POTATO,
                                                    Items.BEETROOT,
                                                    Items.BEETROOT_SEEDS,
                                                    Items.BEETROOT_SOUP,
                                                    Items.SWEET_BERRIES,
                                                    Items.GLOW_BERRIES
                                            )
                            )
                            .put(
                                    4,
                                    new SkillLevelDataGenSpec()
                                            .withItems(Items.STONE_AXE)
                            )
                            .put(
                                    5,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.CAKE,
                                                    Blocks.COCOA,
                                                    Blocks.KELP,
                                                    Blocks.KELP_PLANT,
                                                    Blocks.DRIED_KELP_BLOCK
                                            )
                                            .withBlockTags(BlockTags.CANDLE_CAKES)
                                            .withItems(
                                                    Items.SUGAR,
                                                    Items.PUMPKIN_PIE,
                                                    Items.COOKIE,
                                                    Items.PAPER,
                                                    Items.BOOK,
                                                    Items.WRITABLE_BOOK,
                                                    Items.WRITTEN_BOOK,
                                                    Items.DRIED_KELP
                                            )
                            )
                            .put(
                                    6,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.MELON,
                                                    Blocks.MELON_STEM,
                                                    Blocks.ATTACHED_MELON_STEM,
                                                    Blocks.PUMPKIN,
                                                    Blocks.PUMPKIN_STEM,
                                                    Blocks.ATTACHED_PUMPKIN_STEM,
                                                    Blocks.CARVED_PUMPKIN,
                                                    Blocks.JACK_O_LANTERN
                                            )
                                            .withItems(
                                                    Items.MELON_SEEDS,
                                                    Items.MELON_SLICE,
                                                    Items.PUMPKIN_SEEDS
                                            )
                            )
                            .put(
                                    7,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(Blocks.CACTUS)
                                            .withItems(Items.COPPER_AXE, Items.GOLDEN_AXE, Items.STONE_HOE)
                            )
                            .put(
                                    8,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.SPONGE,
                                                    Blocks.WET_SPONGE,
                                                    Blocks.SEA_PICKLE,
                                                    Blocks.DEAD_BRAIN_CORAL,
                                                    Blocks.DEAD_BRAIN_CORAL_BLOCK,
                                                    Blocks.DEAD_BRAIN_CORAL_FAN,
                                                    Blocks.DEAD_BRAIN_CORAL_WALL_FAN,
                                                    Blocks.DEAD_BUBBLE_CORAL,
                                                    Blocks.DEAD_BUBBLE_CORAL_BLOCK,
                                                    Blocks.DEAD_BUBBLE_CORAL_FAN,
                                                    Blocks.DEAD_BUBBLE_CORAL_WALL_FAN,
                                                    Blocks.DEAD_FIRE_CORAL,
                                                    Blocks.DEAD_FIRE_CORAL_BLOCK,
                                                    Blocks.DEAD_FIRE_CORAL_FAN,
                                                    Blocks.DEAD_FIRE_CORAL_WALL_FAN,
                                                    Blocks.DEAD_HORN_CORAL,
                                                    Blocks.DEAD_HORN_CORAL_BLOCK,
                                                    Blocks.DEAD_HORN_CORAL_FAN,
                                                    Blocks.DEAD_HORN_CORAL_WALL_FAN,
                                                    Blocks.DEAD_TUBE_CORAL,
                                                    Blocks.DEAD_TUBE_CORAL_BLOCK,
                                                    Blocks.DEAD_TUBE_CORAL_FAN,
                                                    Blocks.DEAD_TUBE_CORAL_WALL_FAN
                                            )
                                            .withBlockTags(
                                                    BlockTags.CORALS,
                                                    BlockTags.WALL_CORALS,
                                                    BlockTags.CORAL_BLOCKS
                                            )
                            )
                            .put(
                                    9,
                                    new SkillLevelDataGenSpec()
                                            .withBlockTags(BlockTags.FLOWER_POTS)
                                            .withItems(Items.COPPER_HOE, Items.GOLDEN_HOE)
                            )
                            .put(
                                    10,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.SPORE_BLOSSOM,
                                                    Blocks.MOSS_CARPET,
                                                    Blocks.MOSS_BLOCK,
                                                    Blocks.PALE_MOSS_CARPET,
                                                    Blocks.PALE_MOSS_BLOCK,
                                                    Blocks.PALE_HANGING_MOSS,
                                                    Blocks.AZALEA,
                                                    Blocks.FLOWERING_AZALEA,
                                                    Blocks.BIG_DRIPLEAF,
                                                    Blocks.BIG_DRIPLEAF_STEM,
                                                    Blocks.SMALL_DRIPLEAF,
                                                    Blocks.HANGING_ROOTS
                                            )
                                            .withItems(Items.IRON_AXE)
                            )
                            .put(11, new SkillLevelDataGenSpec().withItems(Items.IRON_HOE))
                            .put(
                                    13,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.PITCHER_CROP,
                                                    Blocks.PITCHER_PLANT,
                                                    Blocks.TORCHFLOWER_CROP,
                                                    Blocks.TORCHFLOWER
                                            )
                                            .withItems(
                                                    Items.PITCHER_POD,
                                                    Items.TORCHFLOWER_SEEDS,
                                                    Items.DIAMOND_AXE,
                                                    Items.DIAMOND_HOE
                                            )
                            )
                            .put(
                                    14,
                                    new SkillLevelDataGenSpec().withBlocks(
                                            Blocks.NETHER_WART,
                                            Blocks.NETHER_WART_BLOCK,
                                            Blocks.SHROOMLIGHT,
                                            Blocks.CRIMSON_STEM,
                                            Blocks.STRIPPED_CRIMSON_STEM,
                                            Blocks.CRIMSON_HYPHAE,
                                            Blocks.STRIPPED_CRIMSON_HYPHAE,
                                            Blocks.CRIMSON_NYLIUM,
                                            Blocks.CRIMSON_FUNGUS,
                                            Blocks.CRIMSON_ROOTS,
                                            Blocks.CRIMSON_PLANKS,
                                            Blocks.WEEPING_VINES,
                                            Blocks.WEEPING_VINES_PLANT,
                                            Blocks.POTTED_CRIMSON_FUNGUS,
                                            Blocks.POTTED_CRIMSON_ROOTS,
                                            Blocks.WARPED_STEM,
                                            Blocks.WARPED_HYPHAE,
                                            Blocks.STRIPPED_WARPED_STEM,
                                            Blocks.STRIPPED_WARPED_HYPHAE,
                                            Blocks.WARPED_NYLIUM,
                                            Blocks.WARPED_FUNGUS,
                                            Blocks.WARPED_WART_BLOCK,
                                            Blocks.WARPED_ROOTS,
                                            Blocks.WARPED_PLANKS,
                                            Blocks.TWISTING_VINES,
                                            Blocks.TWISTING_VINES_PLANT,
                                            Blocks.POTTED_WARPED_FUNGUS,
                                            Blocks.POTTED_WARPED_ROOTS
                                    )
                            )
                            .put(
                                    16,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(Blocks.WITHER_ROSE)
                                            .withItems(Items.NETHERITE_AXE, Items.NETHERITE_HOE)
                            )
                            .put(
                                    17,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(Blocks.CHORUS_PLANT, Blocks.CHORUS_FLOWER)
                                            .withItems(
                                                    Items.CHORUS_FRUIT,
                                                    Items.POPPED_CHORUS_FRUIT
                                            )
                            )
                            .build()
            ),
            // ANIMAL_HUSBANDRY
            new SkillDataGenSpec(
                    Identifier.of("skillmmo", "animalhusbandry"),
                    "skillmmo.skill.animalhusbandry.name",
                    "skillmmo.skill.animalhusbandry.description",
                    Items.SADDLE,
                    levelsBuilder()
                            .put(
                                    2,
                                    new SkillLevelDataGenSpec()
                                            .withItemTags(ItemTags.EGGS)
                                            .withEntityTypes(
                                                    EntityType.BAT,
                                                    EntityType.CHICKEN,
                                                    EntityType.RABBIT
                                            )
                            )
                            .put(
                                    3,
                                    new SkillLevelDataGenSpec()
                                            .withBlockTags(BlockTags.WOOL)
                                            .withItems(Items.GOAT_HORN, Items.CARROT_ON_A_STICK)
                                            .withEntityTypes(
                                                    EntityType.GOAT,
                                                    EntityType.PIG,
                                                    EntityType.SHEEP
                                            )
                            )
                            .put(
                                    4,
                                    new SkillLevelDataGenSpec().withEntityTypes(
                                            EntityType.COW,
                                            EntityType.MOOSHROOM
                                    )
                            )
                            .put(
                                    5,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.BEE_NEST,
                                                    Blocks.BEEHIVE,
                                                    Blocks.HONEY_BLOCK,
                                                    Blocks.HONEYCOMB_BLOCK
                                            )
                                            .withItems(Items.HONEYCOMB, Items.HONEY_BOTTLE)
                                            .withEntityTypes(EntityType.BEE)
                            )
                            .put(
                                    6,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.PEARLESCENT_FROGLIGHT,
                                                    Blocks.VERDANT_FROGLIGHT,
                                                    Blocks.OCHRE_FROGLIGHT,
                                                    Blocks.FROGSPAWN
                                            )
                                            .withItems(Items.TADPOLE_BUCKET, Items.ARMADILLO_SCUTE)
                                            .withEntityTypes(
                                                    EntityType.FROG,
                                                    EntityType.TADPOLE,
                                                    EntityType.ARMADILLO
                                            )
                            )
                            .put(
                                    7,
                                    new SkillLevelDataGenSpec()
                                            .withItems(
                                                    Items.COD_BUCKET,
                                                    Items.PUFFERFISH_BUCKET,
                                                    Items.SALMON_BUCKET,
                                                    Items.TROPICAL_FISH_BUCKET
                                            )
                                            .withEntityTypes(
                                                    EntityType.COD,
                                                    EntityType.PUFFERFISH,
                                                    EntityType.SALMON,
                                                    EntityType.TROPICAL_FISH
                                            )
                            )
                            .put(
                                    8,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(Blocks.TURTLE_EGG)
                                            .withItems(
                                                    Items.AXOLOTL_BUCKET,
                                                    Items.TURTLE_SCUTE,
                                                    Items.TURTLE_HELMET
                                            )
                                            .withEntityTypes(
                                                    EntityType.AXOLOTL,
                                                    EntityType.DOLPHIN,
                                                    EntityType.GLOW_SQUID,
                                                    EntityType.SQUID,
                                                    EntityType.TURTLE
                                            )
                            )
                            .put(
                                    9,
                                    new SkillLevelDataGenSpec().withEntityTypes(
                                            EntityType.LLAMA,
                                            EntityType.TRADER_LLAMA
                                    )
                            )
                            .put(
                                    10,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(Blocks.SMOKER)
                                            .withEntityTypes(
                                                    EntityType.CAT,
                                                    EntityType.FOX,
                                                    EntityType.OCELOT,
                                                    EntityType.PARROT,
                                                    EntityType.WOLF
                                            )
                            )
                            .put(
                                    12,
                                    new SkillLevelDataGenSpec()
                                            .withItems(
                                                    Items.SADDLE,
                                                    Items.LEAD
                                            )
                                            .withEntityTypes(
                                                    EntityType.LEASH_KNOT,
                                                    EntityType.DONKEY,
                                                    EntityType.HORSE,
                                                    EntityType.MULE,
                                                    EntityType.SKELETON_HORSE,
                                                    EntityType.ZOMBIE_HORSE,
                                                    EntityType.CAMEL
                                            )
                            )
                            .put(
                                    14,
                                    new SkillLevelDataGenSpec().withEntityTypes(
                                            EntityType.PANDA,
                                            EntityType.POLAR_BEAR
                                    )
                            )
                            .put(
                                    15,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.DRIED_GHAST,
                                                    Blocks.SNIFFER_EGG
                                            )
                                            .withItems(
                                                    Items.WARPED_FUNGUS_ON_A_STICK
                                            )
                                            .withItemTags(
                                                    ItemTags.HARNESSES
                                            )
                                            .withEntityTypes(
                                                    EntityType.STRIDER,
                                                    EntityType.SNIFFER,
                                                    EntityType.HAPPY_GHAST
                                            )
                            )
                            .build()
            ),
            // BUILDING
            new SkillDataGenSpec(
                    Identifier.of("skillmmo", "building"),
                    "skillmmo.skill.building.name",
                    "skillmmo.skill.building.description",
                    Items.SCAFFOLDING,
                    levelsBuilder()
                            .put(
                                    1,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.BAMBOO_MOSAIC,
                                                    Blocks.BAMBOO_MOSAIC_STAIRS,
                                                    Blocks.BAMBOO_MOSAIC_SLAB
                                            )
                                            .withBlockTags(
                                                    BlockTags.WOODEN_STAIRS,
                                                    BlockTags.WOODEN_SLABS
                                            )
                                            .withItems(
                                                    Items.WHITE_DYE,
                                                    Items.ORANGE_DYE,
                                                    Items.MAGENTA_DYE,
                                                    Items.LIGHT_BLUE_DYE,
                                                    Items.YELLOW_DYE,
                                                    Items.LIME_DYE,
                                                    Items.PINK_DYE,
                                                    Items.GRAY_DYE,
                                                    Items.LIGHT_GRAY_DYE,
                                                    Items.CYAN_DYE,
                                                    Items.PURPLE_DYE,
                                                    Items.BLUE_DYE,
                                                    Items.BROWN_DYE,
                                                    Items.GREEN_DYE,
                                                    Items.RED_DYE,
                                                    Items.BLACK_DYE
                                            )
                            )
                            .put(
                                    3,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.BOOKSHELF,
                                                    Blocks.CHISELED_BOOKSHELF,
                                                    Blocks.LADDER,
                                                    Blocks.GLASS,
                                                    Blocks.GLASS_PANE
                                            )
                                            .withBlockTags(BlockTags.WOODEN_FENCES, BlockTags.BANNERS)
                                            .withItems(
                                                    Items.PAINTING,
                                                    Items.GLOW_INK_SAC,
                                                    Items.FLOWER_BANNER_PATTERN,
                                                    Items.CREEPER_BANNER_PATTERN,
                                                    Items.SKULL_BANNER_PATTERN,
                                                    Items.MOJANG_BANNER_PATTERN,
                                                    Items.GLOBE_BANNER_PATTERN,
                                                    Items.PIGLIN_BANNER_PATTERN,
                                                    Items.FLOW_BANNER_PATTERN,
                                                    Items.GUSTER_BANNER_PATTERN,
                                                    Items.FIELD_MASONED_BANNER_PATTERN,
                                                    Items.BORDURE_INDENTED_BANNER_PATTERN
                                            )
                                            .withItemTags(ItemTags.BANNERS)
                                            .withEntityTypes(EntityType.PAINTING)
                            )
                            .put(
                                    5,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.SCAFFOLDING,
                                                    Blocks.LOOM,
                                                    Blocks.LECTERN,
                                                    Blocks.STONECUTTER,
                                                    Blocks.DECORATED_POT
                                            )
                                            .withBlockTags(
                                                    BlockTags.CANDLES,
                                                    BlockTags.STANDING_SIGNS,
                                                    BlockTags.WALL_SIGNS,
                                                    BlockTags.WOOL_CARPETS
                                            )
                                            .withItemTags(ItemTags.DECORATED_POT_SHERDS)
                            )
                            .put(
                                    6,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.MUD_BRICKS,
                                                    Blocks.MUD_BRICK_STAIRS,
                                                    Blocks.MUD_BRICK_SLAB,
                                                    Blocks.MUD_BRICK_WALL,
                                                    Blocks.ANDESITE_STAIRS,
                                                    Blocks.ANDESITE_SLAB,
                                                    Blocks.ANDESITE_WALL,
                                                    Blocks.DIORITE_STAIRS,
                                                    Blocks.DIORITE_SLAB,
                                                    Blocks.DIORITE_WALL,
                                                    Blocks.GRANITE_STAIRS,
                                                    Blocks.GRANITE_SLAB,
                                                    Blocks.GRANITE_WALL,
                                                    Blocks.TUFF_STAIRS,
                                                    Blocks.TUFF_SLAB,
                                                    Blocks.TUFF_WALL,
                                                    Blocks.POLISHED_ANDESITE,
                                                    Blocks.POLISHED_ANDESITE_STAIRS,
                                                    Blocks.POLISHED_ANDESITE_SLAB,
                                                    Blocks.POLISHED_DIORITE,
                                                    Blocks.POLISHED_DIORITE_STAIRS,
                                                    Blocks.POLISHED_DIORITE_SLAB,
                                                    Blocks.POLISHED_GRANITE,
                                                    Blocks.POLISHED_GRANITE_STAIRS,
                                                    Blocks.POLISHED_GRANITE_SLAB,
                                                    Blocks.POLISHED_TUFF,
                                                    Blocks.POLISHED_TUFF_STAIRS,
                                                    Blocks.POLISHED_TUFF_SLAB,
                                                    Blocks.POLISHED_TUFF_WALL,
                                                    Blocks.COBBLESTONE_STAIRS,
                                                    Blocks.COBBLESTONE_SLAB,
                                                    Blocks.COBBLESTONE_WALL,
                                                    Blocks.MOSSY_COBBLESTONE,
                                                    Blocks.MOSSY_COBBLESTONE_STAIRS,
                                                    Blocks.MOSSY_COBBLESTONE_SLAB,
                                                    Blocks.MOSSY_COBBLESTONE_WALL,
                                                    Blocks.SMOOTH_STONE,
                                                    Blocks.SMOOTH_STONE_SLAB,
                                                    Blocks.STONE_SLAB,
                                                    Blocks.PETRIFIED_OAK_SLAB,
                                                    Blocks.STONE_STAIRS,
                                                    Blocks.SANDSTONE_STAIRS,
                                                    Blocks.SANDSTONE_SLAB,
                                                    Blocks.SANDSTONE_WALL,
                                                    Blocks.RED_SANDSTONE_STAIRS,
                                                    Blocks.RED_SANDSTONE_SLAB,
                                                    Blocks.RED_SANDSTONE_WALL,
                                                    Blocks.CHISELED_SANDSTONE,
                                                    Blocks.CUT_SANDSTONE,
                                                    Blocks.CUT_SANDSTONE_SLAB,
                                                    Blocks.CHISELED_RED_SANDSTONE,
                                                    Blocks.CUT_RED_SANDSTONE,
                                                    Blocks.CUT_RED_SANDSTONE_SLAB,
                                                    Blocks.SMOOTH_SANDSTONE,
                                                    Blocks.SMOOTH_SANDSTONE_STAIRS,
                                                    Blocks.SMOOTH_SANDSTONE_SLAB,
                                                    Blocks.SMOOTH_RED_SANDSTONE,
                                                    Blocks.SMOOTH_RED_SANDSTONE_STAIRS,
                                                    Blocks.SMOOTH_RED_SANDSTONE_SLAB
                                            )
                            )
                            .put(
                                    8,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.BRICKS,
                                                    Blocks.BRICK_STAIRS,
                                                    Blocks.BRICK_SLAB,
                                                    Blocks.BRICK_WALL,
                                                    Blocks.STONE_BRICKS,
                                                    Blocks.STONE_BRICK_STAIRS,
                                                    Blocks.STONE_BRICK_SLAB,
                                                    Blocks.STONE_BRICK_WALL,
                                                    Blocks.CRACKED_STONE_BRICKS,
                                                    Blocks.CHISELED_STONE_BRICKS,
                                                    Blocks.MOSSY_STONE_BRICKS,
                                                    Blocks.MOSSY_STONE_BRICK_STAIRS,
                                                    Blocks.MOSSY_STONE_BRICK_SLAB,
                                                    Blocks.MOSSY_STONE_BRICK_WALL,
                                                    Blocks.INFESTED_STONE_BRICKS,
                                                    Blocks.INFESTED_MOSSY_STONE_BRICKS,
                                                    Blocks.INFESTED_CRACKED_STONE_BRICKS,
                                                    Blocks.INFESTED_CHISELED_STONE_BRICKS,
                                                    Blocks.TUFF_BRICKS,
                                                    Blocks.TUFF_BRICK_SLAB,
                                                    Blocks.TUFF_BRICK_STAIRS,
                                                    Blocks.TUFF_BRICK_WALL,
                                                    Blocks.CHISELED_TUFF,
                                                    Blocks.CHISELED_TUFF_BRICKS,
                                                    Blocks.CHISELED_QUARTZ_BLOCK,
                                                    Blocks.QUARTZ_BRICKS,
                                                    Blocks.QUARTZ_STAIRS,
                                                    Blocks.QUARTZ_SLAB,
                                                    Blocks.QUARTZ_PILLAR,
                                                    Blocks.SMOOTH_QUARTZ,
                                                    Blocks.SMOOTH_QUARTZ_STAIRS,
                                                    Blocks.SMOOTH_QUARTZ_SLAB,
                                                    Blocks.RESIN_BRICKS,
                                                    Blocks.RESIN_BRICK_STAIRS,
                                                    Blocks.RESIN_BRICK_SLAB,
                                                    Blocks.RESIN_BRICK_WALL,
                                                    Blocks.CHISELED_RESIN_BRICKS
                                            )
                                            .withItems(
                                                    Items.BRICK
                                            )
                            )
                            .put(
                                    10,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.WHITE_GLAZED_TERRACOTTA,
                                                    Blocks.ORANGE_GLAZED_TERRACOTTA,
                                                    Blocks.MAGENTA_GLAZED_TERRACOTTA,
                                                    Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA,
                                                    Blocks.YELLOW_GLAZED_TERRACOTTA,
                                                    Blocks.LIME_GLAZED_TERRACOTTA,
                                                    Blocks.PINK_GLAZED_TERRACOTTA,
                                                    Blocks.GRAY_GLAZED_TERRACOTTA,
                                                    Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA,
                                                    Blocks.CYAN_GLAZED_TERRACOTTA,
                                                    Blocks.PURPLE_GLAZED_TERRACOTTA,
                                                    Blocks.BLUE_GLAZED_TERRACOTTA,
                                                    Blocks.BROWN_GLAZED_TERRACOTTA,
                                                    Blocks.GREEN_GLAZED_TERRACOTTA,
                                                    Blocks.RED_GLAZED_TERRACOTTA,
                                                    Blocks.BLACK_GLAZED_TERRACOTTA,
                                                    Blocks.WHITE_CONCRETE,
                                                    Blocks.ORANGE_CONCRETE,
                                                    Blocks.MAGENTA_CONCRETE,
                                                    Blocks.LIGHT_BLUE_CONCRETE,
                                                    Blocks.YELLOW_CONCRETE,
                                                    Blocks.LIME_CONCRETE,
                                                    Blocks.PINK_CONCRETE,
                                                    Blocks.GRAY_CONCRETE,
                                                    Blocks.LIGHT_GRAY_CONCRETE,
                                                    Blocks.CYAN_CONCRETE,
                                                    Blocks.PURPLE_CONCRETE,
                                                    Blocks.BLUE_CONCRETE,
                                                    Blocks.BROWN_CONCRETE,
                                                    Blocks.GREEN_CONCRETE,
                                                    Blocks.RED_CONCRETE,
                                                    Blocks.BLACK_CONCRETE
                                            )
                                            .withBlockTags(
                                                    BlockTags.ALL_HANGING_SIGNS,
                                                    BlockTags.CONCRETE_POWDER
                                            )
                            )
                            .put(
                                    13,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.WHITE_STAINED_GLASS,
                                                    Blocks.ORANGE_STAINED_GLASS,
                                                    Blocks.MAGENTA_STAINED_GLASS,
                                                    Blocks.LIGHT_BLUE_STAINED_GLASS,
                                                    Blocks.YELLOW_STAINED_GLASS,
                                                    Blocks.LIME_STAINED_GLASS,
                                                    Blocks.PINK_STAINED_GLASS,
                                                    Blocks.GRAY_STAINED_GLASS,
                                                    Blocks.LIGHT_GRAY_STAINED_GLASS,
                                                    Blocks.CYAN_STAINED_GLASS,
                                                    Blocks.PURPLE_STAINED_GLASS,
                                                    Blocks.BLUE_STAINED_GLASS,
                                                    Blocks.BROWN_STAINED_GLASS,
                                                    Blocks.GREEN_STAINED_GLASS,
                                                    Blocks.RED_STAINED_GLASS,
                                                    Blocks.BLACK_STAINED_GLASS,
                                                    Blocks.WHITE_STAINED_GLASS_PANE,
                                                    Blocks.ORANGE_STAINED_GLASS_PANE,
                                                    Blocks.MAGENTA_STAINED_GLASS_PANE,
                                                    Blocks.LIGHT_BLUE_STAINED_GLASS_PANE,
                                                    Blocks.YELLOW_STAINED_GLASS_PANE,
                                                    Blocks.LIME_STAINED_GLASS_PANE,
                                                    Blocks.PINK_STAINED_GLASS_PANE,
                                                    Blocks.GRAY_STAINED_GLASS_PANE,
                                                    Blocks.LIGHT_GRAY_STAINED_GLASS_PANE,
                                                    Blocks.CYAN_STAINED_GLASS_PANE,
                                                    Blocks.PURPLE_STAINED_GLASS_PANE,
                                                    Blocks.BLUE_STAINED_GLASS_PANE,
                                                    Blocks.BROWN_STAINED_GLASS_PANE,
                                                    Blocks.GREEN_STAINED_GLASS_PANE,
                                                    Blocks.RED_STAINED_GLASS_PANE,
                                                    Blocks.BLACK_STAINED_GLASS_PANE,
                                                    Blocks.TINTED_GLASS
                                            )
                            )
                            .put(
                                    15,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.CUT_COPPER,
                                                    Blocks.CUT_COPPER_STAIRS,
                                                    Blocks.CUT_COPPER_SLAB,
                                                    Blocks.EXPOSED_CUT_COPPER,
                                                    Blocks.EXPOSED_CUT_COPPER_STAIRS,
                                                    Blocks.EXPOSED_CUT_COPPER_SLAB,
                                                    Blocks.WEATHERED_CUT_COPPER,
                                                    Blocks.WEATHERED_CUT_COPPER_STAIRS,
                                                    Blocks.WEATHERED_CUT_COPPER_SLAB,
                                                    Blocks.OXIDIZED_CUT_COPPER,
                                                    Blocks.OXIDIZED_CUT_COPPER_STAIRS,
                                                    Blocks.OXIDIZED_CUT_COPPER_SLAB,
                                                    Blocks.WAXED_CUT_COPPER,
                                                    Blocks.WAXED_CUT_COPPER_STAIRS,
                                                    Blocks.WAXED_CUT_COPPER_SLAB,
                                                    Blocks.WAXED_EXPOSED_CUT_COPPER,
                                                    Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS,
                                                    Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB,
                                                    Blocks.WAXED_WEATHERED_CUT_COPPER,
                                                    Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS,
                                                    Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB,
                                                    Blocks.WAXED_OXIDIZED_CUT_COPPER,
                                                    Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS,
                                                    Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB,
                                                    Blocks.CHISELED_COPPER,
                                                    Blocks.EXPOSED_CHISELED_COPPER,
                                                    Blocks.WEATHERED_CHISELED_COPPER,
                                                    Blocks.OXIDIZED_CHISELED_COPPER,
                                                    Blocks.WAXED_CHISELED_COPPER,
                                                    Blocks.WAXED_EXPOSED_CHISELED_COPPER,
                                                    Blocks.WAXED_WEATHERED_CHISELED_COPPER,
                                                    Blocks.WAXED_OXIDIZED_CHISELED_COPPER,
                                                    Blocks.COPPER_GRATE,
                                                    Blocks.EXPOSED_COPPER_GRATE,
                                                    Blocks.WEATHERED_COPPER_GRATE,
                                                    Blocks.OXIDIZED_COPPER_GRATE,
                                                    Blocks.WAXED_COPPER_GRATE,
                                                    Blocks.WAXED_EXPOSED_COPPER_GRATE,
                                                    Blocks.WAXED_WEATHERED_COPPER_GRATE,
                                                    Blocks.WAXED_OXIDIZED_COPPER_GRATE
                                            )
                                            .withBlockSets(
                                                    Blocks.COPPER_LANTERNS
                                            )
                                            .withBlockTags(
                                                    BlockTags.BARS,
                                                    BlockTags.CHAINS
                                            )
                            )
                            .put(
                                    17,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.PRISMARINE_STAIRS,
                                                    Blocks.PRISMARINE_BRICK_STAIRS,
                                                    Blocks.DARK_PRISMARINE_STAIRS,
                                                    Blocks.PRISMARINE_SLAB,
                                                    Blocks.PRISMARINE_BRICK_SLAB,
                                                    Blocks.DARK_PRISMARINE_SLAB,
                                                    Blocks.PRISMARINE_WALL,
                                                    Blocks.COBBLED_DEEPSLATE_STAIRS,
                                                    Blocks.COBBLED_DEEPSLATE_SLAB,
                                                    Blocks.COBBLED_DEEPSLATE_WALL,
                                                    Blocks.POLISHED_DEEPSLATE,
                                                    Blocks.POLISHED_DEEPSLATE_STAIRS,
                                                    Blocks.POLISHED_DEEPSLATE_SLAB,
                                                    Blocks.POLISHED_DEEPSLATE_WALL,
                                                    Blocks.DEEPSLATE_TILES,
                                                    Blocks.DEEPSLATE_TILE_STAIRS,
                                                    Blocks.DEEPSLATE_TILE_SLAB,
                                                    Blocks.DEEPSLATE_TILE_WALL,
                                                    Blocks.DEEPSLATE_BRICKS,
                                                    Blocks.DEEPSLATE_BRICK_STAIRS,
                                                    Blocks.DEEPSLATE_BRICK_SLAB,
                                                    Blocks.DEEPSLATE_BRICK_WALL,
                                                    Blocks.CHISELED_DEEPSLATE,
                                                    Blocks.CRACKED_DEEPSLATE_BRICKS,
                                                    Blocks.CRACKED_DEEPSLATE_TILES
                                            )
                                            .withBlockTags(
                                                    BlockTags.CORALS,
                                                    BlockTags.CORAL_BLOCKS,
                                                    BlockTags.WALL_CORALS
                                            )
                            )
                            .put(
                                    20,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.SMOOTH_BASALT,
                                                    Blocks.POLISHED_BASALT,
                                                    Blocks.NETHER_BRICKS,
                                                    Blocks.NETHER_BRICK_FENCE,
                                                    Blocks.NETHER_BRICK_STAIRS,
                                                    Blocks.NETHER_BRICK_SLAB,
                                                    Blocks.RED_NETHER_BRICKS,
                                                    Blocks.RED_NETHER_BRICK_SLAB,
                                                    Blocks.RED_NETHER_BRICK_STAIRS,
                                                    Blocks.NETHER_BRICK_WALL,
                                                    Blocks.RED_NETHER_BRICK_WALL,
                                                    Blocks.BLACKSTONE_STAIRS,
                                                    Blocks.BLACKSTONE_WALL,
                                                    Blocks.BLACKSTONE_SLAB,
                                                    Blocks.POLISHED_BLACKSTONE,
                                                    Blocks.POLISHED_BLACKSTONE_BRICKS,
                                                    Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS,
                                                    Blocks.CHISELED_POLISHED_BLACKSTONE,
                                                    Blocks.POLISHED_BLACKSTONE_BRICK_SLAB,
                                                    Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS,
                                                    Blocks.POLISHED_BLACKSTONE_BRICK_WALL,
                                                    Blocks.POLISHED_BLACKSTONE_STAIRS,
                                                    Blocks.POLISHED_BLACKSTONE_SLAB,
                                                    Blocks.POLISHED_BLACKSTONE_WALL,
                                                    Blocks.CHISELED_NETHER_BRICKS,
                                                    Blocks.CRACKED_NETHER_BRICKS
                                            )
                                            .withItems(
                                                    Items.NETHER_BRICK
                                            )
                            )
                            .put(
                                    25,
                                    new SkillLevelDataGenSpec().withBlocks(
                                            Blocks.PURPUR_BLOCK,
                                            Blocks.PURPUR_PILLAR,
                                            Blocks.PURPUR_STAIRS,
                                            Blocks.PURPUR_SLAB,
                                            Blocks.END_ROD,
                                            Blocks.END_STONE_BRICKS,
                                            Blocks.END_STONE_BRICK_SLAB,
                                            Blocks.END_STONE_BRICK_STAIRS,
                                            Blocks.END_STONE_BRICK_WALL
                                    )
                            )
                            .build()
            ),
            // COMBAT
            new SkillDataGenSpec(
                    Identifier.of("skillmmo", "combat"),
                    "skillmmo.skill.combat.name",
                    "skillmmo.skill.combat.description",
                    Items.DIAMOND_SWORD,
                    levelsBuilder()
                            .put(
                                    1,
                                    new SkillLevelDataGenSpec()
                                            .withItems(
                                                    Items.ARMOR_STAND,
                                                    Items.WOODEN_SWORD
                                            )
                                            .withEntityTypes(
                                                    EntityType.ARMOR_STAND
                                            )
                            )
                            .put(
                                    2,
                                    new SkillLevelDataGenSpec().withItems(
                                            Items.LEATHER_HELMET,
                                            Items.LEATHER_CHESTPLATE,
                                            Items.LEATHER_LEGGINGS,
                                            Items.LEATHER_BOOTS,
                                            Items.LEATHER_HORSE_ARMOR
                                    )
                            )
                            .put(
                                    4,
                                    new SkillLevelDataGenSpec().withItems(Items.STONE_SWORD)
                            )
                            .put(
                                    5,
                                    new SkillLevelDataGenSpec().withItems(
                                            Items.CHAINMAIL_HELMET,
                                            Items.CHAINMAIL_CHESTPLATE,
                                            Items.CHAINMAIL_LEGGINGS,
                                            Items.CHAINMAIL_BOOTS
                                    )
                            )
                            .put(6, new SkillLevelDataGenSpec().withItems(Items.WOLF_ARMOR))
                            .put(
                                    7,
                                    new SkillLevelDataGenSpec().withItems(
                                            Items.BOW,
                                            Items.ARROW
                                    )
                            )
                            .put(
                                    8,
                                    new SkillLevelDataGenSpec().withItems(
                                            Items.COPPER_SWORD,
                                            Items.COPPER_HELMET,
                                            Items.COPPER_CHESTPLATE,
                                            Items.COPPER_LEGGINGS,
                                            Items.COPPER_BOOTS,
                                            Items.COPPER_HORSE_ARMOR,
                                            Items.GOLDEN_SWORD,
                                            Items.GOLDEN_HELMET,
                                            Items.GOLDEN_CHESTPLATE,
                                            Items.GOLDEN_LEGGINGS,
                                            Items.GOLDEN_BOOTS,
                                            Items.GOLDEN_HORSE_ARMOR
                                    )
                            )
                            .put(
                                    10,
                                    new SkillLevelDataGenSpec().withItems(
                                            Items.IRON_SWORD,
                                            Items.IRON_HELMET,
                                            Items.IRON_CHESTPLATE,
                                            Items.IRON_LEGGINGS,
                                            Items.IRON_BOOTS,
                                            Items.IRON_HORSE_ARMOR,
                                            Items.SHIELD
                                    )
                            )
                            .put(12, new SkillLevelDataGenSpec().withItems(Items.CROSSBOW))
                            .put(
                                    15,
                                    new SkillLevelDataGenSpec().withItems(
                                            Items.DIAMOND_SWORD,
                                            Items.DIAMOND_HELMET,
                                            Items.DIAMOND_CHESTPLATE,
                                            Items.DIAMOND_LEGGINGS,
                                            Items.DIAMOND_BOOTS,
                                            Items.DIAMOND_HORSE_ARMOR,
                                            Items.TRIDENT
                                    )
                            )
                            .put(
                                    20,
                                    new SkillLevelDataGenSpec().withItems(
                                            Items.NETHERITE_SWORD,
                                            Items.NETHERITE_HELMET,
                                            Items.NETHERITE_CHESTPLATE,
                                            Items.NETHERITE_LEGGINGS,
                                            Items.NETHERITE_BOOTS
                                    )
                            )
                            .put(25, new SkillLevelDataGenSpec().withItems(Items.MACE))
                            .build()
            ),
            // ENGINEERING
            new SkillDataGenSpec(
                    Identifier.of("skillmmo", "engineering"),
                    "skillmmo.skill.engineering.name",
                    "skillmmo.skill.engineering.description",
                    Items.REDSTONE,
                    levelsBuilder()
                            .put(
                                    1,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(Blocks.LEVER)
                                            .withBlockTags(
                                                    BlockTags.WOODEN_BUTTONS,
                                                    BlockTags.STONE_BUTTONS
                                            )
                            )
                            .put(
                                    3,
                                    new SkillLevelDataGenSpec().withBlockTags(
                                            BlockTags.PRESSURE_PLATES
                                    )
                            )
                            .put(
                                    5,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.REDSTONE_WIRE,
                                                    Blocks.REDSTONE_TORCH,
                                                    Blocks.REDSTONE_WALL_TORCH
                                            )
                                            .withItems(Items.COMPASS)
                            )
                            .put(
                                    6,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(Blocks.REDSTONE_LAMP)
                                            .withItems(Items.CLOCK)
                            )
                            .put(
                                    7,
                                    new SkillLevelDataGenSpec().withBlocks(
                                            Blocks.COPPER_BULB,
                                            Blocks.EXPOSED_COPPER_BULB,
                                            Blocks.WEATHERED_COPPER_BULB,
                                            Blocks.OXIDIZED_COPPER_BULB,
                                            Blocks.WAXED_COPPER_BULB,
                                            Blocks.WAXED_EXPOSED_COPPER_BULB,
                                            Blocks.WAXED_WEATHERED_COPPER_BULB,
                                            Blocks.WAXED_OXIDIZED_COPPER_BULB,
                                            Blocks.COPPER_DOOR,
                                            Blocks.EXPOSED_COPPER_DOOR,
                                            Blocks.WEATHERED_COPPER_DOOR,
                                            Blocks.OXIDIZED_COPPER_DOOR,
                                            Blocks.WAXED_COPPER_DOOR,
                                            Blocks.WAXED_EXPOSED_COPPER_DOOR,
                                            Blocks.WAXED_WEATHERED_COPPER_DOOR,
                                            Blocks.WAXED_OXIDIZED_COPPER_DOOR,
                                            Blocks.COPPER_TRAPDOOR,
                                            Blocks.EXPOSED_COPPER_TRAPDOOR,
                                            Blocks.WEATHERED_COPPER_TRAPDOOR,
                                            Blocks.OXIDIZED_COPPER_TRAPDOOR,
                                            Blocks.WAXED_COPPER_TRAPDOOR,
                                            Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR,
                                            Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR,
                                            Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR
                                    )
                            )
                            .put(
                                    8,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.IRON_DOOR,
                                                    Blocks.IRON_TRAPDOOR,
                                                    Blocks.SMITHING_TABLE
                                            )
                                            .withBlockTags(BlockTags.RAILS, BlockTags.ANVIL)
                                            .withItems(
                                                    Items.MINECART,
                                                    Items.FURNACE_MINECART,
                                                    Items.TNT_MINECART,
                                                    Items.HOPPER_MINECART
                                            )
                                            .withEntityTypes(
                                                    EntityType.MINECART,
                                                    EntityType.FURNACE_MINECART,
                                                    EntityType.TNT_MINECART,
                                                    EntityType.HOPPER_MINECART
                                            )
                            )
                            .put(
                                    9,
                                    new SkillLevelDataGenSpec().withBlocks(
                                            Blocks.TRIPWIRE,
                                            Blocks.TRIPWIRE_HOOK
                                    )
                            )
                            .put(
                                    10,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.DISPENSER,
                                                    Blocks.DROPPER,
                                                    Blocks.NOTE_BLOCK,
                                                    Blocks.JUKEBOX
                                            )
                                            .withItems(
                                                    Items.MUSIC_DISC_5,
                                                    Items.MUSIC_DISC_11,
                                                    Items.MUSIC_DISC_13,
                                                    Items.MUSIC_DISC_CAT,
                                                    Items.MUSIC_DISC_BLOCKS,
                                                    Items.MUSIC_DISC_CHIRP,
                                                    Items.MUSIC_DISC_CREATOR,
                                                    Items.MUSIC_DISC_CREATOR_MUSIC_BOX,
                                                    Items.MUSIC_DISC_FAR,
                                                    Items.MUSIC_DISC_LAVA_CHICKEN,
                                                    Items.MUSIC_DISC_MALL,
                                                    Items.MUSIC_DISC_MELLOHI,
                                                    Items.MUSIC_DISC_OTHERSIDE,
                                                    Items.MUSIC_DISC_PIGSTEP,
                                                    Items.MUSIC_DISC_PRECIPICE,
                                                    Items.MUSIC_DISC_RELIC,
                                                    Items.MUSIC_DISC_STAL,
                                                    Items.MUSIC_DISC_STRAD,
                                                    Items.MUSIC_DISC_TEARS,
                                                    Items.MUSIC_DISC_WAIT,
                                                    Items.MUSIC_DISC_WARD
                                            )
                            )
                            .put(
                                    12,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(Blocks.COMPARATOR, Blocks.REPEATER)
                                            .withItems(Items.FIREWORK_ROCKET, Items.FIREWORK_STAR)
                            )
                            .put(
                                    13,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.SLIME_BLOCK,
                                                    Blocks.STICKY_PISTON,
                                                    Blocks.PISTON,
                                                    Blocks.PISTON_HEAD,
                                                    Blocks.MOVING_PISTON
                                            )
                                            .withItems(
                                                    Items.SLIME_BALL
                                            )
                            )
                            .put(
                                    15,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.HOPPER,
                                                    Blocks.LODESTONE,
                                                    Blocks.OBSERVER,
                                                    Blocks.TARGET,
                                                    Blocks.TNT
                                            )
                                            .withItems(Items.TNT_MINECART)
                                            .withEntityTypes(EntityType.TNT_MINECART)
                            )
                            .put(16, new SkillLevelDataGenSpec().withBlocks(Blocks.CRAFTER))
                            .put(
                                    18,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.DAYLIGHT_DETECTOR,
                                                    Blocks.SCULK_SENSOR,
                                                    Blocks.CALIBRATED_SCULK_SENSOR
                                            )
                                            .withBlockTags(BlockTags.LIGHTNING_RODS)
                            )
                            .build()
            ),
            // MINING
            new SkillDataGenSpec(
                    Identifier.of("skillmmo", "mining"),
                    "skillmmo.skill.mining.name",
                    "skillmmo.skill.mining.description",
                    Items.GOLD_ORE,
                    levelsBuilder()
                            .put(
                                    1,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.STONE,
                                                    Blocks.COBBLESTONE,
                                                    Blocks.GRANITE,
                                                    Blocks.DIORITE,
                                                    Blocks.ANDESITE,
                                                    Blocks.CALCITE,
                                                    Blocks.TUFF,
                                                    Blocks.SANDSTONE,
                                                    Blocks.RED_SANDSTONE,
                                                    Blocks.INFESTED_STONE,
                                                    Blocks.INFESTED_COBBLESTONE
                                            )
                                            .withBlockTags(BlockTags.TERRACOTTA)
                                            .withItems(Items.WOODEN_SHOVEL, Items.WOODEN_PICKAXE)
                            )
                            .put(
                                    3,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.COAL_ORE,
                                                    Blocks.COAL_BLOCK
                                            )
                                            .withItems(Items.COAL)
                            )
                            .put(
                                    4,
                                    new SkillLevelDataGenSpec().withItems(
                                            Items.STONE_SHOVEL,
                                            Items.STONE_PICKAXE
                                    )
                            )
                            .put(
                                    5,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.COPPER_ORE,
                                                    Blocks.RAW_COPPER_BLOCK,
                                                    Blocks.COPPER_BLOCK,
                                                    Blocks.EXPOSED_COPPER,
                                                    Blocks.WEATHERED_COPPER,
                                                    Blocks.OXIDIZED_COPPER,
                                                    Blocks.WAXED_COPPER_BLOCK,
                                                    Blocks.WAXED_EXPOSED_COPPER,
                                                    Blocks.WAXED_WEATHERED_COPPER,
                                                    Blocks.WAXED_OXIDIZED_COPPER
                                            )
                                            .withItems(
                                                    Items.RAW_COPPER,
                                                    Items.COPPER_NUGGET,
                                                    Items.COPPER_INGOT
                                            )
                            )
                            .put(
                                    6,
                                    new SkillLevelDataGenSpec().withItems(
                                            Items.COPPER_SHOVEL,
                                            Items.COPPER_PICKAXE
                                    )
                            )
                            .put(
                                    7,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.IRON_ORE,
                                                    Blocks.RAW_IRON_BLOCK,
                                                    Blocks.IRON_BLOCK,
                                                    Blocks.POINTED_DRIPSTONE,
                                                    Blocks.DRIPSTONE_BLOCK
                                            )
                                            .withItems(
                                                    Items.RAW_IRON,
                                                    Items.IRON_INGOT,
                                                    Items.IRON_NUGGET
                                            )
                            )
                            .put(
                                    8,
                                    new SkillLevelDataGenSpec().withItems(
                                            Items.IRON_SHOVEL,
                                            Items.IRON_PICKAXE
                                    )
                            )
                            .put(
                                    9,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.LAPIS_ORE,
                                                    Blocks.LAPIS_BLOCK,
                                                    Blocks.REDSTONE_ORE,
                                                    Blocks.REDSTONE_BLOCK
                                            )
                                            .withItems(Items.LAPIS_LAZULI)
                            )
                            .put(
                                    10,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.BLAST_FURNACE,
                                                    Blocks.GOLD_ORE,
                                                    Blocks.GOLD_BLOCK,
                                                    Blocks.RAW_GOLD_BLOCK,
                                                    Blocks.MAGMA_BLOCK
                                            )
                                            .withItems(
                                                    Items.RAW_GOLD,
                                                    Items.GOLD_INGOT,
                                                    Items.GOLD_NUGGET,
                                                    Items.GOLDEN_SHOVEL,
                                                    Items.GOLDEN_PICKAXE
                                            )
                            )
                            .put(
                                    12,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.DIAMOND_ORE,
                                                    Blocks.DIAMOND_BLOCK,
                                                    Blocks.EMERALD_ORE,
                                                    Blocks.EMERALD_BLOCK
                                            )
                                            .withItems(
                                                    Items.DIAMOND,
                                                    Items.DIAMOND_SHOVEL,
                                                    Items.DIAMOND_PICKAXE,
                                                    Items.EMERALD
                                            )
                            )
                            .put(
                                    15,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.AMETHYST_BLOCK,
                                                    Blocks.AMETHYST_CLUSTER,
                                                    Blocks.BUDDING_AMETHYST,
                                                    Blocks.LARGE_AMETHYST_BUD,
                                                    Blocks.MEDIUM_AMETHYST_BUD,
                                                    Blocks.SMALL_AMETHYST_BUD
                                            )
                                            .withItems(Items.AMETHYST_SHARD)
                            )
                            .put(
                                    18,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.OBSIDIAN,
                                                    Blocks.CRYING_OBSIDIAN,
                                                    Blocks.NETHERRACK,
                                                    Blocks.BASALT,
                                                    Blocks.GLOWSTONE,
                                                    Blocks.NETHER_GOLD_ORE,
                                                    Blocks.NETHER_QUARTZ_ORE,
                                                    Blocks.QUARTZ_BLOCK,
                                                    Blocks.BLACKSTONE,
                                                    Blocks.GILDED_BLACKSTONE,
                                                    Blocks.END_STONE
                                            )
                                            .withItems(
                                                    Items.GLOWSTONE_DUST,
                                                    Items.QUARTZ
                                            )
                            )
                            .put(
                                    20,
                                    new SkillLevelDataGenSpec().withBlocks(
                                            Blocks.DEEPSLATE,
                                            Blocks.COBBLED_DEEPSLATE,
                                            Blocks.INFESTED_DEEPSLATE,
                                            Blocks.DEEPSLATE_COAL_ORE,
                                            Blocks.DEEPSLATE_COPPER_ORE,
                                            Blocks.DEEPSLATE_IRON_ORE,
                                            Blocks.DEEPSLATE_LAPIS_ORE,
                                            Blocks.DEEPSLATE_REDSTONE_ORE,
                                            Blocks.DEEPSLATE_GOLD_ORE,
                                            Blocks.DEEPSLATE_DIAMOND_ORE,
                                            Blocks.DEEPSLATE_EMERALD_ORE
                                    )
                            )
                            .put(
                                    25,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.ANCIENT_DEBRIS,
                                                    Blocks.NETHERITE_BLOCK
                                            )
                                            .withItems(
                                                    Items.NETHERITE_INGOT,
                                                    Items.NETHERITE_SCRAP,
                                                    Items.NETHERITE_SHOVEL,
                                                    Items.NETHERITE_PICKAXE,
                                                    Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE
                                            )
                            )
                            .build()
            ),
            // SORCERY
            new SkillDataGenSpec(
                    Identifier.of("skillmmo", "sorcery"),
                    "skillmmo.skill.sorcery.name",
                    "skillmmo.skill.sorcery.description",
                    Items.ENCHANTING_TABLE,
                    levelsBuilder()
                            .put(
                                    1,
                                    new SkillLevelDataGenSpec().withItems(Items.ENDER_PEARL)
                            )
                            .put(
                                    3,
                                    new SkillLevelDataGenSpec()
                                            .withBlockTags(BlockTags.CAULDRONS)
                                            .withItems(
                                                    Items.EXPERIENCE_BOTTLE,
                                                    Items.GOLDEN_APPLE,
                                                    Items.GOLDEN_CARROT,
                                                    Items.GLISTERING_MELON_SLICE
                                            )
                                            .withEntityTypes(EntityType.ALLAY)
                            )
                            .put(
                                    5,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(Blocks.BREWING_STAND)
                                            .withItems(
                                                    Items.ENCHANTED_BOOK,
                                                    Items.ENCHANTED_GOLDEN_APPLE,
                                                    Items.POTION,
                                                    Items.FERMENTED_SPIDER_EYE,
                                                    Items.BLAZE_ROD,
                                                    Items.BLAZE_POWDER,
                                                    Items.GHAST_TEAR,
                                                    Items.MAGMA_CREAM
                                            )
                                            .withEntityTypes(
                                                    EntityType.SNOW_GOLEM,
                                                    EntityType.ZOMBIE_VILLAGER
                                            )
                            )
                            .put(
                                    6,
                                    new SkillLevelDataGenSpec()
                                            .withBlockTags(BlockTags.COPPER_GOLEM_STATUES)
                                            .withEntityTypes(EntityType.COPPER_GOLEM)
                            )
                            .put(
                                    7,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.ZOMBIE_HEAD,
                                                    Blocks.ZOMBIE_WALL_HEAD,
                                                    Blocks.SKELETON_SKULL,
                                                    Blocks.SKELETON_WALL_SKULL,
                                                    Blocks.CREEPER_HEAD,
                                                    Blocks.CREEPER_WALL_HEAD,
                                                    Blocks.WITHER_SKELETON_SKULL,
                                                    Blocks.WITHER_SKELETON_WALL_SKULL,
                                                    Blocks.PIGLIN_HEAD,
                                                    Blocks.PIGLIN_WALL_HEAD,
                                                    Blocks.PLAYER_HEAD,
                                                    Blocks.PLAYER_WALL_HEAD,
                                                    Blocks.DRAGON_HEAD,
                                                    Blocks.DRAGON_WALL_HEAD
                                            )
                            )
                            .put(
                                    8,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(Blocks.ENCHANTING_TABLE, Blocks.GRINDSTONE)
                                            .withItems(
                                                    Items.ENDER_EYE,
                                                    Items.FIRE_CHARGE,
                                                    Items.SPECTRAL_ARROW,
                                                    Items.TIPPED_ARROW
                                            )
                            )
                            .put(
                                    10,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(Blocks.RESPAWN_ANCHOR)
                                            .withItems(Items.SPLASH_POTION, Items.TOTEM_OF_UNDYING)
                                            .withEntityTypes(EntityType.IRON_GOLEM)
                            )
                            .put(
                                    12,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.SCULK,
                                                    Blocks.SCULK_CATALYST,
                                                    Blocks.SCULK_SHRIEKER,
                                                    Blocks.SCULK_VEIN
                                            )
                                            .withItems(Items.DISC_FRAGMENT_5, Items.ECHO_SHARD)
                                            .withEntityTypes(EntityType.WARDEN)
                            )
                            .put(
                                    15,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.BEACON,
                                                    Blocks.DRAGON_EGG,
                                                    Blocks.END_PORTAL_FRAME
                                            )
                                            .withItems(
                                                    Items.DRAGON_BREATH,
                                                    Items.ELYTRA,
                                                    Items.PHANTOM_MEMBRANE,
                                                    Items.END_CRYSTAL,
                                                    Items.LINGERING_POTION,
                                                    Items.NETHER_STAR
                                            )
                                            .withEntityTypes(
                                                    EntityType.END_CRYSTAL,
                                                    EntityType.ENDER_DRAGON
                                            )
                            )
                            .build()
            ),
            // STORAGE
            new SkillDataGenSpec(
                    Identifier.of("skillmmo", "storage"),
                    "skillmmo.skill.storage.name",
                    "skillmmo.skill.storage.description",
                    Items.CHEST,
                    levelsBuilder()
                            .put(
                                    1,
                                    new SkillLevelDataGenSpec().withItemTags(ItemTags.BUNDLES)
                            )
                            .put(
                                    3,
                                    new SkillLevelDataGenSpec()
                                            .withItems(
                                                    Items.ITEM_FRAME,
                                                    Items.GLOW_ITEM_FRAME
                                            )
                                            .withEntityTypes(
                                                    EntityType.ITEM_FRAME,
                                                    EntityType.GLOW_ITEM_FRAME
                                            )
                            )
                            .put(5, new SkillLevelDataGenSpec()
                                    .withBlockTags(BlockTags.WOODEN_SHELVES)
                            )
                            .put(7, new SkillLevelDataGenSpec()
                                    .withBlocks(Blocks.BARREL)
                            )
                            .put(
                                    10,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(Blocks.CHEST, Blocks.TRAPPED_CHEST)
                                            .withBlockTags(BlockTags.COPPER_CHESTS)
                                            .withItems(Items.CHEST_MINECART)
                                            .withItemTags(ItemTags.CHEST_BOATS)
                                            .withEntityTypes(
                                                    EntityType.CHEST_MINECART,
                                                    EntityType.ACACIA_CHEST_BOAT,
                                                    EntityType.BIRCH_CHEST_BOAT,
                                                    EntityType.CHERRY_CHEST_BOAT,
                                                    EntityType.DARK_OAK_CHEST_BOAT,
                                                    EntityType.JUNGLE_CHEST_BOAT,
                                                    EntityType.MANGROVE_CHEST_BOAT,
                                                    EntityType.OAK_CHEST_BOAT,
                                                    EntityType.PALE_OAK_CHEST_BOAT,
                                                    EntityType.SPRUCE_CHEST_BOAT,
                                                    EntityType.BAMBOO_CHEST_RAFT
                                            )
                            )
                            .put(
                                    12,
                                    new SkillLevelDataGenSpec().withBlocks(Blocks.ENDER_CHEST)
                            )
                            .put(
                                    18,
                                    new SkillLevelDataGenSpec()
                                            .withBlockTags(BlockTags.SHULKER_BOXES)
                                            .withItems(Items.SHULKER_SHELL)
                            )
                            .build()
            ),
            // SURVIVAL
            new SkillDataGenSpec(
                    Identifier.of("skillmmo", "survival"),
                    "skillmmo.skill.survival.name",
                    "skillmmo.skill.survival.description",
                    Items.FLINT_AND_STEEL,
                    levelsBuilder()
                            .put(
                                    1,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.COPPER_TORCH,
                                                    Blocks.COPPER_WALL_TORCH,
                                                    Blocks.SOUL_TORCH,
                                                    Blocks.SOUL_WALL_TORCH
                                            )
                                            .withBlockTags(BlockTags.BEDS)
                            )
                            .put(
                                    2,
                                    new SkillLevelDataGenSpec().withItems(
                                            Items.BUCKET,
                                            Items.WATER_BUCKET,
                                            Items.LAVA_BUCKET,
                                            Items.MILK_BUCKET,
                                            Items.GLASS_BOTTLE
                                    )
                            )
                            .put(
                                    3,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(Blocks.LANTERN, Blocks.SOUL_LANTERN)
                                            .withBlockTags(BlockTags.CAMPFIRES)
                                            .withItemTags(ItemTags.BOATS)
                                            .withEntityTypes(
                                                    EntityType.ACACIA_BOAT,
                                                    EntityType.BIRCH_BOAT,
                                                    EntityType.CHERRY_BOAT,
                                                    EntityType.DARK_OAK_BOAT,
                                                    EntityType.JUNGLE_BOAT,
                                                    EntityType.MANGROVE_BOAT,
                                                    EntityType.OAK_BOAT,
                                                    EntityType.PALE_OAK_BOAT,
                                                    EntityType.SPRUCE_BOAT,
                                                    EntityType.BAMBOO_RAFT
                                            )
                            )
                            .put(
                                    4,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.ICE,
                                                    Blocks.PACKED_ICE,
                                                    Blocks.FROSTED_ICE,
                                                    Blocks.BLUE_ICE,
                                                    Blocks.POWDER_SNOW
                                            )
                                            .withItems(Items.POWDER_SNOW_BUCKET)
                            )
                            .put(
                                    5,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.FURNACE,
                                                    Blocks.CARTOGRAPHY_TABLE,
                                                    Blocks.FLETCHING_TABLE
                                            )
                                            .withItems(
                                                    Items.FILLED_MAP,
                                                    Items.FISHING_ROD,
                                                    Items.MAP,
                                                    Items.FILLED_MAP,
                                                    Items.NAME_TAG,
                                                    Items.BRUSH,
                                                    Items.SPYGLASS,
                                                    Items.FLINT_AND_STEEL,
                                                    Items.SHEARS,
                                                    Items.CHARCOAL,
                                                    Items.NAUTILUS_SHELL,
                                                    Items.HEART_OF_THE_SEA
                                            )
                            )
                            .put(
                                    6,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.SPAWNER,
                                                    Blocks.CREAKING_HEART,
                                                    Blocks.CLOSED_EYEBLOSSOM,
                                                    Blocks.OPEN_EYEBLOSSOM,
                                                    Blocks.RESIN_BLOCK,
                                                    Blocks.RESIN_CLUMP
                                            )
                                            .withItems(Items.RESIN_BRICK)
                                            .withEntityTypes(EntityType.SPAWNER_MINECART)
                            )
                            .put(
                                    8,
                                    new SkillLevelDataGenSpec().withItems(
                                            Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                                            Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE,
                                            Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE,
                                            Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE,
                                            Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE,
                                            Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE,
                                            Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE,
                                            Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE,
                                            Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE,
                                            Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE,
                                            Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE,
                                            Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE,
                                            Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE,
                                            Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE,
                                            Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE,
                                            Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE,
                                            Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE,
                                            Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE,
                                            Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE
                                    )
                            )
                            .put(
                                    10,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.TRIAL_SPAWNER,
                                                    Blocks.HEAVY_CORE,
                                                    Blocks.VAULT
                                            )
                                            .withItems(
                                                    Items.BREEZE_ROD,
                                                    Items.WIND_CHARGE,
                                                    Items.TRIAL_KEY,
                                                    Items.OMINOUS_BOTTLE,
                                                    Items.OMINOUS_TRIAL_KEY
                                            )
                            )
                            .put(
                                    12,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(
                                                    Blocks.PRISMARINE,
                                                    Blocks.PRISMARINE_BRICKS,
                                                    Blocks.DARK_PRISMARINE,
                                                    Blocks.SEA_LANTERN
                                            )
                                            .withItems(
                                                    Items.PRISMARINE_SHARD,
                                                    Items.PRISMARINE_CRYSTALS
                                            )
                            )
                            .put(
                                    15,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(Blocks.CONDUIT, Blocks.REINFORCED_DEEPSLATE)
                                            .withItems(Items.RECOVERY_COMPASS)
                            )
                            .build()
            ),
            // TRADING
            new SkillDataGenSpec(
                    Identifier.of("skillmmo", "trading"),
                    "skillmmo.skill.trading.name",
                    "skillmmo.skill.trading.description",
                    Items.EMERALD,
                    levelsBuilder()
                            .put(
                                    3,
                                    new SkillLevelDataGenSpec().withItems(Items.WRITABLE_BOOK)
                            )
                            .put(
                                    5,
                                    new SkillLevelDataGenSpec()
                                            .withBlocks(Blocks.BELL)
                                            .withEntityTypes(EntityType.VILLAGER)
                            )
                            .put(
                                    8,
                                    new SkillLevelDataGenSpec()
                                            .withEntityTypes(EntityType.WANDERING_TRADER)
                            )
                            .put(
                                    10,
                                    new SkillLevelDataGenSpec()
                                            .withEntityTypes(EntityType.PIGLIN)
                            )
                            .build()
            )
    );

    private static ImmutableMap.Builder<
            Integer,
            SkillLevelDataGenSpec
            > levelsBuilder() {
        return ImmutableMap.builder();
    }

    static {
        // TODO: validate all blocks, items, and entities are accounted for
    }
}
