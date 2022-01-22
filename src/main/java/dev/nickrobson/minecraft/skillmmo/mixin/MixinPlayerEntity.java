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
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity implements SkillMmoPlayerDataHolder {
    private static final String SKILLMMO_ROOT_NBT_KEY = "skillMmo";
    private static final String SKILLMMO_EXPERIENCE_NBT_KEY = "experience";
    private static final String SKILLMMO_AVAILABLE_SKILL_POINTS_NBT_KEY = "availableSkillPoints";
    private static final String SKILLMMO_SKILL_LEVELS_NBT_KEY = "skillLevels";

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

        NbtCompound skillMmoNbt = nbt.contains(SKILLMMO_ROOT_NBT_KEY, NbtElement.COMPOUND_TYPE)
                ? nbt.getCompound(SKILLMMO_ROOT_NBT_KEY)
                : new NbtCompound();

        long experience = skillMmoNbt.contains(SKILLMMO_EXPERIENCE_NBT_KEY, NbtElement.NUMBER_TYPE)
                ? skillMmoNbt.getLong(SKILLMMO_EXPERIENCE_NBT_KEY)
                : 0L;

        int availableSkillPoints = skillMmoNbt.contains(SKILLMMO_AVAILABLE_SKILL_POINTS_NBT_KEY, NbtElement.NUMBER_TYPE)
                ? skillMmoNbt.getInt(SKILLMMO_AVAILABLE_SKILL_POINTS_NBT_KEY)
                : 0;

        Map<Identifier, Integer> skillLevels = new HashMap<>();
        if (skillMmoNbt.contains(SKILLMMO_SKILL_LEVELS_NBT_KEY, NbtElement.COMPOUND_TYPE)) {
            NbtCompound skillLevelsNbt = skillMmoNbt.getCompound(SKILLMMO_SKILL_LEVELS_NBT_KEY);
            for (String skillLevelKey : skillLevelsNbt.getKeys()) {
                if (skillLevelsNbt.contains(skillLevelKey, NbtElement.NUMBER_TYPE)) {
                    Identifier skillId = Identifier.tryParse(skillLevelKey);
                    int level = skillLevelsNbt.getInt(skillLevelKey);

                    if (skillId == null) {
                        continue;
                    }
                    skillLevels.put(skillId, level);
                }
            }
        }

        this.skillMmo$playerData = new SkillMmoPlayerData(experience, availableSkillPoints, skillLevels);
    }

    @Inject(
            method = "writeCustomDataToNbt",
            at = @At(value = "TAIL")
    )
    public void skillMmo$writeNbtData(NbtCompound nbt, CallbackInfo ci) {
        if (!SkillMmoMod.isModEnabled) {
            return;
        }

        SkillMmoPlayerData playerData = this.getSkillMmoPlayerData();
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

        nbt.put(SKILLMMO_ROOT_NBT_KEY, skillMmoNbt);
    }

    @Unique
    @Override
    public SkillMmoPlayerData getSkillMmoPlayerData() {
        return skillMmo$playerData != null
                ? skillMmo$playerData
                : SkillMmoPlayerData.UNINITIALISED;
    }

    @Unique
    @Override
    public void setSkillMmoPlayerData(@Nonnull SkillMmoPlayerData playerData) {
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
        ItemStack itemStackInHand = this.inventory.getMainHandStack();
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

    @Inject(
            method = "checkFallFlying",
            at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/entity/player/PlayerEntity;startFallFlying()V"),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    public void skillMmo$checkFallFlying(CallbackInfoReturnable<Boolean> cir, ItemStack itemStack) {
        PlayerEntity player = (PlayerEntity) (Object) this; // safe as this is a mixin for PlayerEntity
        if (!PlayerSkillUnlockManager.getInstance().hasItemUnlock(player, itemStack)) {
            PlayerSkillUnlockManager.getInstance().reportItemUseLocked(player, itemStack.getItem());
            cir.setReturnValue(false);
        }
    }
}
