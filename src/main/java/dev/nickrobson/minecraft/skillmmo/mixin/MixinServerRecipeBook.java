package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.config.SkillMmoConfig;
import dev.nickrobson.minecraft.skillmmo.skill.data.SkillMmoPlayerDataHolder;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.PlayerSkillUnlockManager;
import dev.nickrobson.minecraft.skillmmo.util.SkillMmoRecipeBookAccessor;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerRecipeBook;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(ServerRecipeBook.class)
public class MixinServerRecipeBook implements SkillMmoRecipeBookAccessor {
    @ModifyVariable(
            method = "unlockRecipes",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    public Collection<RecipeEntry<?>> skillMmo$unlockRecipes$makeRecipeCollectionMutable(Collection<RecipeEntry<?>> recipes) {
        // ensure the recipes collection is mutable
        return new HashSet<>(recipes);
    }

    @Inject(
            method = "unlockRecipes",
            at = @At(value = "HEAD")
    )
    public void skillMmo$unlockRecipes$removeLockedRecipes(Collection<RecipeEntry<?>> recipes, ServerPlayerEntity player, CallbackInfoReturnable<Integer> cir) {
        if (!SkillMmoConfig.getConfig().lockRecipesUntilIngredientsAndOutputAreUnlocked) {
            return;
        }

        Set<RecipeEntry<?>> lockedRecipes = recipes.stream()
                .filter(recipe -> !PlayerSkillUnlockManager.getInstance().hasRecipeUnlock(player, recipe))
                .collect(Collectors.toSet());

        recipes.removeAll(lockedRecipes);

        ((SkillMmoPlayerDataHolder) player).skillMmo$getPlayerData()
                .addLockedRecipes(lockedRecipes);
        ((SkillMmoPlayerDataHolder) player).skillMmo$getPlayerData()
                .removeLockedRecipes(recipes);
    }

    @Shadow
    @Final
    protected Set<RegistryKey<Recipe<?>>> unlocked;

    @Override
    public Set<RegistryKey<Recipe<?>>> skillMmo$getUnlockedRecipes() {
        return this.unlocked;
    }
}
