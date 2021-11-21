package dev.nickrobson.minecraft.skillmmo.skill.unlock;

import dev.nickrobson.minecraft.skillmmo.interaction.Interaction;
import dev.nickrobson.minecraft.skillmmo.interaction.InteractionType;
import net.minecraft.util.Identifier;

public record Unlock(
        UnlockType<?> unlockType,
        Identifier identifier
) {
    public Interaction toInteraction(InteractionType interactionType) {
        if (interactionType.unlockType() != unlockType) {
            throw new IllegalArgumentException("Provided interaction type is of wrong unlock type");
        }
        return new Interaction(interactionType, identifier);
    }
}
