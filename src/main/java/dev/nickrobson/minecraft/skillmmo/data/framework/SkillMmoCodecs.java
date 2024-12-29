package dev.nickrobson.minecraft.skillmmo.data.framework;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class SkillMmoCodecs {
    private SkillMmoCodecs() {}

    public static <T> Codec<T> forAllowedValues(Codec<T> baseCodec, Set<T> allowedValues) {
        return baseCodec.comapFlatMap(
                baseValue -> {
                    if (allowedValues.contains(baseValue)) {
                        return DataResult.success(baseValue);
                    } else {
                        return DataResult.error(() -> "Value " + baseValue + " is not one of allowed values: " + allowedValues);
                    }
                },
                Function.identity()
        );
    }

    public static <E extends Enum<E>> Codec<E> withProxyOfEnumName(Class<E> enumType, Function<E, String> nameGetter) {
        Map<String, E> enumConstantsByName = Arrays.stream(enumType.getEnumConstants())
                .collect(Collectors.toMap(nameGetter, Function.identity()));

        return Codec.STRING.comapFlatMap(
                string -> {
                    E enumConstant = enumConstantsByName.get(string);
                    if (enumConstant == null) {
                        return DataResult.error(() -> "No enum constant " + string);
                    }
                    return DataResult.success(enumConstant);
                },
                nameGetter
        );
    }
}
