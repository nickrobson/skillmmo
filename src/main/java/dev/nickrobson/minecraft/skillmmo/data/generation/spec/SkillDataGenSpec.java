package dev.nickrobson.minecraft.skillmmo.data.generation.spec;

import dev.nickrobson.minecraft.skillmmo.data.SkillData;
import dev.nickrobson.minecraft.skillmmo.data.SkillIconData;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Map;

public record SkillDataGenSpec(
        Identifier id,
        String nameKey,
        String descriptionKey,
        Item iconItem,
        Map<Integer, SkillLevelDataGenSpec> levels
) {
    public SkillData toSkillData() {
        SkillData result = new SkillData();
        result.replace = true;
        result.enabled = true;
        result.nameKey = nameKey;
        result.descriptionKey = descriptionKey;
        result.maxLevel = levels.keySet().stream().max(Integer::compareTo).orElse(-1);
        result.icon = new SkillIconData();
        result.icon.type = "item";
        result.icon.value = Registries.ITEM.getId(iconItem).toString();
        return result;
    }
}
