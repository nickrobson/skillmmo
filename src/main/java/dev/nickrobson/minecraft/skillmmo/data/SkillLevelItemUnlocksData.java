package dev.nickrobson.minecraft.skillmmo.data;

import com.google.gson.annotations.SerializedName;
import net.minecraft.util.annotation.FieldsAreNonnullByDefault;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Data shape for a skill level in a datapack
 */
@FieldsAreNonnullByDefault
public class SkillLevelItemUnlocksData extends AbstractSkillLevelUnlocksData {
    /**
     * The items that are unlocked when this skill level is acquired
     * This should be a list of item IDs
     */
    @SerializedName("items")
    public List<String> items;

    @Override
    public void validate(@Nonnull Collection<String> errors) {
        super.validate(errors);

        if (items == null) {
            errors.add("'items' is not defined");
        } else {
            items.removeIf(Objects::isNull);
            if (items.isEmpty()) {
                errors.add("No items have been set");
            } else {
                items = items.stream()
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList());
            }
        }
    }
}
