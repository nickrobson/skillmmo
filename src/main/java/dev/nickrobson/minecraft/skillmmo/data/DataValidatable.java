package dev.nickrobson.minecraft.skillmmo.data;

import java.util.Collection;

import org.jspecify.annotations.NonNull;

public interface DataValidatable {
    void validate(@NonNull Collection<String> errors);
}
