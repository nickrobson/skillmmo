package dev.nickrobson.minecraft.skillmmo;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;

import dev.nickrobson.minecraft.skillmmo.api.unlockable.Unlockable;
import dev.nickrobson.minecraft.skillmmo.api.unlockable.VanillaUnlockables;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;

import static dev.nickrobson.minecraft.skillmmo.data.generation.spec.SkillMmoDefaultUngatedContent.UNGATED_THINGS;

public class SkillMmoDebugger {
    private static final Logger logger = LogManager.getLogger(SkillMmoDebugger.class);

    private SkillMmoDebugger() {
    }

    public static void printDebugInfo() {
        if (Boolean.getBoolean("skillmmo.debug.unassigned")) {
            printUnassignedUnlockables(BuiltInRegistries.BLOCK::stream, VanillaUnlockables::forBlock);
            printUnassignedUnlockables(BuiltInRegistries.ITEM::stream, VanillaUnlockables::forItem);
            printUnassignedUnlockables(BuiltInRegistries.ENTITY_TYPE::stream, VanillaUnlockables::forEntityType);
        }
    }

    private static <T> void printUnassignedUnlockables(Supplier<Stream<T>> getStream, Function<T, Unlockable<?>> toUnlockable) {
        getStream.get().forEach(thing -> {
            Unlockable<?> unlockable = toUnlockable.apply(thing);
            Registry<?> registry = unlockable.type().getRegistry();

            var ungatedThings = UNGATED_THINGS.get(registry);
            boolean isDefaultUnlockedByEntry = ungatedThings != null && ungatedThings.getA().contains(registry.getValue(unlockable.targetId()));
            //noinspection unchecked
            boolean isDefaultUnlockedByTag = ungatedThings != null && ungatedThings.getB().stream().anyMatch(
                    tag -> registry.get(unlockable.targetId()).orElseThrow().is((TagKey) tag)
            );
            boolean isDefaultUnlocked = isDefaultUnlockedByEntry || isDefaultUnlockedByTag;

            Set<Skill> skillSet = SkillManager.getInstance().getSkillsAffecting(unlockable);

            Identifier registryId = unlockable.type().getRegistry().key().identifier();
            Identifier thingId = unlockable.targetId();

            if (skillSet.isEmpty() && !isDefaultUnlocked) {
                logger.warn("Unassigned value '{}' in registry '{}' not in default unlocked list", thingId, registryId);
            } else if (!skillSet.isEmpty() && isDefaultUnlocked) {
                logger.warn("Default locked value '{}' in registry '{}' also present in default unlocked list", thingId, registryId);
            }
        });
    }
}
