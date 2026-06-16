package dev.nickrobson.minecraft.skillmmo.util;

import java.util.Set;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

public interface SkillMmoRecipeBookAccessor {
    Set<ResourceKey<Recipe<?>>> skillMmo$getUnlockedRecipes();
}
