package dev.nickrobson.minecraft.skillmmo.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.world.item.Item;

import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.SimpleResourceReloader;

import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;

public class SkillMmoResourceLoader extends SimpleResourceReloader<Set<Skill>> {
    private static final Logger logger = LogManager.getLogger(SkillMmoResourceLoader.class);

    @Override
    protected Set<Skill> prepare(SharedState store) {
        ResourceManager resourceManager = store.resourceManager();
        HolderLookup.Provider registryWrapperLookup = store.get(ResourceLoader.RELOADER_REGISTRY_LOOKUP_KEY);
        Map<Identifier, SkillData> skillsData = loadResources(resourceManager, SkillMmoDataType.SKILLS);

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
                                errors.add(Component.translatable("skillmmo.data.skill.missing.enabled", skillId).getString());
                                valid = false;
                            }
                            if (skillData.nameKey().isEmpty()) {
                                errors.add(Component.translatable("skillmmo.data.skill.missing.namekey", skillId).getString());
                                valid = false;
                            }
                            if (skillData.descriptionKey().isEmpty()) {
                                errors.add(Component.translatable("skillmmo.data.skill.missing.descriptionkey", skillId).getString());
                                valid = false;
                            }
                            if (skillData.maxLevel().isEmpty()) {
                                errors.add(Component.translatable("skillmmo.data.skill.missing.maxlevel", skillId).getString());
                                valid = false;
                            }

                            Optional<Item> iconItem = Optional.empty();
                            if (skillData.icon().isEmpty()) {
                                errors.add(Component.translatable("skillmmo.data.skill.missing.icon", skillId).getString());
                                valid = false;
                            } else if (!"item".equals(skillData.icon().get().type())) {
                                errors.add(Component.translatable("skillmmo.data.skill.invalid.icon.type", skillId, skillData.icon().get().type()).getString());
                                valid = false;
                            } else {
                                Identifier itemId = Identifier.tryParse(skillData.icon().get().value());
                                if (itemId == null) {
                                    errors.add(Component.translatable("skillmmo.data.skill.invalid.icon.item.id", skillId, skillData.icon().get().value()).getString());
                                    valid = false;
                                } else {
                                    HolderLookup.RegistryLookup<Item> itemRegistry = registryWrapperLookup.lookupOrThrow(Registries.ITEM);
                                    ResourceKey<Item> itemRegistryKey = ResourceKey.create(Registries.ITEM, itemId);
                                    iconItem = itemRegistry.get(itemRegistryKey).map(Holder.Reference::value);
                                    if (iconItem.isEmpty()) {
                                        errors.add(Component.translatable("skillmmo.data.skill.invalid.icon.item", skillId, itemId).getString());
                                        valid = false;
                                    }
                                }
                            }

                            if (valid) {
                                return Stream.of(
                                        new Skill(
                                                skillId,
                                                Component.translatable(skillData.nameKey().get()),
                                                Component.translatable(skillData.descriptionKey().get()),
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

        return skills;
    }

    @Override
    protected void apply(Set<Skill> prepared, SharedState store) {
        SkillManager.getInstance().initInstalledSkills(prepared);
    }

    private <T> Map<Identifier, T> loadResources(ResourceManager resourceManager, SkillMmoDataType<T> type) {
        FileToIdConverter resourceFinder = FileToIdConverter.json(type.getResourceCategory());
        Map<Identifier, Resource> resourceMap = resourceFinder.listMatchingResources(resourceManager);

        Map<Identifier, T> resourcesMap = new HashMap<>();
        SimpleJsonResourceReloadListener.scanDirectory(resourceManager, resourceFinder, JsonOps.INSTANCE, type.getCodec(), resourcesMap);

        Set<Identifier> successfullyLoaded = new TreeSet<>(resourcesMap.keySet());
        logger.info("Loaded resources for {}: {}", type.getResourceCategory(), successfullyLoaded);

        return resourcesMap;
    }
}
