package dev.nickrobson.minecraft.skillmmo.data.generation.spec;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Pair;

import java.util.List;
import java.util.Map;

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
public final class SkillMmoDefaultUngatedContent {
    private SkillMmoDefaultUngatedContent() {
    }

    public static final List<Block> UNGATED_BY_DEFAULT_BLOCKS = List.of(
            // Basic overworld blocks that are breakable/obtainable by hand
            Blocks.GRASS_BLOCK,
            Blocks.DIRT,
            Blocks.COARSE_DIRT,
            Blocks.ROOTED_DIRT,
            Blocks.MUD,
            Blocks.PODZOL,
            Blocks.MYCELIUM,
            Blocks.FARMLAND,
            Blocks.DIRT_PATH,
            Blocks.SAND,
            Blocks.RED_SAND,
            Blocks.SOUL_SAND,
            Blocks.SOUL_SOIL,
            Blocks.GRAVEL,
            Blocks.SUSPICIOUS_SAND, // as long as you have a brush
            Blocks.SUSPICIOUS_GRAVEL, // as long as you have a brush
            Blocks.CLAY,
            Blocks.SNOW,
            Blocks.SNOW_BLOCK,
            Blocks.COBWEB,

            // Plants
            Blocks.BROWN_MUSHROOM,
            Blocks.BROWN_MUSHROOM_BLOCK,
            Blocks.RED_MUSHROOM,
            Blocks.RED_MUSHROOM_BLOCK,
            Blocks.MUSHROOM_STEM,
            Blocks.NETHER_SPROUTS,
            Blocks.LEAF_LITTER,
            Blocks.BUSH,
            Blocks.FIREFLY_BUSH,
            Blocks.FERN,
            Blocks.LARGE_FERN,
            Blocks.DEAD_BUSH,
            Blocks.SHORT_GRASS,
            Blocks.SHORT_DRY_GRASS,
            Blocks.TALL_GRASS,
            Blocks.TALL_DRY_GRASS,
            Blocks.SEAGRASS,
            Blocks.TALL_SEAGRASS,
            Blocks.SUGAR_CANE,
            Blocks.BAMBOO,
            Blocks.BAMBOO_SAPLING,
            Blocks.VINE,
            Blocks.CAVE_VINES,
            Blocks.CAVE_VINES_PLANT,
            Blocks.GLOW_LICHEN,
            Blocks.LILY_PAD,
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
            Blocks.PEONY,
            Blocks.ROSE_BUSH,
            Blocks.PINK_PETALS,
            Blocks.WILDFLOWERS,
            Blocks.CACTUS_FLOWER,

            // Overworld wood (starting materials)
            Blocks.OAK_PLANKS,
            Blocks.SPRUCE_PLANKS,
            Blocks.BIRCH_PLANKS,
            Blocks.JUNGLE_PLANKS,
            Blocks.ACACIA_PLANKS,
            Blocks.DARK_OAK_PLANKS,
            Blocks.PALE_OAK_PLANKS,
            Blocks.MANGROVE_PLANKS,
            Blocks.BAMBOO_PLANKS,
            Blocks.CHERRY_PLANKS,

            // Basic items
            Blocks.CRAFTING_TABLE,
            Blocks.TORCH,
            Blocks.WALL_TORCH,

            // Unobtainable blocks
            Blocks.AIR,
            Blocks.WATER, // as long as you have a bucket you can get water
            Blocks.BUBBLE_COLUMN,
            Blocks.LAVA, // as long as you have a bucket you can get lava
            Blocks.FIRE,
            Blocks.SOUL_FIRE,
            Blocks.BEDROCK,
            Blocks.NETHER_PORTAL,
            Blocks.END_PORTAL,
            Blocks.END_GATEWAY,
            Blocks.COMMAND_BLOCK,
            Blocks.CHAIN_COMMAND_BLOCK,
            Blocks.REPEATING_COMMAND_BLOCK,

            // Technical blocks
            Blocks.LIGHT,
            Blocks.BARRIER,
            Blocks.STRUCTURE_BLOCK,
            Blocks.STRUCTURE_VOID,
            Blocks.JIGSAW,
            Blocks.TEST_BLOCK,
            Blocks.TEST_INSTANCE_BLOCK
    );

