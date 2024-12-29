package dev.nickrobson.minecraft.skillmmo.data.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.nickrobson.minecraft.skillmmo.data.framework.SkillMmoCodecs;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

import java.util.Collections;

/**
 * Data shape for a skill's icon in a datapack
 */
public record SkillIconData(
        /*
          The type of the icon. Must be 'item'
         */
        String type,
        /*
          The icon value. The format of this depends on what 'type' is set to.
          For type 'item', this should be a Minecraft item identifier, like minecraft:stone or minecraft:egg.
         */
        Item item
) {
    static final Codec<SkillIconData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SkillMmoCodecs.forAllowedValues(Codec.STRING, Collections.singleton("item")).fieldOf("type").forGetter(it -> it.type),
            Registries.ITEM.getCodec().fieldOf("value").forGetter(it -> it.item)
    ).apply(instance, SkillIconData::new));

    public Item getItem() {
        return item;
    }
}
