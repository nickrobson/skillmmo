package dev.nickrobson.minecraft.skillmmo.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.nickrobson.minecraft.skillmmo.data.entity.SkillData;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class SkillMmoResourceLoader implements SimpleSynchronousResourceReloadListener {
    private static final Logger logger = LogManager.getLogger(SkillMmoResourceLoader.class);

    private final Gson gson = new GsonBuilder().create();

    @Override
    public Identifier getFabricId() {
        return new Identifier("skillmmo", "skills");
    }

    @Override
    public void reload(ResourceManager manager) {
        Map<Identifier, SkillData> skillsDataById = loadResources(manager, SkillMmoDataType.SKILLS);
        Set<Skill> skills = skillsDataById.entrySet()
                .stream()
                .filter(skillData -> skillData.getValue().isEnabled())
                .map(skillData ->
                        new Skill(
                                skillData.getKey(),
                                Text.translatable(skillData.getValue().getNameKey()),
                                Text.translatable(skillData.getValue().getDescriptionKey()),
                                skillData.getValue().getMaxLevel(),
                                skillData.getValue().getIcon().getItem()
                        ))
                .collect(Collectors.toUnmodifiableSet());

        SkillManager.getInstance().initSkills(skills);
    }

    private <T> Map<Identifier, T> loadResources(ResourceManager manager, SkillMmoDataType<T> type) {
        Map<Identifier, List<Resource>> rawResourcesByIdMap = ResourceFinder.json(type.getResourceCategory()).findAllResources(manager);

        Map<Identifier, List<T>> parsedResourceMap = new HashMap<>();
        boolean errored = false;

        for (Map.Entry<Identifier, List<Resource>> rawResourcesEntry : rawResourcesByIdMap.entrySet()) {
            Identifier resourceIdentifier = new Identifier(
                    rawResourcesEntry.getKey().getNamespace(),
                    // e.g. skills/abc.json -> abc
                    rawResourcesEntry.getKey().getPath().substring(type.getResourceCategory().length() + 1, rawResourcesEntry.getKey().getPath().lastIndexOf("."))
            );
            for (Resource resource : rawResourcesEntry.getValue()) {
                try (BufferedReader resourceReader = resource.getReader()) {
                    DataResult<Pair<T, JsonElement>> parsedResourceValue = JsonOps.INSTANCE.withDecoder(type.getCodec())
                            .apply(gson.fromJson(resourceReader, JsonElement.class));

                    if (parsedResourceValue.error().isPresent()) {
                        logger.error(
                                "Failed to parse resource '{}' of type '{}' due to error: {}",
                                resourceIdentifier,
                                type.getResourceCategory(),
                                parsedResourceValue.error().get().message()
                        );
                        errored = true;
                        continue;
                    }

                    T parsedResult = parsedResourceValue.result().orElseThrow().getFirst();
                    parsedResourceMap.compute(resourceIdentifier, (k, v) -> {
                        List<T> results = v == null ? new ArrayList<>() : v;
                        results.add(parsedResult);
                        return results;
                    });
                } catch (Exception ex) {
                    logger.error("Failed to load resource '%s'".formatted(resourceIdentifier), ex);
                    errored = true;
                }
            }
        }

        if (errored) {
            throw new IllegalStateException("Failed to start due to datapack validation errors! (See logs above)");
        }

        Map<Identifier, T> finalResourceMap = new HashMap<>();
        for (Map.Entry<Identifier, List<T>> parsedResourceEntry : parsedResourceMap.entrySet()) {
            Identifier resourceIdentifier = parsedResourceEntry.getKey();
            List<T> resources = parsedResourceEntry.getValue();

            Either<Optional<T>, List<String>> mergedResult = type.getMerger().merge(resources);
            if (mergedResult.left().isPresent()) {
                logger.error(
                        "Failed to merge resources with ID '{}' due to errors:{}",
                        resourceIdentifier,
                        mergedResult.left().get().stream().map("\n\t- %s"::formatted).collect(Collectors.joining())
                );
                errored = true;
                continue;
            }

            Optional<T> finalValue = mergedResult.orThrow();
            if (finalValue.isEmpty()) {
                continue;
            }

            Collection<String> errors = new ArrayList<>();
            type.getValidator().validate(finalValue.get(), errors);
            if (errors.isEmpty()) {
                finalResourceMap.put(resourceIdentifier, finalValue.get());
            } else {
                logger.error(
                        "Failed to load resource '{}' due to errors:{}",
                        resourceIdentifier,
                        errors.stream().map("\n\t- %s"::formatted).collect(Collectors.joining())
                );
                errored = true;
            }
        }

        if (errored) {
            throw new IllegalStateException("Failed to start due to datapack validation errors! (See logs above)");
        }

        Set<Identifier> successfullyLoaded = new TreeSet<>(finalResourceMap.keySet());
        logger.info("Loaded resources for {}: {}", type.getResourceCategory(), successfullyLoaded);

        return finalResourceMap;
    }
}
