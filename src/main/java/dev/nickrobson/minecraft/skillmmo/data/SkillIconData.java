package dev.nickrobson.minecraft.skillmmo.data;

import com.google.gson.annotations.SerializedName;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

/**
 * Data shape for a skill's icon in a datapack
 */
public class SkillIconData implements DataValidatable {
    /**
     * The type of the icon. Must be 'item'
     */
    @SerializedName("type")
    public String type;

    /**
     * The icon value. The format of this depends on what 'type' is set to.
     * For type 'item', this should be a Minecraft item identifier, like minecraft:stone or minecraft:egg.
     */
    @SerializedName("value")
    public String value;

    public transient Item iconItem;

    @Override
    public void validate(@NotNull Collection<String> errors) {
        if (type == null) {
            errors.add("'icon.type' should be set, should be set to 'item'");
        } else if ("item".equals(type)) {
            if (value == null) {
                errors.add("'icon.value' is not set, should be an item ID, e.g. minecraft:stone or minecraft:egg");
            } else {
                Identifier iconItemId = Identifier.tryParse(value);
                if (iconItemId == null) {
                    errors.add("'icon.value' is '%s', should be a valid identifier format, e.g. minecraft:stone or minecraft:egg".formatted(value));
                } else {
                    Optional<Item> iconItemOpt = Registries.ITEM.getOrEmpty(iconItemId);
                    if (iconItemOpt.isPresent()) {
                        iconItem = iconItemOpt.get();
                    } else {
                        errors.add("'icon.value' is '%s', which is not the ID of any item known to the game".formatted(iconItemId));
                    }
                }
            }
        } else {
            errors.add("'icon.type' is set to '%s' which is unsupported, should be set to 'item'".formatted(type));
        }
    }
}
