package dev.nickrobson.minecraft.skillmmo.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevel;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevelUnlockType;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SkillMmoResourceLoader implements SimpleSynchronousResourceReloadListener {
    private static final Logger logger = LoggerFactory.getLogger(SkillMmoResourceLoader.class);

    private final Gson gson = new GsonBuilder().create();

    @Override
    public Identifier getFabricId() {
        return new Identifier("skillmmo", "skills");
    }

    @Override
    public void reload(ResourceManager manager) {
        List<SkillData> skillsData = loadResources(manager, SkillMmoDataType.SKILLS);
        List<SkillLevelUnlocksData> skillUnlocksData = loadResources(manager, SkillMmoDataType.SKILL_UNLOCKS);

        Map<Identifier, Set<SkillLevel>> skillLevelsBySkill = loadSkillLevels(skillUnlocksData);

        Map<Identifier, SkillData> skillDataBySkillId = new HashMap<>();
        skillsData.forEach((skillData) ->
                skillDataBySkillId.compute(skillData.id, (k, v) -> {
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
        Set<Skill> skills = skillDataBySkillId.values()
                .stream()
                .filter(skillData -> skillData.enabled == null || skillData.enabled)
                .map(skillData -> {
                    Set<SkillLevel> skillLevels = skillLevelsBySkill.get(skillData.id);
                    if (skillLevels == null || skillLevels.isEmpty()) {
                        return null;
                    }

                    return new Skill(
                            skillData.id,
                            skillData.nameKey,
                            skillData.descriptionKey,
                            skillLevels
                    );
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        SkillManager.getInstance().initSkills(skills);
    }

    private Map<Identifier, Set<SkillLevel>> loadSkillLevels(List<SkillLevelUnlocksData> skillLevelUnlocksData) {
        Map<Identifier, List<SkillLevelUnlocksData>> unlocksDataBySkillId = skillLevelUnlocksData
                .stream()
                .collect(Collectors.groupingBy(data -> data.skillId, Collectors.toList()));

        Map<Identifier, Set<SkillLevel>> skillLevelsBySkillId = new HashMap<>();
        unlocksDataBySkillId.forEach((skillId, unlocksDataForSkill) -> {
            Map<Integer, List<SkillLevelUnlocksData>> unlocksDataByLevel = unlocksDataForSkill
                    .stream()
                    .collect(Collectors.groupingBy(data -> data.level, Collectors.toList()));

            Map<Integer, SkillLevel> skillLevelsByLevel = new HashMap<>();
            unlocksDataByLevel.forEach((level, unlocksDataForSkillLevel) -> {
                Map<SkillLevelUnlockType, Set<Identifier>> unlockIdentifiersByUnlockType = new HashMap<>();
                unlocksDataForSkillLevel.forEach(unlockData -> {
                    if (unlockData.replace) {
                        unlockIdentifiersByUnlockType.clear();
                    }

                    Map<SkillLevelUnlockType, Set<Identifier>> levelUnlocks = unlockData.getIdentifiers();
                    levelUnlocks.forEach((unlockType, identifiers) ->
                            unlockIdentifiersByUnlockType.compute(unlockType, (k, v) -> {
                                if (v == null || unlockData.replace) {
                                    v = new HashSet<>();
                                }
                                v.addAll(identifiers);
                                return v;
                            }));
                });

                skillLevelsByLevel.put(
                        level, new SkillLevel(skillId, level, unlockIdentifiersByUnlockType)
                );
            });

            skillLevelsBySkillId.put(skillId, new HashSet<>(skillLevelsByLevel.values()));
        });

        return skillLevelsBySkillId;
    }

    private <T extends DataValidatable> List<T> loadResources(ResourceManager manager, SkillMmoDataType<T> type) {
        Collection<Identifier> resourceIdentifiers = manager.findResources(
                type.getResourceCategory(),
                path -> path.endsWith(".json")
        );

        List<T> unlocksList = new ArrayList<>();
        for (Identifier resourceIdentifier : resourceIdentifiers) {
            try (InputStreamReader resourceReader = new InputStreamReader(manager.getResource(resourceIdentifier).getInputStream())) {
                T unlocks = gson.fromJson(resourceReader, type.getResourceClass());
                Collection<String> errors = new ArrayList<>();
                unlocks.validate(errors);
                if (errors.isEmpty()) {
                    unlocksList.add(unlocks);
                    logger.info("Loaded resource for {}: '{}'", type.getResourceCategory(), resourceIdentifier);
                } else {
                    logger.warn("Ignoring resource '{}' for {} due to errors:\n\t- {}", resourceIdentifier, type.getResourceCategory(), String.join("\n\t- ", errors));
                }
            } catch (Exception ex) {
                logger.error(new StringFormattedMessage("Failed to load resource '{}' for type '{}'", resourceIdentifier, type.getResourceCategory()).getFormattedMessage(), ex);
            }
        }
        return unlocksList;
    }
}
