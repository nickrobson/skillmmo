package dev.nickrobson.minecraft.skillmmo.data;

import com.mojang.serialization.Codec;
import dev.nickrobson.minecraft.skillmmo.data.entity.SkillData;
import dev.nickrobson.minecraft.skillmmo.data.framework.SkillMmoDataMerger;
import dev.nickrobson.minecraft.skillmmo.data.framework.SkillMmoDataValidator;
import net.minecraft.util.annotation.FieldsAreNonnullByDefault;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@FieldsAreNonnullByDefault
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public final class SkillMmoDataType<T> {
    public static final SkillMmoDataType<SkillData> SKILLS = new SkillMmoDataType<>("skills", SkillData.CODEC, SkillData.VALIDATOR, SkillData.MERGER);

    private final String resourceCategory;
    private final Codec<T> codec;
    private final SkillMmoDataValidator<T> validator;
    private final SkillMmoDataMerger<T> merger;

    private SkillMmoDataType(String resourceCategory, Codec<T> codec, SkillMmoDataValidator<T> validator, SkillMmoDataMerger<T> merger) {
        this.resourceCategory = resourceCategory;
        this.codec = codec;
        this.validator = validator;
        this.merger = merger;
    }

    public String getResourceCategory() {
        return resourceCategory;
    }

    public Codec<T> getCodec() {
        return codec;
    }

    public SkillMmoDataValidator<T> getValidator() {
        return validator;
    }

    public SkillMmoDataMerger<T> getMerger() {
        return merger;
    }
}
