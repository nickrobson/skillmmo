package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.config.SkillMmoConfig;
import dev.nickrobson.minecraft.skillmmo.skill.SkillMmoPlayerDataHolder;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.PlayerSkillUnlockManager;
import net.minecraft.recipe.Recipe;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerRecipeBook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(ServerRecipeBook.class)
public class MixinServerRecipeBook {
    @ModifyVariable(
            method = "unlockRecipes",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    public Collection<Recipe<?>> skillMmo$unlockRecipes$makeRecipeCollectionMutable(Collection<Recipe<?>> recipes) {
        // ensure the recipes collection is mutable
        return new HashSet<>(recipes);
    }

    @Inject(
            method = "unlockRecipes",
            at = @At(value = "HEAD")
    )
    public void skillMmo$unlockRecipes$removeLockedRecipes(Collection<Recipe<?>> recipes, ServerPlayerEntity player, CallbackInfoReturnable<Integer> cir) {
        if (!SkillMmoConfig.getConfig().lockRecipesUntilIngredientsAndOutputAreUnlocked) {
            return;
        }

        Set<Recipe<?>> lockedRecipes = recipes.stream()
                .filter(recipe -> !PlayerSkillUnlockManager.getInstance().hasRecipeUnlock(player, recipe))
                .collect(Collectors.toSet());

        recipes.removeAll(lockedRecipes);

        ((SkillMmoPlayerDataHolder) player).getSkillMmoPlayerData()
                .addLockedRecipes(lockedRecipes);
        ((SkillMmoPlayerDataHolder) player).getSkillMmoPlayerData()
                .removeLockedRecipes(recipes);
    }
}
