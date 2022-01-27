package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.util.SkillMmoRecipeBookAccessor;
import net.minecraft.recipe.book.RecipeBook;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(RecipeBook.class)
public class MixinRecipeBook implements SkillMmoRecipeBookAccessor {
    @Shadow
    @Final
    protected Set<Identifier> recipes;

    @Override
    public Set<Identifier> skillMmo$getRecipes() {
        return this.recipes;
    }
}
