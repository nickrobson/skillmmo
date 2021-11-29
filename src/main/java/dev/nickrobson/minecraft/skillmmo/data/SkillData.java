package dev.nickrobson.minecraft.skillmmo.data;

import com.google.gson.annotations.SerializedName;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import net.minecraft.util.annotation.FieldsAreNonnullByDefault;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * Data shape for a skill in a datapack
 */
@FieldsAreNonnullByDefault
public class SkillData implements DataValidatable {
    /**
     * Whether this skill definition should replace an existing skill with the same ID
     */
    @SerializedName("replace")
    public boolean replace = false;

    /**
     * Whether this skill is enabled
     */
    @SerializedName("enabled")
    public Boolean enabled = true;

    /**
     * Translation key for this skill's name
     */
    @SerializedName("nameKey")
    public String nameKey;

    /**
     * Translation key for this skill's description
     */
    @SerializedName("descriptionKey")
    public String descriptionKey;

    /**
     * Maximum level this skill goes to (must be below the global level limit)
     *
     * @see Skill#MAX_LEVEL
     */
    @SerializedName("maxLevel")
    public int maxLevel;

    /**
     * The icon representing this skill in the Skills GUI
     */
    @SerializedName("icon")
    public SkillIconData icon;

    @Override
    public void validate(@Nonnull Collection<String> errors) {
        if (nameKey == null) {
            errors.add("'nameKey' is not defined");
        }

        if (descriptionKey == null) {
            errors.add("'descriptionKey' is not defined");
        }

        if (maxLevel <= Skill.MIN_LEVEL || maxLevel > Skill.MAX_LEVEL) {
            errors.add("'maxLevel' is %d, should be between %d and %d".formatted(maxLevel, Skill.MIN_LEVEL + 1, Skill.MAX_LEVEL));
        }

        if (icon == null) {
            errors.add("'icon' is not set, should be a JSON object with keys 'type' and 'value'");
        } else {
            icon.validate(errors);
        }
    }
}
