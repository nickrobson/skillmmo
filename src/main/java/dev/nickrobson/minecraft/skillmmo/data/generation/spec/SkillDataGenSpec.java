package dev.nickrobson.minecraft.skillmmo.data.generation.spec;

import java.util.Map;
import java.util.Optional;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import dev.nickrobson.minecraft.skillmmo.data.SkillData;
import dev.nickrobson.minecraft.skillmmo.data.SkillIconData;

public record SkillDataGenSpec(
        Identifier id,
        String nameKey,
        String descriptionKey,
        Item iconItem,
        Map<Integer, SkillLevelDataGenSpec> levels
) {
    public SkillData toSkillData() {
        return new SkillData(
                true,
                Optional.of(true),
                Optional.of(nameKey),
                Optional.of(descriptionKey),
                Optional.of(levels.keySet().stream().max(Integer::compareTo).orElse(-1)),
                Optional.of(new SkillIconData(
                        "item",
                        BuiltInRegistries.ITEM.getKey(iconItem).toString()
                ))
        );
    }
}
