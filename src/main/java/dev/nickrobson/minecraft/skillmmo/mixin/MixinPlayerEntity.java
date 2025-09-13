package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import dev.nickrobson.minecraft.skillmmo.config.SkillMmoConfig;
import dev.nickrobson.minecraft.skillmmo.skill.SkillMmoPlayerDataHolder;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.PlayerSkillUnlockManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity implements SkillMmoPlayerDataHolder {
    @Unique
    private static final String SKILLMMO_ROOT_NBT_KEY = "skillMmo";
    @Unique
    private static final String SKILLMMO_EXPERIENCE_NBT_KEY = "experience";
    @Unique
    private static final String SKILLMMO_AVAILABLE_SKILL_POINTS_NBT_KEY = "availableSkillPoints";
    @Unique
    private static final String SKILLMMO_SKILL_LEVELS_NBT_KEY = "skillLevels";
    @Unique
    private static final String SKILLMMO_LOCKED_RECIPES_NBT_KEY = "lockedRecipes";

    @Unique
    private SkillMmoPlayerData skillMmo$playerData = null;

    @Shadow
    @Final
    private PlayerInventory inventory;

    @Inject(
            method = "readCustomDataFromNbt",
            at = @At(value = "TAIL")
    )
    public void skillMmo$readNbtData(NbtCompound nbt, CallbackInfo ci) {
        if (!SkillMmoMod.isModEnabled) {
            return;
        }

        NbtCompound skillMmoNbt = nbt.getCompoundOrEmpty(SKILLMMO_ROOT_NBT_KEY);
        long experience = skillMmoNbt.getLong(SKILLMMO_EXPERIENCE_NBT_KEY, 0L);
        int availableSkillPoints = skillMmoNbt.getInt(SKILLMMO_AVAILABLE_SKILL_POINTS_NBT_KEY, 0);

        Map<Identifier, Integer> skillLevels = new HashMap<>();
        NbtCompound skillLevelsNbt = skillMmoNbt.getCompoundOrEmpty(SKILLMMO_SKILL_LEVELS_NBT_KEY);
        for (String skillLevelKey : skillLevelsNbt.getKeys()) {
            skillLevelsNbt.getInt(skillLevelKey).ifPresent(level -> {
                Identifier skillId = Identifier.tryParse(skillLevelKey);
                if (skillId != null) {
                    skillLevels.put(skillId, level);
                }
            });
        }

        Map<Identifier, Set<RegistryKey<Recipe<?>>>> lockedRecipes = new HashMap<>();
        NbtCompound lockedRecipesNbt = skillMmoNbt.getCompoundOrEmpty(SKILLMMO_LOCKED_RECIPES_NBT_KEY);
        for (String recipeTypeKey : lockedRecipesNbt.getKeys()) {
            Identifier recipeTypeId = Identifier.tryParse(recipeTypeKey);
            if (recipeTypeId == null) {
                continue;
            }
            NbtList lockedRecipeIdsNbt = lockedRecipesNbt.getListOrEmpty(recipeTypeKey);
            Set<RegistryKey<Recipe<?>>> lockedRecipeIds = lockedRecipeIdsNbt
                    .stream()
                    .<RegistryKey<Recipe<?>>>mapMulti((recipeIdNbt, sink) -> {
                        recipeIdNbt.asString().map(Identifier::tryParse).ifPresent(recipeId -> {
                            sink.accept(RegistryKey.of(RegistryKeys.RECIPE, recipeId));
                        });
                    })
                    .collect(Collectors.toSet());
            if (!lockedRecipeIds.isEmpty()) {
                lockedRecipes.put(recipeTypeId, lockedRecipeIds);
            }
        }

        this.skillMmo$playerData = new SkillMmoPlayerData(experience, availableSkillPoints, skillLevels, lockedRecipes);
    }

    @Inject(
            method = "writeCustomDataToNbt",
            at = @At(value = "TAIL")
    )
    public void skillMmo$writeNbtData(NbtCompound nbt, CallbackInfo ci) {
        if (!SkillMmoMod.isModEnabled) {
            return;
        }

        SkillMmoPlayerData playerData = this.skillMmo$getPlayerData();
        NbtCompound skillMmoNbt = new NbtCompound();

        {
            long experience = playerData.getExperience();
            skillMmoNbt.putLong(SKILLMMO_EXPERIENCE_NBT_KEY, experience);
        }

        {
            int availableSkillPoints = playerData.getAvailableSkillPoints();
            skillMmoNbt.putLong(SKILLMMO_AVAILABLE_SKILL_POINTS_NBT_KEY, availableSkillPoints);
        }

        {
            NbtCompound skillLevelsNbt = new NbtCompound();
            playerData.getSkillLevels().forEach((skillId, level) ->
                    skillLevelsNbt.putInt(skillId.toString(), level));
            skillMmoNbt.put(SKILLMMO_SKILL_LEVELS_NBT_KEY, skillLevelsNbt);
        }

        {
            NbtCompound lockedRecipesNbt = new NbtCompound();
            playerData.getLockedRecipesByType().forEach((recipeTypeId, recipeIds) -> {
                NbtList recipeIdsNbt = new NbtList();
                recipeIds.stream()
                        .map(RegistryKey::getValue)
                        .map(Identifier::toString)
                        .map(NbtString::of)
                        .forEach(recipeIdsNbt::add);
                lockedRecipesNbt.put(recipeTypeId.toString(), recipeIdsNbt);
            });
            skillMmoNbt.put(SKILLMMO_LOCKED_RECIPES_NBT_KEY, lockedRecipesNbt);
        }

        nbt.put(SKILLMMO_ROOT_NBT_KEY, skillMmoNbt);
    }

    @Unique
    @Nonnull
    @Override
    public SkillMmoPlayerData skillMmo$getPlayerData() {
        return skillMmo$playerData != null
                ? skillMmo$playerData
                : SkillMmoPlayerData.UNINITIALISED;
    }

    @Unique
    @Override
    public void skillMmo$setPlayerData(@Nonnull SkillMmoPlayerData playerData) {
        this.skillMmo$playerData = playerData;
    }

    // This prevents blocks from dropping items when you haven't unlocked them
    @Inject(
            method = "canHarvest",
            at = @At("HEAD"),
            cancellable = true
    )
    public void skillMmo$canHarvest(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this; // safe as this is a mixin for PlayerEntity
        ItemStack itemStackInHand = player.getMainHandStack();
        if (!PlayerSkillUnlockManager.getInstance().hasItemUnlock(player, itemStackInHand) && !(itemStackInHand.getItem() instanceof BlockItem)) {
            PlayerSkillUnlockManager.getInstance().reportItemUseLocked(player, itemStackInHand.getItem());
            cir.setReturnValue(false);
        } else if (!PlayerSkillUnlockManager.getInstance().hasBlockUnlock(player, state)) {
            if (SkillMmoConfig.getConfig().announceRequiredSkillWhenBreakingBlock) {
                // Only announce what skill is required to break a certain block if configured – it can be quite verbose
                PlayerSkillUnlockManager.getInstance().reportBlockBreakLocked(player, state.getBlock());
            }
            cir.setReturnValue(false);
        }
    }
}
