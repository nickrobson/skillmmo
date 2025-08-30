package dev.nickrobson.minecraft.skillmmo.util;

import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryKey;

import java.util.Set;

public interface SkillMmoRecipeBookAccessor {
    Set<RegistryKey<Recipe<?>>> skillMmo$getUnlockedRecipes();
}
