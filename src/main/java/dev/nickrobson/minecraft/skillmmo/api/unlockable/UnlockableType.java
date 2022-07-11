package dev.nickrobson.minecraft.skillmmo.api.unlockable;

import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public abstract class UnlockableType<Target> {
    private final Registry<Target> registry;

    public UnlockableType(Registry<Target> registry) {
        this.registry = registry;
    }

    public Registry<Target> getRegistry() {
        return registry;
    }

    public TagKey<Target> createTag(Identifier tagIdentifier) {
        return TagKey.of(registry.getKey(), tagIdentifier);
    }

    public Unlockable<Target> createUnlockable(Target targetType) {
        Identifier targetId = this.registry.getId(targetType);
        return new Unlockable<>(this, targetId);
    }

    public Unlockable<Target> createUnlockable(Identifier targetId) {
        return new Unlockable<>(this, targetId);
    }

    public Target getById(Identifier identifier) {
        return this.registry.get(identifier);
    }

    public Identifier getId(Target targetType) {
        return this.registry.getId(targetType);
    }

    public Text getName(Identifier identifier) {
        Target targetType = registry.get(identifier);
        return targetType == null ? null : getName(targetType);
    }

    protected abstract Text getName(Target target);
}
