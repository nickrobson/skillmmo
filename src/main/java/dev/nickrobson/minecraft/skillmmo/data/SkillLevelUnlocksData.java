package dev.nickrobson.minecraft.skillmmo.data;

import com.google.gson.annotations.SerializedName;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.UnlockType;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.FieldsAreNonnullByDefault;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Data shape for a skill level in a datapack
 */
@FieldsAreNonnullByDefault
public class SkillLevelUnlocksData implements DataValidatable {
    /**
     * Whether this unlocks definition replaces an existing set of unlocks of this type for the same skill level
     */
    @SerializedName("replace")
    public boolean replace = false;

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
    public int level;

    /**
     * The blocks that are unlocked when this skill level is acquired
     * This should be a list of block IDs
     */
    @SerializedName("blocks")
    public Set<String> rawBlockIds;

    public transient Set<Identifier> blockIds;

    /**
     * The items that are unlocked when this skill level is acquired
     * This should be a list of item IDs
     */
    @SerializedName("items")
    public Set<String> rawItemIds;

    public transient Set<Identifier> itemIds;
    // TODO: add support for tags?

    public transient Map<UnlockType, Set<Identifier>> identifiers;

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

        if (rawBlockIds == null && rawItemIds == null) {
            errors.add("Neither 'blocks' nor 'items' are defined");
        }

        if (rawBlockIds != null) {
            rawBlockIds.removeIf(Objects::isNull);
            blockIds = parseIdentifiers(rawBlockIds, errors);
            if (rawBlockIds.isEmpty()) {
                errors.add("No blocks have been set");
            }
        }

        if (rawItemIds != null) {
            rawItemIds.removeIf(Objects::isNull);
            itemIds = parseIdentifiers(rawItemIds, errors);
            if (itemIds.isEmpty()) {
                errors.add("No items have been set");
            }
        }

        Map<UnlockType, Set<Identifier>> identifiers = new HashMap<>();
        if (blockIds != null && !blockIds.isEmpty()) {
            identifiers.put(UnlockType.BLOCK, blockIds);
        }
        if (itemIds != null && !itemIds.isEmpty()) {
            identifiers.put(UnlockType.ITEM, itemIds);
        }
        this.identifiers = Collections.unmodifiableMap(identifiers);
    }

    @Nonnull
    private static Set<Identifier> parseIdentifiers(@Nonnull Set<String> ids, @Nonnull Collection<String> errors) {
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
