package dev.nickrobson.minecraft.skillmmo.data;

import javax.annotation.Nonnull;
import java.util.function.Function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

public final class SkillMmoCodecHelper {
    private SkillMmoCodecHelper() {
    }

    public static <T> Codec<T> constant(Codec<T> base, @Nonnull T constantValue) {
        return base.comapFlatMap(
                parsedValue -> {
                    if (constantValue.equals(parsedValue)) {
                        return DataResult.success(parsedValue);
                    }
                    return DataResult.error(() -> "", parsedValue);
                },
                Function.identity());
    }
}
