package dev.nickrobson.minecraft.skillmmo.data;

import com.google.gson.annotations.SerializedName;
import net.minecraft.util.annotation.FieldsAreNonnullByDefault;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * Data shape for a skill in a datapack
 */
@FieldsAreNonnullByDefault
public class SkillData implements DataValidatable {
    private static final Pattern ID_REGEX = Pattern.compile("[A-Za-z0-9_]+");

    /**
     * Whether this skill definition should replace an existing skill with the same ID
     */
    @SerializedName("replace")
    public boolean replace = true;

    /**
     * The ID of this skill, e.g. "mining"
     * This should never be changed as it's used to save player data.
     */
    @SerializedName("id")
    public String id;

    /**
     * Whether this skill is enabled
     */
    @SerializedName("enabled")
    public boolean enabled = true;

    /**
     * Translation key for this skill's name
     */
    @SerializedName("translationKey")
    public String translationKey;

    @Override
    public void validate(@Nonnull Collection<String> errors) {
        if (id == null) {
            errors.add("'id' is not defined");
        } else if (!ID_REGEX.matcher(id).matches()) {
            errors.add(String.format("ID '%s' is invalid. must contain only A-Z, a-z, 0-9, or _", id));
        }

        if (translationKey == null) {
            errors.add("'translationKey' is not defined");
        }
    }
}
