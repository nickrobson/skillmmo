package dev.nickrobson.minecraft.skillmmo.api.unlockable;

import net.minecraft.util.Identifier;

import java.util.Objects;

/**
 * Represents a thing that is or can be unlocked
 *
 * @param <Target> the type of the class of thing, e.g. {@link net.minecraft.item.Item}, {@link net.minecraft.block.Block}, or {@link net.minecraft.entity.EntityType}
 */
public final class Unlockable<Target> {
    private final UnlockableType<Target> type;
    private final Identifier targetId;

    public Unlockable(UnlockableType<Target> type, Identifier targetId) {
        this.type = type;
        this.targetId = targetId;
    }

    /**
     * Gets the {@link UnlockableType type} of unlock this is
     *
     * @return the type of unlockable this is
     */
    public UnlockableType<Target> type() {
        return this.type;
    }

    /**
     * Gets the {@link Identifier identifier} of the thing this Unlock contains
     * @return the identifier of the target thing that is or can be unlocked
     */
    public Identifier targetId() {
        return targetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unlockable<?> that = (Unlockable<?>) o;
        return Objects.equals(type, that.type) && Objects.equals(targetId, that.targetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, targetId);
    }
}
