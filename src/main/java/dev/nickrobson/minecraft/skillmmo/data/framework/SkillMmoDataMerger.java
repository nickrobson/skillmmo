package dev.nickrobson.minecraft.skillmmo.data.framework;

import com.mojang.datafixers.util.Either;

import java.util.List;
import java.util.Optional;

public interface SkillMmoDataMerger<T> {
    /**
     * Merge multiple entities into one
     *
     * @param entities the entities to merge into one. guaranteed to have at least one item
     *
     * @return an either with either:
     * - left: a provided value, or empty indicating to skip entities for this resource
     * - right: validation errors
     */
    Either<Optional<T>, List<String>> merge(List<T> entities);
}
