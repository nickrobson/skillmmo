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
import java.util.stream.Stream;

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
        List<SkillLevelBlockUnlocksData> blockUnlocksData = loadResources(manager, SkillMmoDataType.BLOCKS);
        List<SkillLevelItemUnlocksData> itemUnlocksData = loadResources(manager, SkillMmoDataType.ITEMS);
        // TODO - entities

        Map<String, Set<SkillLevel>> skillLevelsBySkill = loadSkillLevels(blockUnlocksData, itemUnlocksData);

        Map<String, SkillData> skillDataBySkillId = new HashMap<>();
        skillsData.forEach((skillData) -> {
            skillDataBySkillId.compute(skillData.id, (k, v) -> {
                if (v == null || skillData.replace) {
                    return skillData;
                }
                v.replace = false;
                v.translationKey = skillData.translationKey;
                v.enabled = skillData.enabled;
                return v;
            });
        });
        Set<Skill> skills = skillDataBySkillId.values()
                .stream()
                .filter(skillData -> skillData.enabled)
                .map(skillData -> {
                    Set<SkillLevel> skillLevels = skillLevelsBySkill.get(skillData.id);
                    if (skillLevels == null || skillLevels.isEmpty()) {
                        return null;
                    }

                    return new Skill(
                            skillData.id,
                            skillData.translationKey,
                            skillLevels
                    );
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        SkillManager.getInstance().initSkills(skills);
    }

    private Map<String, Set<SkillLevel>> loadSkillLevels(List<SkillLevelBlockUnlocksData> blockUnlocksData, List<SkillLevelItemUnlocksData> itemUnlocksData) {
        Map<String, List<AbstractSkillLevelUnlocksData>> unlocksDataBySkillId = Stream
                .concat(blockUnlocksData.stream(), itemUnlocksData.stream())
                .collect(Collectors.groupingBy(data -> data.skillId, Collectors.toList()));

        Map<String, Set<SkillLevel>> skillLevelsBySkillId = new HashMap<>();
        unlocksDataBySkillId.forEach((skillId, unlocksDataForSkill) -> {
            Map<Byte, List<AbstractSkillLevelUnlocksData>> unlocksDataByLevel = unlocksDataForSkill
                    .stream()
                    .collect(Collectors.groupingBy(data -> data.level, Collectors.toList()));

            Map<Byte, SkillLevel> skillLevelsByLevel = new HashMap<>();
            unlocksDataByLevel.forEach((level, unlocksDataForSkillLevel) -> {
                Map<SkillLevelUnlockType, Set<Identifier>> unlockIdentifiersByUnlockType = new HashMap<>();
                unlocksDataForSkillLevel.forEach(unlockData ->
                        unlockIdentifiersByUnlockType.compute(unlockData.getUnlockType(), (k, v) -> {
                            if (v == null || unlockData.replace) {
                                v = new HashSet<>();
                            }
                            Set<Identifier> identifiers = unlockData.getRawIdentifiers()
                                    .stream()
                                    .map(id -> {
                                        try {
                                            return new Identifier(id);
                                        } catch (Exception ex) {
                                            logger.warn(String.format("Failed to parse identifier '%s'", id), ex);
                                            return null;
                                        }
                                    })
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toSet());
                            v.addAll(identifiers);
                            return v;
                        })
                );

                skillLevelsByLevel.put(
                        level,
                        new SkillLevel(skillId, level, unlockIdentifiersByUnlockType)
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
