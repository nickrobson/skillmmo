package dev.nickrobson.minecraft.skillmmo.data;

import com.mojang.serialization.Codec;

public final class SkillMmoDataType<T> {
    public static final SkillMmoDataType<SkillData> SKILLS = new SkillMmoDataType<>("skills", SkillData.class, SkillData.CODEC);

    private final String resourceCategory;
    private final Class<T> resourceClass;
    private final Codec<T> codec;

    private SkillMmoDataType(String resourceCategory, Class<T> resourceClass, Codec<T> codec) {
        this.resourceCategory = resourceCategory;
        this.resourceClass = resourceClass;
        this.codec = codec;
    }

    public String getResourceCategory() {
        return resourceCategory;
    }

    public Class<T> getResourceClass() {
        return resourceClass;
    }

    public Codec<T> getCodec() {
        return codec;
    }
}
