package dev.nickrobson.minecraft.skillmmo.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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
        Map<Identifier, SkillData> skillsData = loadResources(manager, SkillMmoDataType.SKILLS);

        Map<Identifier, SkillData> skillDataBySkillId = new HashMap<>();
        skillsData.forEach((id, skillData) ->
                skillDataBySkillId.compute(id, (k, v) -> {
                    if (v == null || skillData.replace) {
                        return skillData;
                    }
                    if (skillData.enabled != null) {
                        v.enabled = skillData.enabled;
                    }
                    if (skillData.nameKey != null) {
                        v.nameKey = skillData.nameKey;
                    }
                    if (skillData.descriptionKey != null) {
                        v.descriptionKey = skillData.descriptionKey;
                    }
                    return v;
                }));

        Set<Skill> skills = skillDataBySkillId.entrySet()
                .stream()
                .filter(skillData -> skillData.getValue().enabled != Boolean.FALSE)
                .map(skillData ->
                        new Skill(
                                skillData.getKey(),
                                Text.translatable(skillData.getValue().nameKey),
                                Text.translatable(skillData.getValue().descriptionKey),
                                skillData.getValue().maxLevel,
                                skillData.getValue().icon.iconItem
                        ))
                .collect(Collectors.toUnmodifiableSet());

        SkillManager.getInstance().initSkills(skills);
    }

    private <T extends DataValidatable> Map<Identifier, T> loadResources(ResourceManager manager, SkillMmoDataType<T> type) {
        Map<Identifier, Resource> resourceMap = manager.findResources(
                type.getResourceCategory(),
                path -> path.getPath().endsWith(".json")
        );

        Map<Identifier, T> resourcesMap = new HashMap<>();
        boolean errored = false;
        for (Map.Entry<Identifier, Resource> resourceEntry : resourceMap.entrySet()) {
            Identifier resourceIdentifier = resourceEntry.getKey();
            Resource resource = resourceEntry.getValue();
            try (InputStreamReader resourceReader = new InputStreamReader(resource.getInputStream())) {
                T resourceValue = gson.fromJson(resourceReader, type.getResourceClass());
                Collection<String> errors = new ArrayList<>();
                resourceValue.validate(errors);
                if (errors.isEmpty()) {
                    Identifier resourceId = new Identifier(
                            resourceIdentifier.getNamespace(),
                            // e.g. skills/abc.json -> abc
                            resourceIdentifier.getPath().substring(type.getResourceCategory().length() + 1, resourceIdentifier.getPath().lastIndexOf("."))
                    );
                    resourcesMap.put(resourceId, resourceValue);
                } else {
                    logger.error(
                            "Failed to load resource '{}' for type '{}' due to errors:{}",
                            resourceIdentifier,
                            type.getResourceCategory(),
                            errors.stream().map("\n\t- %s"::formatted).collect(Collectors.joining())
                    );
                    errored = true;
                }
            } catch (Exception ex) {
                logger.error(new StringFormattedMessage("Failed to load resource '{}' for type '{}'", resourceIdentifier, type.getResourceCategory()).getFormattedMessage(), ex);
                errored = true;
            }
        }

        if (errored) {
            throw new IllegalStateException("Failed to start due to datapack validation errors! (See above)");
        }

        Set<Identifier> successfullyLoaded = new TreeSet<>(resourcesMap.keySet());
        logger.info("Loaded resources for {}: {}", type.getResourceCategory(), successfullyLoaded);

        return resourcesMap;
    }
}
