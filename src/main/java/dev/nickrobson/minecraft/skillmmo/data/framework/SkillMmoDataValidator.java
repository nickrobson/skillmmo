package dev.nickrobson.minecraft.skillmmo.data.framework;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface SkillMmoDataValidator<T> {
    void validate(T entity, @Nonnull Collection<String> errors);
}
