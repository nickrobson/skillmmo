package dev.nickrobson.minecraft.skillmmo.api.unlockable;

import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;

public abstract class UnlockableType<Target> {
    private final Registry<Target> registry;

    public UnlockableType(Registry<Target> registry) {
        this.registry = registry;
    }

    public Registry<Target> getRegistry() {
        return registry;
    }

    public TagKey<Target> createTag(Identifier tagIdentifier) {
        return TagKey.create(registry.key(), tagIdentifier);
    }

    public Unlockable<Target> createUnlockable(Target targetType) {
        Identifier targetId = this.registry.getKey(targetType);
        return new Unlockable<>(this, targetId);
    }

    public Unlockable<Target> createUnlockable(Identifier targetId) {
        return new Unlockable<>(this, targetId);
    }

    public Target getById(Identifier identifier) {
        return this.registry.getValue(identifier);
    }

    public Identifier getId(Target targetType) {
        return this.registry.getKey(targetType);
    }

    public Component getName(Identifier identifier) {
        Target targetType = registry.getValue(identifier);
        return targetType == null ? null : getName(targetType);
    }

    protected abstract Component getName(Target target);
}
