package dev.nickrobson.minecraft.skillmmo;

import dev.nickrobson.minecraft.skillmmo.api.unlockable.Unlockable;
import dev.nickrobson.minecraft.skillmmo.api.unlockable.VanillaUnlockables;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static dev.nickrobson.minecraft.skillmmo.data.generation.spec.SkillMmoDefaultUngatedContent.UNGATED_THINGS;

public class SkillMmoDebugger {
    private static final Logger logger = LogManager.getLogger(SkillMmoDebugger.class);

    private SkillMmoDebugger() {
    }

    public static void printDebugInfo() {
        if (Boolean.getBoolean("skillmmo.debug.unassigned")) {
            printUnassignedUnlockables(Registries.BLOCK::stream, VanillaUnlockables::forBlock);
            printUnassignedUnlockables(Registries.ITEM::stream, VanillaUnlockables::forItem);
            printUnassignedUnlockables(Registries.ENTITY_TYPE::stream, VanillaUnlockables::forEntityType);
        }
    }

    private static <T> void printUnassignedUnlockables(Supplier<Stream<T>> getStream, Function<T, Unlockable<?>> toUnlockable) {
        getStream.get().forEach(thing -> {
            Unlockable<?> unlockable = toUnlockable.apply(thing);
            Registry<?> registry = unlockable.type().getRegistry();

            var ungatedThings = UNGATED_THINGS.get(registry);
            boolean isDefaultUnlockedByEntry = ungatedThings != null && ungatedThings.getLeft().contains(registry.get(unlockable.targetId()));
            //noinspection unchecked
            boolean isDefaultUnlockedByTag = ungatedThings != null && ungatedThings.getRight().stream().anyMatch(
                    tag -> registry.getEntry(unlockable.targetId()).orElseThrow().isIn((TagKey) tag)
            );
            boolean isDefaultUnlocked = isDefaultUnlockedByEntry || isDefaultUnlockedByTag;

            Set<Skill> skillSet = SkillManager.getInstance().getSkillsAffecting(unlockable);

            Identifier registryId = unlockable.type().getRegistry().getKey().getValue();
            Identifier thingId = unlockable.targetId();

            if (skillSet.isEmpty() && !isDefaultUnlocked) {
                logger.warn("Unassigned value '{}' in registry '{}' not in default unlocked list", thingId, registryId);
            } else if (!skillSet.isEmpty() && isDefaultUnlocked) {
                logger.warn("Default locked value '{}' in registry '{}' also present in default unlocked list", thingId, registryId);
            }
        });
    }
}
