package dev.nickrobson.minecraft.skillmmo.interaction;

import dev.nickrobson.minecraft.skillmmo.skill.unlock.Unlock;
import net.minecraft.util.Identifier;

public record Interaction(
        InteractionType interactionType,
        Identifier identifier
) {
    public Unlock toUnlock() {
        return new Unlock(interactionType.unlockType(), identifier);
    }
}