    public static final List<TagKey<Block>> UNGATED_BY_DEFAULT_BLOCK_TAGS = List.of(
            // Overworld wood (starting materials)
            BlockTags.DARK_OAK_LOGS,
            BlockTags.PALE_OAK_LOGS,
            BlockTags.OAK_LOGS,
            BlockTags.ACACIA_LOGS,
            BlockTags.BIRCH_LOGS,
            BlockTags.JUNGLE_LOGS,
            BlockTags.SPRUCE_LOGS,
            BlockTags.MANGROVE_LOGS,
            BlockTags.CHERRY_LOGS,
            BlockTags.BAMBOO_BLOCKS,

            // Basic interactive blocks (we don't want to block the player navigating)
            BlockTags.WOODEN_DOORS,
            BlockTags.WOODEN_TRAPDOORS,
            BlockTags.FENCE_GATES,

            // Unobtainable blocks
            BlockTags.AIR
    );

    public static final List<Item> UNGATED_BY_DEFAULT_ITEMS = List.of(
            // Basic overworld blocks that are breakable/obtainable by hand
            Items.FLINT,
            Items.CLAY_BALL,
            Items.SNOWBALL,
            Items.POISONOUS_POTATO,

            // Overworld wood (starting materials)
            Items.STICK,

            // Starting food
            Items.APPLE,
            Items.BOWL,
            Items.MUSHROOM_STEW,
            Items.PORKCHOP,
            Items.COOKED_PORKCHOP,
            Items.BEEF,
            Items.COOKED_BEEF,
            Items.MUTTON,
            Items.COOKED_MUTTON,
            Items.CHICKEN,
            Items.COOKED_CHICKEN,
            Items.RABBIT,
            Items.COOKED_RABBIT,
            Items.RABBIT_STEW,
            Items.COD,
            Items.COOKED_COD,
            Items.SALMON,
            Items.COOKED_SALMON,
            Items.TROPICAL_FISH,
            Items.PUFFERFISH,
            Items.SUSPICIOUS_STEW,

            // Mob drops
            Items.FEATHER,
            Items.ROTTEN_FLESH,
            Items.BONE,
            Items.GUNPOWDER,
            Items.SPIDER_EYE,
            Items.STRING,
            Items.LEATHER,
            Items.RABBIT_FOOT,
            Items.RABBIT_HIDE,
            Items.INK_SAC,

            // Unobtainable items
            Items.AIR,
            Items.COMMAND_BLOCK_MINECART,
            Items.KNOWLEDGE_BOOK,
            Items.DEBUG_STICK,

            // Spawn eggs
            Items.ARMADILLO_SPAWN_EGG,
            Items.ALLAY_SPAWN_EGG,
            Items.AXOLOTL_SPAWN_EGG,
            Items.BAT_SPAWN_EGG,
            Items.BEE_SPAWN_EGG,
            Items.BLAZE_SPAWN_EGG,
            Items.BOGGED_SPAWN_EGG,
            Items.BREEZE_SPAWN_EGG,
            Items.CAT_SPAWN_EGG,
            Items.CAMEL_SPAWN_EGG,
            Items.CAVE_SPIDER_SPAWN_EGG,
            Items.CHICKEN_SPAWN_EGG,
            Items.COD_SPAWN_EGG,
            Items.COPPER_GOLEM_SPAWN_EGG,
            Items.COW_SPAWN_EGG,
            Items.CREEPER_SPAWN_EGG,
            Items.DOLPHIN_SPAWN_EGG,
            Items.DONKEY_SPAWN_EGG,
            Items.DROWNED_SPAWN_EGG,
            Items.ELDER_GUARDIAN_SPAWN_EGG,
            Items.ENDER_DRAGON_SPAWN_EGG,
            Items.ENDERMAN_SPAWN_EGG,
            Items.ENDERMITE_SPAWN_EGG,
            Items.EVOKER_SPAWN_EGG,
            Items.FOX_SPAWN_EGG,
            Items.FROG_SPAWN_EGG,
            Items.GHAST_SPAWN_EGG,
            Items.HAPPY_GHAST_SPAWN_EGG,
            Items.GLOW_SQUID_SPAWN_EGG,
            Items.GOAT_SPAWN_EGG,
            Items.GUARDIAN_SPAWN_EGG,
            Items.HOGLIN_SPAWN_EGG,
            Items.HORSE_SPAWN_EGG,
            Items.HUSK_SPAWN_EGG,
            Items.IRON_GOLEM_SPAWN_EGG,
            Items.LLAMA_SPAWN_EGG,
            Items.MAGMA_CUBE_SPAWN_EGG,
            Items.MOOSHROOM_SPAWN_EGG,
            Items.MULE_SPAWN_EGG,
            Items.OCELOT_SPAWN_EGG,
            Items.PANDA_SPAWN_EGG,
            Items.PARROT_SPAWN_EGG,
            Items.PHANTOM_SPAWN_EGG,
            Items.PIG_SPAWN_EGG,
            Items.PIGLIN_SPAWN_EGG,
            Items.PIGLIN_BRUTE_SPAWN_EGG,
            Items.PILLAGER_SPAWN_EGG,
            Items.POLAR_BEAR_SPAWN_EGG,
            Items.PUFFERFISH_SPAWN_EGG,
            Items.RABBIT_SPAWN_EGG,
            Items.RAVAGER_SPAWN_EGG,
            Items.SALMON_SPAWN_EGG,
            Items.SHEEP_SPAWN_EGG,
            Items.SHULKER_SPAWN_EGG,
            Items.SILVERFISH_SPAWN_EGG,
            Items.SKELETON_SPAWN_EGG,
            Items.SKELETON_HORSE_SPAWN_EGG,
            Items.SLIME_SPAWN_EGG,
            Items.SNIFFER_SPAWN_EGG,
            Items.SNOW_GOLEM_SPAWN_EGG,
            Items.SPIDER_SPAWN_EGG,
            Items.SQUID_SPAWN_EGG,
            Items.STRAY_SPAWN_EGG,
            Items.STRIDER_SPAWN_EGG,
            Items.TADPOLE_SPAWN_EGG,
            Items.TRADER_LLAMA_SPAWN_EGG,
            Items.TROPICAL_FISH_SPAWN_EGG,
            Items.TURTLE_SPAWN_EGG,
            Items.VEX_SPAWN_EGG,
            Items.VILLAGER_SPAWN_EGG,
            Items.VINDICATOR_SPAWN_EGG,
            Items.WANDERING_TRADER_SPAWN_EGG,
            Items.WARDEN_SPAWN_EGG,
            Items.WITCH_SPAWN_EGG,
            Items.WITHER_SPAWN_EGG,
            Items.WITHER_SKELETON_SPAWN_EGG,
            Items.WOLF_SPAWN_EGG,
            Items.ZOGLIN_SPAWN_EGG,
            Items.CREAKING_SPAWN_EGG,
            Items.ZOMBIE_SPAWN_EGG,
            Items.ZOMBIE_HORSE_SPAWN_EGG,
            Items.ZOMBIE_VILLAGER_SPAWN_EGG,
            Items.ZOMBIFIED_PIGLIN_SPAWN_EGG
    );

