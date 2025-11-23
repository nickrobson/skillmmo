package dev.nickrobson.minecraft.skillmmo.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.annotation.FieldsAreNonnullByDefault;

import static dev.nickrobson.minecraft.skillmmo.data.SkillMmoCodecHelper.constant;

/**
 * Data shape for a skill's icon in a datapack
 */
@FieldsAreNonnullByDefault
public record SkillIconData(
        /*
         * The type of the icon. Must be 'item'
         */
        String type,
        /*
         * The icon value. The format of this depends on what 'type' is set to.
         * For type 'item', this should be a Minecraft item identifier, like minecraft:stone or minecraft:egg.
         */
        String value
) {
    public static final Codec<SkillIconData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            constant(Codec.STRING, "item").fieldOf("type").forGetter(SkillIconData::type),
            Codec.STRING.fieldOf("value").forGetter(SkillIconData::value)
    ).apply(instance, SkillIconData::new));
}
