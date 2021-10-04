package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.skill.SkillMmoPlayerDataHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity implements SkillMmoPlayerDataHolder {
    private static final String ROOT_NBT_KEY = "skillMmo";
    private static final String EXPERIENCE_NBT_KEY = "experience";
    private static final String SKILL_LEVELS_NBT_KEY = "skillLevels";

    private SkillMmoPlayerData skillMmo$playerData = null;

    @Inject(
            method = "readCustomDataFromNbt",
            at = @At(value = "TAIL")
    )
    public void readSkillMmoNbtData(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound skillMmoNbt = nbt.contains(ROOT_NBT_KEY, NbtElement.COMPOUND_TYPE)
                ? nbt.getCompound(ROOT_NBT_KEY)
                : new NbtCompound();

        long experience = skillMmoNbt.contains(EXPERIENCE_NBT_KEY, NbtElement.NUMBER_TYPE)
                ? skillMmoNbt.getLong(EXPERIENCE_NBT_KEY)
                : 0L;

        Map<String, Byte> skillLevels = new HashMap<>();
        if (skillMmoNbt.contains(SKILL_LEVELS_NBT_KEY, NbtElement.COMPOUND_TYPE)) {
            NbtCompound skillLevelsNbt = skillMmoNbt.getCompound(SKILL_LEVELS_NBT_KEY);
            for (String skillLevelKey : skillLevelsNbt.getKeys()) {
                if (skillLevelsNbt.contains(skillLevelKey, NbtElement.NUMBER_TYPE)) {
                    byte level = skillLevelsNbt.getByte(skillLevelKey);
                    skillLevels.put(skillLevelKey, level);
                }
            }
        }

        this.skillMmo$playerData = new SkillMmoPlayerData(experience, skillLevels);
    }

    @Inject(
            method = "writeCustomDataToNbt",
            at = @At(value = "TAIL")
    )
    public void writeSkillMmoNbtData(NbtCompound nbt, CallbackInfo ci) {
        SkillMmoPlayerData playerData = this.getSkillMmoPlayerData();
        NbtCompound skillMmoNbt = new NbtCompound();

        {
            long experience = playerData.experience();
            skillMmoNbt.putLong(EXPERIENCE_NBT_KEY, experience);
        }

        {
            NbtCompound skillLevelsNbt = new NbtCompound();
            playerData.skillLevels().forEach(skillLevelsNbt::putByte);
            skillMmoNbt.put(SKILL_LEVELS_NBT_KEY, skillLevelsNbt);
        }

        nbt.put(ROOT_NBT_KEY, skillMmoNbt);
    }

    @Override
    public SkillMmoPlayerData getSkillMmoPlayerData() {
        return skillMmo$playerData != null ? skillMmo$playerData : SkillMmoPlayerData.UNINITIALISED;
    }
}
