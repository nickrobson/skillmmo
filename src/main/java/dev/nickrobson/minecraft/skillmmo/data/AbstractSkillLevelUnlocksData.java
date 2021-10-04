package dev.nickrobson.minecraft.skillmmo.data;

import com.google.gson.annotations.SerializedName;
import net.minecraft.util.annotation.FieldsAreNonnullByDefault;

import javax.annotation.Nonnull;
import java.util.Collection;

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
    public String skillId;

    /**
     * The level number.
     * Please note that all levels from 1 to the maximum level for a skill MUST be defined.
     */
    @SerializedName("level")
    public byte level;

    @Override
    public void validate(@Nonnull Collection<String> errors) {
        if (skillId == null) {
            errors.add("'skill' is not defined. It should be set to the skill's ID.");
        }

        if (level < 1) {
            errors.add(String.format("Level is invalid: %d", level));
        }
    }
}
