package dev.nickrobson.minecraft.skillmmo.api.interaction;

import dev.nickrobson.minecraft.skillmmo.api.unlockable.Unlockable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Objects;

/**
 * Represents an interaction with something
 *
 * @param <Target> the type of the class of thing, e.g. {@link net.minecraft.item.Item}, {@link net.minecraft.block.Block}, or {@link net.minecraft.entity.EntityType}
 */
public final class Interaction<Target> {
    private final InteractionType<Target> type;
    private final Identifier targetId;

    public Interaction(InteractionType<Target> type, Identifier targetId) {
        this.type = type;
        this.targetId = targetId;
    }

    /**
     * Gets the {@link InteractionType type} of interaction this is
     *
     * @return the interaction type
     */
    public InteractionType<Target> type() {
        return this.type;
    }

    /**
     * Gets the {@link Identifier identifier} of the thing this interaction is with
     *
     * @return the identifier of the target thing that is interacted with
     */
    public Identifier targetId() {
        return this.targetId;
    }

    /**
     * Creates an instance of {@link Unlockable} representing the same target as this interaction
     *
     * @return the created instance
     */
    public Unlockable<Target> toUnlockable() {
        return type.getUnlockableType().createUnlockable(targetId);
    }

    public Text getDenyText(Target target, Text requiredSkillName, int requiredSkillLevel) {
        return type.getDenyText(target, requiredSkillName, requiredSkillLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interaction<?> that = (Interaction<?>) o;
        return Objects.equals(type, that.type) && Objects.equals(targetId, that.targetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, targetId);
    }
}
