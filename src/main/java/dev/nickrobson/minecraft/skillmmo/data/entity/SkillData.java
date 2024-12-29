package dev.nickrobson.minecraft.skillmmo.data.entity;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.nickrobson.minecraft.skillmmo.data.framework.SkillMmoDataMerger;
import dev.nickrobson.minecraft.skillmmo.data.framework.SkillMmoDataValidator;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Data shape for a skill in a datapack
 */
public class SkillData {
    public static final Codec<SkillData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("replace", false).forGetter(it -> it.replace),
            Codec.BOOL.optionalFieldOf("enabled", true).forGetter(it -> it.enabled),
            Codec.STRING.optionalFieldOf("nameKey").forGetter(it -> Optional.ofNullable(it.nameKey)),
            Codec.STRING.optionalFieldOf("descriptionKey").forGetter(it -> Optional.ofNullable(it.descriptionKey)),
            Codec.intRange(Skill.MIN_LEVEL + 1, Skill.MAX_LEVEL).optionalFieldOf("maxLevel").forGetter(it -> Optional.ofNullable(it.maxLevel)),
            SkillIconData.CODEC.optionalFieldOf("icon").forGetter(it -> Optional.ofNullable(it.icon))
    ).apply(instance, (replace, enabled, nameKey, descriptionKey, maxLevel, icon) -> {
        return new SkillData(
                replace,
                enabled,
                nameKey.orElse(null),
                descriptionKey.orElse(null),
                maxLevel.orElse(null),
                icon.orElse(null)
        );
    }));

    public static final SkillMmoDataValidator<SkillData> VALIDATOR = (data, errors) -> {
        if (data.nameKey == null) {
            errors.add("'nameKey' is not defined");
        }

        if (data.descriptionKey == null) {
            errors.add("'descriptionKey' is not defined");
        }

        if (data.maxLevel == null || data.maxLevel <= Skill.MIN_LEVEL || data.maxLevel > Skill.MAX_LEVEL) {
            errors.add("'maxLevel' is %d, should be between %d and %d".formatted(data.maxLevel, Skill.MIN_LEVEL + 1, Skill.MAX_LEVEL));
        }

        if (data.icon == null) {
            errors.add("'icon' is not set, should be a JSON object with keys 'type' and 'value'");
        }
    };

    public static final SkillMmoDataMerger<SkillData> MERGER = (skillData) -> {
        List<SkillData> nonReplacing = skillData.stream().filter(Predicate.not(SkillData::isReplacing)).toList();
        if (nonReplacing.isEmpty()) {
            return Either.right(Collections.singletonList("No original versions found"));
        } else if (nonReplacing.size() > 1) {
            return Either.right(Collections.singletonList("Too many (%d) versions found".formatted(nonReplacing.size())));
        }

        SkillData original = nonReplacing.get(0);
        List<String> originalErrors = new ArrayList<>();
        VALIDATOR.validate(original, originalErrors);
        if (!originalErrors.isEmpty()) {
            return Either.right(originalErrors);
        }

        SkillData combinedSkillData = skillData.stream().filter(SkillData::isReplacing).reduce(original, (acc, override) -> {
            return new SkillData(
                    false,
                    acc.enabled && override.enabled,
                    override.nameKey == null ? acc.nameKey : override.nameKey,
                    override.descriptionKey == null ? acc.descriptionKey : override.descriptionKey,
                    override.maxLevel == null ? acc.maxLevel : override.maxLevel,
                    override.icon == null ? acc.icon : override.icon
            );
        });

        return Either.left(Optional.of(combinedSkillData));
    };

    /**
     * Whether this skill definition should replace an existing skill with the same ID
     */
    private final boolean replace;

    /**
     * Whether this skill is enabled
     */
    private final boolean enabled;

    /**
     * Translation key for this skill's name
     */
    private final String nameKey;

    /**
     * Translation key for this skill's description
     */
    private final String descriptionKey;

    /**
     * Maximum level this skill goes to (must be below the global level limit)
     *
     * @see Skill#MAX_LEVEL
     */
    private final Integer maxLevel;

    /**
     * The icon representing this skill in the Skills GUI
     */
    private final SkillIconData icon;

    public SkillData(boolean replace, boolean enabled, @Nullable String nameKey, @Nullable String descriptionKey, @Nullable Integer maxLevel, @Nullable SkillIconData icon) {
        this.replace = replace;
        this.enabled = enabled;
        this.nameKey = nameKey;
        this.descriptionKey = descriptionKey;
        this.maxLevel = maxLevel;
        this.icon = icon;
    }

    public boolean isReplacing() {
        return replace;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getNameKey() {
        return nameKey;
    }

    public String getDescriptionKey() {
        return descriptionKey;
    }

    public int getMaxLevel() {
        Objects.requireNonNull(maxLevel, "skill max level");
        return maxLevel;
    }

    public SkillIconData getIcon() {
        return icon;
    }
}
