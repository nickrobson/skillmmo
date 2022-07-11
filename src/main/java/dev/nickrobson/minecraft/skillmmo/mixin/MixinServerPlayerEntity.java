package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.config.SkillMmoConfig;
import dev.nickrobson.minecraft.skillmmo.experience.PlayerExperienceManager;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity {
    @Inject(
            method = "addExperience",
            at = @At("TAIL")
    )
    public void skillMmo$addExperience(int experience, CallbackInfo ci) {
        PlayerExperienceManager.getInstance()
                .giveExperience((ServerPlayerEntity) (Object) this, experience);
    }

    @Inject(
            method = "onDeath",
            at = @At("TAIL")
    )
    public void skillMmo$onDeath(DamageSource source, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        int levelsLostOnDeath = SkillMmoConfig.getConfig().loseAllLevelsOnDeath
                ? Integer.MAX_VALUE
                : SkillMmoConfig.getConfig().levelsLostOnDeath;

        if (levelsLostOnDeath > 0) {
            Map<Identifier, Integer> skillLevels = PlayerSkillManager.getInstance().getSkillLevels(player);
            Map<Identifier, Integer> newSkillLevels;
            Identifier skillId = null;
            if (SkillMmoConfig.getConfig().loseLevelsInAllSkillsOnDeath) {
                newSkillLevels = skillLevels.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                            int currentLevel = e.getValue();
                            return currentLevel > levelsLostOnDeath ? currentLevel - levelsLostOnDeath : 0;
                        }));
            } else {
                List<Identifier> ownedSkillIds = skillLevels.entrySet()
                        .stream()
                        .filter(e -> e.getValue() != null && e.getValue() > 0)
                        .map(Map.Entry::getKey)
                        .toList();
                skillId = ownedSkillIds.get(new Random().nextInt(ownedSkillIds.size()));
                int currentLevel = skillLevels.get(skillId);
                newSkillLevels = Map.of(
                        skillId,
                        currentLevel > levelsLostOnDeath ? currentLevel - levelsLostOnDeath : 0
                );
            }
            PlayerSkillManager.getInstance().updateSkillLevels(player, newSkillLevels);

            int totalLostLevels = skillLevels.keySet().stream()
                    .filter(newSkillLevels::containsKey)
                    .mapToInt(id -> skillLevels.get(id) - newSkillLevels.get(id))
                    .sum();

            MutableText message;
            if (skillId == null) {
                // Lost levels in all skills
                message = totalLostLevels == 1
                        ? Text.translatable("skillmmo.feedback.player.death.lost_level", totalLostLevels)
                        : Text.translatable("skillmmo.feedback.player.death.lost_levels", totalLostLevels);
            } else {
                // Lost levels in one skill
                Text skillName = SkillManager.getInstance().getSkill(skillId).orElseThrow().getName();
                message = totalLostLevels == 1
                        ? Text.translatable("skillmmo.feedback.player.death.lost_level.in.skill", totalLostLevels, skillName)
                        : Text.translatable("skillmmo.feedback.player.death.lost_levels.in.skill", totalLostLevels, skillName);
            }


            player.sendMessage(
                    message.setStyle(Style.EMPTY.withFormatting(Formatting.RED)),
                    false
            );
        }
    }
}
