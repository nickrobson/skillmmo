package dev.nickrobson.minecraft.skillmmo.recipe;

import dev.nickrobson.minecraft.skillmmo.config.SkillMmoConfig;
import dev.nickrobson.minecraft.skillmmo.skill.data.SkillMmoPlayerDataHolder;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.PlayerSkillUnlockManager;
import dev.nickrobson.minecraft.skillmmo.util.SkillMmoRecipeBookAccessor;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

public class PlayerLockedRecipeManager {
    private static final PlayerLockedRecipeManager instance = new PlayerLockedRecipeManager();

    public static PlayerLockedRecipeManager getInstance() {
        return instance;
    }

    public void syncLockedRecipes(ServerPlayer player) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = SkillMmoPlayerDataHolder.getPlayerDataHolder(player);
        RecipeManager recipeManager = player.level().getServer().getRecipeManager();

        if (SkillMmoConfig.getConfig().lockRecipesUntilIngredientsAndOutputAreUnlocked) {
            // Unlock the recipes that have been unlocked since this was last synced (i.e. player has gained levels)
            Set<RecipeHolder<?>> newlyUnlockedRecipes = getLockedRecipesAsStream(recipeManager, skillMmoPlayerDataHolder)
                    .filter(recipe -> PlayerSkillUnlockManager.getInstance().hasRecipeUnlock(player, recipe))
                    .collect(Collectors.toSet());
            if (!newlyUnlockedRecipes.isEmpty()) {
                player.awardRecipes(newlyUnlockedRecipes);
            }

            // Lock the recipes that have been locked since this was last synced (i.e. player has lost levels)
            Set<RecipeHolder<?>> newlyLockedRecipes = toUnlockedRecipesAsStream(recipeManager, player)
                    .filter(recipe -> !PlayerSkillUnlockManager.getInstance().hasRecipeUnlock(player, recipe))
                    .collect(Collectors.toSet());
            if (!newlyLockedRecipes.isEmpty()) {
                player.resetRecipes(newlyLockedRecipes);
                skillMmoPlayerDataHolder.skillMmo$getPlayerData().addLockedRecipes(newlyLockedRecipes);
            }
        } else {
            // Recipe locking is disabled so unlock everything
            Set<RecipeHolder<?>> lockedRecipes = getLockedRecipesAsStream(recipeManager, skillMmoPlayerDataHolder)
                    .collect(Collectors.toSet());
            player.awardRecipes(lockedRecipes);
            skillMmoPlayerDataHolder.skillMmo$getPlayerData()
                    .removeLockedRecipes(lockedRecipes);
        }
    }

    private Stream<RecipeHolder<?>> toUnlockedRecipesAsStream(RecipeManager recipeManager, ServerPlayer player) {
        return ((SkillMmoRecipeBookAccessor) player.getRecipeBook()).skillMmo$getUnlockedRecipes()
                .stream()
                .flatMap(recipeId -> recipeManager.byKey(recipeId).stream());
    }

    private Stream<RecipeHolder<?>> getLockedRecipesAsStream(RecipeManager recipeManager, SkillMmoPlayerDataHolder playerDataHolder) {
        return playerDataHolder.skillMmo$getPlayerData().getLockedRecipesByType().values()
                .stream()
                .flatMap(Set::stream)
                .flatMap(recipeId -> recipeManager.byKey(recipeId).stream());
    }
}
