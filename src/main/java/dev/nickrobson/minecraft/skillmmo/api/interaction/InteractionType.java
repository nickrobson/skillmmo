package dev.nickrobson.minecraft.skillmmo.api.interaction;

import dev.nickrobson.minecraft.skillmmo.api.unlockable.UnlockableType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public abstract class InteractionType<Target> {
    private final UnlockableType<Target> unlockableType;

    InteractionType(UnlockableType<Target> unlockableType) {
        this.unlockableType = unlockableType;
    }

    public UnlockableType<Target> getUnlockableType() {
        return unlockableType;
    }

    public Interaction<Target> createInteraction(Target target) {
        Identifier targetId = getUnlockableType().getId(target);
        return new Interaction<>(this, targetId);
    }

    public abstract Component getDenyText(Target target, Component requiredSkillName, int requiredSkillLevel);
}
