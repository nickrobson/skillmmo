package dev.nickrobson.minecraft.skillmmo.data;

import com.google.gson.annotations.SerializedName;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.FieldsAreNonnullByDefault;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;

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
     * The item to use as the icon for display in the Skills GUI
     */
    @SerializedName("iconItem")
    public String rawIconItemId;

    public transient Item iconItem;

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

        if (rawIconItemId == null) {
            errors.add("'iconItem' is not set, should be an item ID, e.g. minecraft:stone or minecraft:egg");
        } else {
            Identifier iconItemId = Identifier.tryParse(rawIconItemId);
            if (iconItemId == null) {
                errors.add("'iconItem' is '%s', should be a valid identifier format, e.g. minecraft:stone or mineraft:egg".formatted(rawIconItemId));
            } else {
                Optional<Item> iconItemOpt = Registry.ITEM.getOrEmpty(iconItemId);
                if (iconItemOpt.isPresent()) {
                    iconItem = iconItemOpt.get();
                } else {
                    errors.add("'iconItem' is '%s', which is not the ID of any item known to the game".formatted(iconItemId));
                }
            }
        }
    }
}
