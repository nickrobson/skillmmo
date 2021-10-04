package dev.nickrobson.minecraft.skillmmo.data;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface DataValidatable {
    void validate(@Nonnull Collection<String> errors);
}
