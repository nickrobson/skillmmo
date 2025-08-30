package dev.nickrobson.minecraft.skillmmo.skill;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface SkillMmoPlayerDataHolder {
    @MethodsReturnNonnullByDefault
    @ParametersAreNonnullByDefault
    class SkillMmoPlayerData implements Cloneable {
        public static final SkillMmoPlayerData UNINITIALISED = new SkillMmoPlayerData(0, 0, Collections.emptyMap(), Collections.emptyMap());

        private long experience;
        private int availableSkillPoints;
        private Map<Identifier, Integer> skillLevels;
        private Map<Identifier, Set<RegistryKey<Recipe<?>>>> lockedRecipesByType;

        public SkillMmoPlayerData() {
            this(0L, 0, new HashMap<>(), new HashMap<>());
        }

        public SkillMmoPlayerData(long experience, int availableSkillPoints, Map<Identifier, Integer> skillLevels, Map<Identifier, Set<RegistryKey<Recipe<?>>>> lockedRecipesByType) {
            this.experience = experience;
            this.availableSkillPoints = availableSkillPoints;
            this.skillLevels = new HashMap<>(skillLevels);
            this.lockedRecipesByType = new HashMap<>(lockedRecipesByType);
        }

        private void checkInitialised() {
            if (this == UNINITIALISED) {
                throw new IllegalStateException("Cannot set skill level - this player data hasn't been loaded");
            }
        }

        public long getExperience() {
            return experience;
        }

        public long addExperience(long experience) {
            this.checkInitialised();
            if (experience > 0) {
                if (Long.MAX_VALUE - this.experience < experience) {
                    this.experience = Long.MAX_VALUE;
                } else {
                    this.experience += experience;
                }
            }
            return this.experience;
        }

        public void setExperience(long experience) {
            this.checkInitialised();
            this.experience = experience;
        }

        public int getAvailableSkillPoints() {
            return availableSkillPoints;
        }

        public void setAvailableSkillPoints(int availableSkillPoints) {
            this.availableSkillPoints = availableSkillPoints;
        }

        public int addAvailableSkillPoints(int availableSkillPoints) {
            this.checkInitialised();
            if (availableSkillPoints > 0) {
                if (Integer.MAX_VALUE - this.availableSkillPoints < availableSkillPoints) {
                    this.availableSkillPoints = Integer.MAX_VALUE;
                } else {
                    this.availableSkillPoints += availableSkillPoints;
                }
            }
            return this.availableSkillPoints;
        }

        public boolean consumeAvailableSkillPoint() {
            this.checkInitialised();
            if (this.availableSkillPoints > 0) {
                this.availableSkillPoints--;
                return true;
            }
            return false;
        }

        public Map<Identifier, Integer> getSkillLevels() {
            return Collections.unmodifiableMap(skillLevels);
        }

        public void setSkillLevel(Identifier skillId, int level) {
            this.checkInitialised();
            this.skillLevels.put(skillId, level);
        }

        @Override
        public SkillMmoPlayerData clone() {
            try {
                SkillMmoPlayerData clone = (SkillMmoPlayerData) super.clone();
                clone.experience = this.experience;
                clone.availableSkillPoints = this.availableSkillPoints;
                clone.skillLevels = new HashMap<>(this.skillLevels);
                clone.lockedRecipesByType = new HashMap<>(this.lockedRecipesByType);
                return clone;
            } catch (CloneNotSupportedException ex) {
                throw new RuntimeException(ex);
            }
        }

        public Map<Identifier, Set<RegistryKey<Recipe<?>>>> getLockedRecipesByType() {
            return Collections.unmodifiableMap(lockedRecipesByType);
        }

        public void addLockedRecipes(Collection<RecipeEntry<?>> recipes) {
            this.checkInitialised();
            recipes.forEach(recipe -> {
                Identifier recipeTypeId = Registries.RECIPE_TYPE.getId(recipe.value().getType());
                this.lockedRecipesByType.compute(recipeTypeId, (typeId, recipeIds) -> {
                    if (recipeIds == null) {
                        recipeIds = new HashSet<>();
                    }
                    recipeIds.add(recipe.id());
                    return recipeIds;
                });
            });
        }

        public void removeLockedRecipes(Collection<RecipeEntry<?>> recipes) {
            this.checkInitialised();
            recipes.forEach(recipe -> {
                Identifier recipeTypeId = Registries.RECIPE_TYPE.getId(recipe.value().getType());
                this.lockedRecipesByType.compute(recipeTypeId, (typeId, recipeIds) -> {
                    if (recipeIds != null) {
                        recipeIds.remove(recipe.id());
                        if (recipeIds.isEmpty()) {
                            return null;
                        }
                    }
                    return recipeIds;
                });
            });
        }
    }

    SkillMmoPlayerData skillMmo$getPlayerData();

    void skillMmo$setPlayerData(SkillMmoPlayerData playerData);
}
