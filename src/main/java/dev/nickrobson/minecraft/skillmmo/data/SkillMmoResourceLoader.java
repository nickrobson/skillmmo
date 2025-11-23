package dev.nickrobson.minecraft.skillmmo.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SkillMmoResourceLoader implements SimpleSynchronousResourceReloadListener {
    private static final Logger logger = LogManager.getLogger(SkillMmoResourceLoader.class);

    private final RegistryWrapper.WrapperLookup registryWrapperLookup;
    private final DynamicOps<JsonElement> ops;

    public SkillMmoResourceLoader(RegistryWrapper.WrapperLookup registryWrapperLookup) {
        this.registryWrapperLookup = registryWrapperLookup;
        this.ops = registryWrapperLookup.getOps(JsonOps.INSTANCE);
    }

    @Override
    public Identifier getFabricId() {
        return Identifier.of("skillmmo", "resources");
    }

    @Override
    public void reload(ResourceManager manager) {
        Map<Identifier, SkillData> skillsData = loadResources(manager, SkillMmoDataType.SKILLS);

        Map<Identifier, SkillData> skillDataBySkillId = new HashMap<>();
        skillsData.forEach((id, skillData) ->
                skillDataBySkillId.compute(id, (k, v) -> {
                    if (v == null || skillData.replace()) {
                        return skillData;
                    }
                    return new SkillData(
                            v.replace(),
                            skillData.enabled().isPresent() ? skillData.enabled() : v.enabled(),
                            skillData.nameKey().isPresent() ? skillData.nameKey() : v.nameKey(),
                            skillData.descriptionKey().isPresent() ? skillData.descriptionKey() : v.descriptionKey(),
                            skillData.maxLevel().isPresent() ? skillData.maxLevel() : v.maxLevel(),
                            skillData.icon().isPresent() ? skillData.icon() : v.icon()
                    );
                }));

        List<String> errors = new ArrayList<>();

        Set<Skill> skills = skillDataBySkillId.entrySet()
                .stream()
                .flatMap(skillDataPair -> {
                            Identifier skillId = skillDataPair.getKey();
                            SkillData skillData = skillDataPair.getValue();

                            boolean valid = true;

                            if (skillData.enabled().isEmpty()) {
                                errors.add(Text.translatable("skillmmo.data.skill.missing.enabled", skillId).getString());
                                valid = false;
                            }
                            if (skillData.nameKey().isEmpty()) {
                                errors.add(Text.translatable("skillmmo.data.skill.missing.namekey", skillId).getString());
                                valid = false;
                            }
                            if (skillData.descriptionKey().isEmpty()) {
                                errors.add(Text.translatable("skillmmo.data.skill.missing.descriptionkey", skillId).getString());
                                valid = false;
                            }
                            if (skillData.maxLevel().isEmpty()) {
                                errors.add(Text.translatable("skillmmo.data.skill.missing.maxlevel", skillId).getString());
                                valid = false;
                            }

                            Optional<Item> iconItem = Optional.empty();
                            if (skillData.icon().isEmpty()) {
                                errors.add(Text.translatable("skillmmo.data.skill.missing.icon", skillId).getString());
                                valid = false;
                            } else if (!"item".equals(skillData.icon().get().type())) {
                                errors.add(Text.translatable("skillmmo.data.skill.invalid.icon.type", skillId, skillData.icon().get().type()).getString());
                                valid = false;
                            } else {
                                Identifier itemId = Identifier.tryParse(skillData.icon().get().value());
                                if (itemId == null) {
                                    errors.add(Text.translatable("skillmmo.data.skill.invalid.icon.item.id", skillId, skillData.icon().get().value()).getString());
                                    valid = false;
                                } else {
                                    RegistryWrapper.Impl<Item> itemRegistry = registryWrapperLookup.getOrThrow(RegistryKeys.ITEM);
                                    RegistryKey<Item> itemRegistryKey = RegistryKey.of(RegistryKeys.ITEM, itemId);
                                    iconItem = itemRegistry.getOptional(itemRegistryKey).map(RegistryEntry.Reference::value);
                                    if (iconItem.isEmpty()) {
                                        errors.add(Text.translatable("skillmmo.data.skill.invalid.icon.item", skillId, itemId).getString());
                                        valid = false;
                                    }
                                }
                            }

                            if (valid) {
                                return Stream.of(
                                        new Skill(
                                                skillId,
                                                Text.translatable(skillData.nameKey().get()),
                                                Text.translatable(skillData.descriptionKey().get()),
                                                skillData.maxLevel().get(),
                                                iconItem.get()
                                        )
                                );
                            }

                            return Stream.empty();
                        }
                )
                .collect(Collectors.toUnmodifiableSet());

        if (!errors.isEmpty()) {
            throw new IllegalStateException(String.join("\n", errors));
        }

        SkillManager.getInstance().initInstalledSkills(skills);
    }

    private <T> Map<Identifier, T> loadResources(ResourceManager resourceManager, SkillMmoDataType<T> type) {
        ResourceFinder resourceFinder = ResourceFinder.json(type.getResourceCategory());
        Map<Identifier, Resource> resourceMap = resourceFinder.findResources(resourceManager);

        Map<Identifier, T> resourcesMap = new HashMap<>();
        JsonDataLoader.load(resourceManager, resourceFinder, this.ops, type.getCodec(), resourcesMap);

        Set<Identifier> successfullyLoaded = new TreeSet<>(resourcesMap.keySet());
        logger.info("Loaded resources for {}: {}", type.getResourceCategory(), successfullyLoaded);

        return resourcesMap;
    }
}
