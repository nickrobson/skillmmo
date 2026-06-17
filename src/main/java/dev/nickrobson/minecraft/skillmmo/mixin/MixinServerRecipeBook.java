package dev.nickrobson.minecraft.skillmmo.mixin;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.nickrobson.minecraft.skillmmo.config.SkillMmoConfig;
import dev.nickrobson.minecraft.skillmmo.skill.data.SkillMmoPlayerDataHolder;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.PlayerSkillUnlockManager;
import dev.nickrobson.minecraft.skillmmo.util.SkillMmoRecipeBookAccessor;

@Mixin(ServerRecipeBook.class)
public class MixinServerRecipeBook implements SkillMmoRecipeBookAccessor {
    @ModifyVariable(
            method = "addRecipes",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    public Collection<RecipeHolder<?>> skillMmo$unlockRecipes$makeRecipeCollectionMutable(Collection<RecipeHolder<?>> recipes) {
        // ensure the recipes collection is mutable
        return new HashSet<>(recipes);
    }

    @Inject(
            method = "addRecipes",
            at = @At(value = "HEAD")
    )
    public void skillMmo$unlockRecipes$removeLockedRecipes(Collection<RecipeHolder<?>> recipes, ServerPlayer player, CallbackInfoReturnable<Integer> cir) {
        if (!SkillMmoConfig.getConfig().lockRecipesUntilIngredientsAndOutputAreUnlocked) {
            return;
        }

        Set<RecipeHolder<?>> lockedRecipes = recipes.stream()
                .filter(recipe -> !PlayerSkillUnlockManager.getInstance().hasRecipeUnlock(player, recipe))
                .collect(Collectors.toSet());

        recipes.removeAll(lockedRecipes);

        SkillMmoPlayerDataHolder.getPlayerDataHolder(player).skillMmo$getPlayerData()
                .addLockedRecipes(lockedRecipes);
        SkillMmoPlayerDataHolder.getPlayerDataHolder(player).skillMmo$getPlayerData()
                .removeLockedRecipes(recipes);
    }

    @Shadow
    @Final
    protected Set<ResourceKey<Recipe<?>>> known;

    @Override
    public Set<ResourceKey<Recipe<?>>> skillMmo$getUnlockedRecipes() {
        return this.known;
    }
}
