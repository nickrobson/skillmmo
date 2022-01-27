package dev.nickrobson.minecraft.skillmmo.recipe;

import dev.nickrobson.minecraft.skillmmo.config.SkillMmoConfig;
import dev.nickrobson.minecraft.skillmmo.skill.SkillMmoPlayerDataHolder;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.PlayerSkillUnlockManager;
import dev.nickrobson.minecraft.skillmmo.util.SkillMmoRecipeBookAccessor;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerLockedRecipeManager {
    private static final PlayerLockedRecipeManager instance = new PlayerLockedRecipeManager();

    public static PlayerLockedRecipeManager getInstance() {
        return instance;
    }

    public void syncLockedRecipes(ServerPlayerEntity player) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) player;
        RecipeManager recipeManager = player.server.getRecipeManager();

        if (SkillMmoConfig.getConfig().lockRecipesUntilIngredientsAndOutputAreUnlocked) {
            // Unlock the recipes that have been unlocked since this was last synced (i.e. player has gained levels)
            Set<Recipe<?>> newlyUnlockedRecipes = getLockedRecipesAsStream(recipeManager, skillMmoPlayerDataHolder)
                    .filter(recipe -> PlayerSkillUnlockManager.getInstance().hasRecipeUnlock(player, recipe))
                    .collect(Collectors.toSet());
            if (!newlyUnlockedRecipes.isEmpty()) {
                player.unlockRecipes(newlyUnlockedRecipes);
            }

            // Lock the recipes that have been locked since this was last synced (i.e. player has lost levels)
            Set<Recipe<?>> newlyLockedRecipes = toUnlockedRecipesAsStream(recipeManager, player)
                    .filter(recipe -> !PlayerSkillUnlockManager.getInstance().hasRecipeUnlock(player, recipe))
                    .collect(Collectors.toSet());
            if (!newlyLockedRecipes.isEmpty()) {
                player.lockRecipes(newlyLockedRecipes);
                skillMmoPlayerDataHolder.getSkillMmoPlayerData().addLockedRecipes(newlyLockedRecipes);
            }
        } else {
            // Recipe locking is disabled so unlock everything
            Set<Recipe<?>> lockedRecipes = getLockedRecipesAsStream(recipeManager, skillMmoPlayerDataHolder)
                    .collect(Collectors.toSet());
            player.unlockRecipes(lockedRecipes);
            skillMmoPlayerDataHolder.getSkillMmoPlayerData()
                    .removeLockedRecipes(lockedRecipes);
        }
    }

    private Stream<Recipe<?>> toUnlockedRecipesAsStream(RecipeManager recipeManager, ServerPlayerEntity player) {
        return ((SkillMmoRecipeBookAccessor) player.getRecipeBook()).skillMmo$getRecipes()
                .stream()
                .flatMap(recipeId -> recipeManager.get(recipeId).stream());
    }

    private Stream<Recipe<?>> getLockedRecipesAsStream(RecipeManager recipeManager, SkillMmoPlayerDataHolder playerDataHolder) {
        return playerDataHolder.getSkillMmoPlayerData().getLockedRecipes().values().stream()
                .flatMap(Set::stream)
                .flatMap(recipeId -> recipeManager.get(recipeId).stream());
    }
}
