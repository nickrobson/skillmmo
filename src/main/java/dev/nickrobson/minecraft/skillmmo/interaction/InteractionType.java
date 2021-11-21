package dev.nickrobson.minecraft.skillmmo.interaction;

import dev.nickrobson.minecraft.skillmmo.skill.unlock.UnlockType;

public enum InteractionType {
    BLOCK_BREAK(UnlockType.BLOCK),
    BLOCK_INTERACT(UnlockType.BLOCK),
    BLOCK_PLACE(UnlockType.BLOCK),
    ITEM_USE(UnlockType.ITEM),
    ENTITY_INTERACT(UnlockType.ENTITY_TYPE);

    private final UnlockType<?> unlockType;

    InteractionType(UnlockType<?> unlockType) {
        this.unlockType = unlockType;
    }

    public UnlockType<?> unlockType() {
        return unlockType;
    }
}
