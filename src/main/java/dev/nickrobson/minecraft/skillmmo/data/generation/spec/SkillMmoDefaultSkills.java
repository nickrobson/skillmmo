package dev.nickrobson.minecraft.skillmmo.data.generation.spec;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;

import java.util.List;

public final class SkillMmoDefaultSkills {
    public static final List<SkillDataGenSpec> DEFAULT_SKILLS = List.of(
            // AGRICULTURE
            new SkillDataGenSpec(
                    Identifier.of("skillmmo", "agriculture"),
                    "skillmmo.skill.agriculture.name",
                    "skillmmo.skill.agriculture.description",
                    Items.WHEAT,
                    levelsBuilder()
                            .put(1, new SkillLevelDataGenSpec()
                                    .withBlocks(
                                            Blocks.OAK_SAPLING,
                                            Blocks.SPRUCE_SAPLING,
                                            Blocks.BIRCH_SAPLING,
                                            Blocks.JUNGLE_SAPLING,
                                            Blocks.ACACIA_SAPLING,
                                            Blocks.DARK_OAK_SAPLING,
                                            Blocks.CHERRY_SAPLING,
                                            Blocks.PINK_PETALS,
                                            Blocks.MANGROVE_PROPAGULE
                                    )
                                    .withItems(
                                            Items.WOODEN_AXE
                                    )
                            )
                            .put(2, new SkillLevelDataGenSpec()
                                    .withBlocks(
                                            Blocks.OAK_LEAVES,
                                            Blocks.SPRUCE_LEAVES,
                                            Blocks.BIRCH_LEAVES,
                                            Blocks.JUNGLE_LEAVES,
                                            Blocks.ACACIA_LEAVES,
                                            Blocks.DARK_OAK_LEAVES,
                                            Blocks.CHERRY_LEAVES,
                                            Blocks.MANGROVE_LEAVES,
                                            Blocks.MANGROVE_ROOTS,
                                            Blocks.MUDDY_MANGROVE_ROOTS,
                                            Blocks.PACKED_MUD
                                    )
                            )
                            .put(3, new SkillLevelDataGenSpec()
                                    .withBlocks(
                                            Blocks.WHEAT,
                                            Blocks.CARROTS,
                                            Blocks.POTATOES,
                                            Blocks.BEETROOTS,
                                            Blocks.COMPOSTER
                                    )
                                    .withItems(
                                            Items.WOODEN_HOE,
                                            Items.BONE_MEAL,
                                            Items.WHEAT_SEEDS,
                                            Items.CARROT,
                                            Items.POTATO,
                                            Items.BEETROOT_SEEDS
                                    )
                            )
                            .put(4, new SkillLevelDataGenSpec()
                                    .withItems(
                                            Items.STONE_AXE
                                    )
                            )
                            .put(5, new SkillLevelDataGenSpec()
                                    .withBlocks(
                                            Blocks.CAKE,
                                            Blocks.COCOA,
                                            Blocks.KELP,
                                            Blocks.KELP_PLANT,
                                            Blocks.DRIED_KELP_BLOCK
                                    )
                                    .withBlockTags(
                                            BlockTags.CANDLE_CAKES
                                    )
                                    .withItems(
                                            Items.COOKIE,
                                            Items.PAPER
                                    )
                            )
                            .put(6, new SkillLevelDataGenSpec()
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
                                            Items.PUMPKIN_SEEDS
                                    )
                            )
                            .put(7, new SkillLevelDataGenSpec()
                                    .withBlocks(
                                            Blocks.CACTUS
                                    )
                                    .withItems(
                                            Items.GOLDEN_AXE,
                                            Items.STONE_HOE
                                    )
                            )
                            .put(8, new SkillLevelDataGenSpec()
                                    .withBlocks(
                                            Blocks.SPONGE,
                                            Blocks.WET_SPONGE
                                    )
                            )
                            .put(9, new SkillLevelDataGenSpec()
                                    .withBlockTags(
                                            BlockTags.FLOWER_POTS
                                    )
                                    .withItems(
                                            Items.GOLDEN_HOE
                                    )
                            )
                            .put(10, new SkillLevelDataGenSpec()
                                    .withBlocks(
                                            Blocks.AZALEA,
                                            Blocks.FLOWERING_AZALEA,
                                            Blocks.AZALEA_LEAVES,
                                            Blocks.FLOWERING_AZALEA_LEAVES,
                                            Blocks.SPORE_BLOSSOM,
                                            Blocks.MOSS_CARPET,
                                            Blocks.MOSS_BLOCK,
                                            Blocks.BIG_DRIPLEAF,
                                            Blocks.BIG_DRIPLEAF_STEM,
                                            Blocks.SMALL_DRIPLEAF,
                                            Blocks.HANGING_ROOTS
                                    )
                                    .withItems(
                                            Items.IRON_AXE
                                    )
                            )
                            .put(11, new SkillLevelDataGenSpec()
                                    .withItems(
                                            Items.IRON_HOE
                                    )
                            )
                            .put(13, new SkillLevelDataGenSpec()
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
                            .put(14, new SkillLevelDataGenSpec()
                                    .withBlocks(
                                            Blocks.NETHER_WART,
                                            Blocks.NETHER_WART_BLOCK,
                                            Blocks.SHROOMLIGHT,
                                            Blocks.CRIMSON_STEM,
                                            Blocks.STRIPPED_CRIMSON_STEM,
                                            Blocks.CRIMSON_HYPHAE,
                                            Blocks.STRIPPED_CRIMSON_HYPHAE,
                                            Blocks.CRIMSON_NYLIUM,
                                            Blocks.CRIMSON_FUNGUS,
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
                                            Blocks.TWISTING_VINES,
                                            Blocks.TWISTING_VINES_PLANT,
                                            Blocks.POTTED_WARPED_FUNGUS,
                                            Blocks.POTTED_WARPED_ROOTS
                                    )
                            )
                            .put(16, new SkillLevelDataGenSpec()
                                    .withBlocks(
                                            Blocks.WITHER_ROSE
                                    )
                                    .withItems(
                                            Items.NETHERITE_AXE,
                                            Items.NETHERITE_HOE
                                    )
                            )
                            .put(17, new SkillLevelDataGenSpec()
                                    .withBlocks(
                                            Blocks.CHORUS_PLANT,
                                            Blocks.CHORUS_FLOWER
                                    )
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
                            .put(2, new SkillLevelDataGenSpec()
                                    .withItems(
                                            Items.EGG
                                    )
                                    .withEntityTypes(
                                            EntityType.BAT,
                                            EntityType.CHICKEN,
                                            EntityType.RABBIT
                                    )
                            )
                            .put(3, new SkillLevelDataGenSpec()
                                    .withBlockTags(
                                            BlockTags.WOOL
                                    )
                                    .withItems(
                                            Items.GOAT_HORN
                                    )
                                    .withEntityTypes(
                                            EntityType.GOAT,
                                            EntityType.PIG,
                                            EntityType.SHEEP
                                    )
                            )
                            .put(4, new SkillLevelDataGenSpec()

                                    .withEntityTypes(
                                            EntityType.COW,
                                            EntityType.MOOSHROOM
                                    )
                            )
                            .put(5, new SkillLevelDataGenSpec()
                                    .withBlocks(
                                            Blocks.BEE_NEST,
                                            Blocks.BEEHIVE,
                                            Blocks.HONEY_BLOCK,
                                            Blocks.HONEYCOMB_BLOCK
                                    )
                                    .withItems(
                                            Items.HONEYCOMB,
                                            Items.HONEY_BOTTLE
                                    )
                                    .withEntityTypes(
                                            EntityType.BEE
                                    )
                            )
                            .put(6, new SkillLevelDataGenSpec()
                                    .withBlocks(
                                            Blocks.PEARLESCENT_FROGLIGHT,
                                            Blocks.VERDANT_FROGLIGHT,
                                            Blocks.OCHRE_FROGLIGHT,
                                            Blocks.FROGSPAWN
                                    )
                                    .withItems(
                                            Items.TADPOLE_BUCKET,
                                            Items.ARMADILLO_SCUTE
                                    )
                                    .withEntityTypes(
                                            EntityType.FROG,
                                            EntityType.TADPOLE,
                                            EntityType.ARMADILLO
                                    )
                            )
                            .put(7, new SkillLevelDataGenSpec()
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
                            .put(8, new SkillLevelDataGenSpec()
                                    .withBlocks(
                                            Blocks.TURTLE_EGG
                                    )
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
                            .put(9, new SkillLevelDataGenSpec()

                                    .withEntityTypes(
                                            EntityType.LLAMA,
                                            EntityType.TRADER_LLAMA
                                    )
                            )
                            .put(10, new SkillLevelDataGenSpec()
                                    .withBlocks(
                                            Blocks.SMOKER
                                    )
                                    .withItems(
                                            Items.WOLF_ARMOR
                                    )
                                    .withEntityTypes(
                                            EntityType.CAT,
                                            EntityType.FOX,
                                            EntityType.OCELOT,
                                            EntityType.PARROT,
                                            EntityType.WOLF
                                    )
                            )
                            .put(12, new SkillLevelDataGenSpec()
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
                            .put(14, new SkillLevelDataGenSpec()
                                    .withEntityTypes(
                                            EntityType.PANDA,
                                            EntityType.POLAR_BEAR
                                    )
                            )
                            .put(15, new SkillLevelDataGenSpec()
                                    .withBlocks(
                                            Blocks.SNIFFER_EGG
                                    )
                                    .withItems(
                                            Items.SADDLE,
                                            Items.LEAD
                                    )
                                    .withEntityTypes(
                                            EntityType.STRIDER,
                                            EntityType.SNIFFER
                                    )
                            )
                            .build()
            ),
//            // BUILDING
//            new SkillDataGenSpec(),
//            // COMBAT
//            new SkillDataGenSpec(),
//            // ENGINEERING
//            new SkillDataGenSpec(),
//            // MINING
//            new SkillDataGenSpec(),
//            // SORCERY
//            new SkillDataGenSpec(),
//            // STORAGE
//            new SkillDataGenSpec(),
//            // SURVIVAL
//            new SkillDataGenSpec(),
            // TRADING
            new SkillDataGenSpec(
                    Identifier.of("skillmmo", "trading"),
                    "skillmmo.skill.trading.name",
                    "skillmmo.skill.trading.description",
                    Items.EMERALD,
                    levelsBuilder()
                            .put(3, new SkillLevelDataGenSpec()
                                    .withItems(
                                            Items.WRITABLE_BOOK
                                    )
                            )
                            .put(5, new SkillLevelDataGenSpec()
                                    .withBlocks(
                                            Blocks.BELL
                                    )
                                    .withEntityTypes(
                                            EntityType.VILLAGER
                                    )
                            )
                            .put(8, new SkillLevelDataGenSpec()
                                    .withEntityTypes(
                                            EntityType.WANDERING_TRADER
                                    )
                            )
                            .build()
            )
    );

    private static ImmutableMap.Builder<Integer, SkillLevelDataGenSpec> levelsBuilder() {
        return ImmutableMap.builder();
    }

    static {
        // TODO: validate all blocks, items, and entities are accounted for
    }
}
