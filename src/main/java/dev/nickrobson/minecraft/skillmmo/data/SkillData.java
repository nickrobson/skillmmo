package dev.nickrobson.minecraft.skillmmo.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import net.minecraft.util.annotation.FieldsAreNonnullByDefault;

import java.util.Optional;

/**
 * Data shape for a skill in a datapack
 */
@FieldsAreNonnullByDefault
public record SkillData(
        /*
         * Whether this skill definition should replace an existing skill with the same ID
         */
        boolean replace,

        /*
          Whether this skill is enabled
         */
        Optional<Boolean> enabled,

        /*
         * Translation key for this skill's name
         */
        Optional<String> nameKey,

        /*
         * Translation key for this skill's description
         */
        Optional<String> descriptionKey,

        /*
         * Maximum level this skill goes to (must be below the global level limit), see Skill#MAX_LEVEL
         */
        Optional<Integer> maxLevel,

        /*
         * The icon representing this skill in the Skills GUI
         */
        Optional<SkillIconData> icon
) {
    public static final Codec<SkillData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("replace", false).forGetter(SkillData::replace),
            Codec.BOOL.optionalFieldOf("enabled").forGetter(SkillData::enabled),
            Codec.STRING.optionalFieldOf("nameKey").forGetter(SkillData::nameKey),
            Codec.STRING.optionalFieldOf("descriptionKey").forGetter(SkillData::descriptionKey),
            Codec.intRange(Skill.MIN_LEVEL + 1, Skill.MAX_LEVEL).optionalFieldOf("maxLevel").forGetter(SkillData::maxLevel),
            SkillIconData.CODEC.optionalFieldOf("icon").forGetter(SkillData::icon)
    ).apply(instance, SkillData::new));
}
