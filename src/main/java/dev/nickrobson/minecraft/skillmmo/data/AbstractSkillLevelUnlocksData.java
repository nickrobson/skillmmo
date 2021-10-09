package dev.nickrobson.minecraft.skillmmo.data;

import com.google.gson.annotations.SerializedName;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevelUnlockType;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.FieldsAreNonnullByDefault;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Generic data shape for unlocks for a skill level in a datapack
 *
 * @see SkillLevelBlockUnlocksData
 */
@FieldsAreNonnullByDefault
public abstract class AbstractSkillLevelUnlocksData implements DataValidatable {
    /**
     * Whether this unlocks definition replaces an existing set of unlocks of this type for the same skill level
     */
    @SerializedName("replace")
    public boolean replace = true;

    /**
     * The ID of the skill, e.g. "mining"
     */
    @SerializedName("skill")
    public String rawSkillId;

    // The parsed skill ID as an identifier
    public transient Identifier skillId;

    /**
     * The level number.
     * Please note that all levels from 1 to the maximum level for a skill MUST be defined.
     */
    @SerializedName("level")
    public byte level;

    @Override
    public void validate(@Nonnull Collection<String> errors) {
        if (rawSkillId == null) {
            errors.add("'skill' is not defined. It should be set to the skill's ID.");
        } else {
            this.skillId = Identifier.tryParse(rawSkillId);
            if (this.skillId == null) {
                errors.add(String.format("Skill ID '%s' is invalid", rawSkillId));
            }
        }

        if (level < 1) {
            errors.add(String.format("Level is invalid: %d", level));
        }
    }

    /**
     * Gets the type of unlock this data represents
     * @return the unlock type
     */
    public abstract SkillLevelUnlockType getUnlockType();

    /**
     * Gets the list of unlocks added by this list, as raw strings
     * Each item in the list should be in the identifier form, e.g. minecraft:stone
     * @return the set of raw unlock identifiers
     */
    public abstract Set<Identifier> getIdentifiers();

    @Nonnull
    protected final Set<Identifier> parseIdentifiers(@Nonnull Set<String> ids, @Nonnull Collection<String> errors) {
        return ids.stream()
                .map(id -> {
                    Identifier identifier = Identifier.tryParse(id);
                    if (identifier == null) {
                        errors.add(String.format("Invalid ID '%s', should be an identifier", id));
                    }
                    return identifier;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
