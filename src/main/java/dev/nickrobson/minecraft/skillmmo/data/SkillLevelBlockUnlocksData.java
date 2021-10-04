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
public class SkillLevelBlockUnlocksData extends AbstractSkillLevelUnlocksData {
    /**
     * The blocks that are unlocked when this skill level is acquired
     * This should be a list of block IDs
     */
    @SerializedName("blocks")
    public List<String> blocks;

    @Override
    public void validate(@Nonnull Collection<String> errors) {
        super.validate(errors);

        if (blocks == null) {
            errors.add("'blocks' is not defined");
        } else {
            blocks.removeIf(Objects::isNull);
            if (blocks.isEmpty()) {
                errors.add("No blocks have been set");
            } else {
                blocks = blocks.stream()
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList());
            }
        }
    }
}