    public static final List<TagKey<Item>> UNGATED_BY_DEFAULT_ITEM_TAGS = List.of();

    public static final List<EntityType<?>> UNGATED_BY_DEFAULT_ENTITY_TYPES = List.of(
            // Projectiles
            EntityType.EGG,
            EntityType.SNOWBALL,
            EntityType.BREEZE_WIND_CHARGE,
            EntityType.ARROW,
            EntityType.SPECTRAL_ARROW,
            EntityType.TRIDENT,
            EntityType.WIND_CHARGE,
            EntityType.FISHING_BOBBER,
            EntityType.ENDER_PEARL,
            EntityType.EYE_OF_ENDER,
            EntityType.LINGERING_POTION,
            EntityType.SPLASH_POTION,
            EntityType.FIREBALL,
            EntityType.SMALL_FIREBALL,
            EntityType.DRAGON_FIREBALL,
            EntityType.EXPERIENCE_BOTTLE,
            EntityType.FIREWORK_ROCKET,
            EntityType.SHULKER_BULLET,
            EntityType.WITHER_SKULL,
            EntityType.TNT,
            EntityType.LLAMA_SPIT,

            // Non-interactive mobs
            EntityType.BLAZE,
            EntityType.BOGGED,
            EntityType.BREEZE,
            EntityType.CAVE_SPIDER,
            EntityType.CREAKING,
            EntityType.CREEPER,
            EntityType.DROWNED,
            EntityType.ENDERMAN,
            EntityType.ENDERMITE,
            EntityType.EVOKER,
            EntityType.EVOKER_FANGS,
            EntityType.GHAST,
            EntityType.GIANT,
            EntityType.GUARDIAN,
            EntityType.ELDER_GUARDIAN,
            EntityType.HOGLIN,
            EntityType.HUSK,
            EntityType.ILLUSIONER,
            EntityType.MAGMA_CUBE,
            EntityType.PHANTOM,
            EntityType.PIGLIN_BRUTE,
            EntityType.PILLAGER,
            EntityType.RAVAGER,
            EntityType.SHULKER,
            EntityType.SILVERFISH,
            EntityType.SKELETON,
            EntityType.SLIME,
            EntityType.SPIDER,
            EntityType.STRAY,
            EntityType.VEX,
            EntityType.VINDICATOR,
            EntityType.WITCH,
            EntityType.WITHER,
            EntityType.WITHER_SKELETON,
            EntityType.ZOGLIN,
            EntityType.ZOMBIE,
            EntityType.ZOMBIFIED_PIGLIN,

            // Non-interactive gameplay
            EntityType.EXPERIENCE_ORB,
            EntityType.FALLING_BLOCK,
            EntityType.ITEM,
            EntityType.LIGHTNING_BOLT,
            EntityType.OMINOUS_ITEM_SPAWNER, // non-interactive item display for a trial spawner, which is gated
            EntityType.PLAYER,

            // Unobtainable in survival
            EntityType.COMMAND_BLOCK_MINECART,
            EntityType.MANNEQUIN,
            EntityType.MARKER,

            // Technical
            EntityType.BLOCK_DISPLAY,
            EntityType.ITEM_DISPLAY,
            EntityType.TEXT_DISPLAY,
            EntityType.INTERACTION,

            // Special
            EntityType.AREA_EFFECT_CLOUD // lingering potions + there is special handling for obtaining dragon's breath from this
    );

    public static final List<TagKey<EntityType<?>>> UNGATED_BY_DEFAULT_ENTITY_TYPE_TAGS = List.of(
    );

    public static final Map<Registry<?>, Pair<List<?>, List<? extends TagKey<?>>>> UNGATED_THINGS = Map.of(
            Registries.BLOCK, new Pair<>(UNGATED_BY_DEFAULT_BLOCKS, UNGATED_BY_DEFAULT_BLOCK_TAGS),
            Registries.ITEM, new Pair<>(UNGATED_BY_DEFAULT_ITEMS, UNGATED_BY_DEFAULT_ITEM_TAGS),
            Registries.ENTITY_TYPE, new Pair<>(UNGATED_BY_DEFAULT_ENTITY_TYPES, UNGATED_BY_DEFAULT_ENTITY_TYPE_TAGS)
    );
}
