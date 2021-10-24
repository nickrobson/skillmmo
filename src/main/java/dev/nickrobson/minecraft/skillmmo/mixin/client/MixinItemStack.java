package dev.nickrobson.minecraft.skillmmo.mixin.client;

import dev.nickrobson.minecraft.skillmmo.config.SkillMmoConfig;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevel;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.PlayerSkillUnlockManager;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.Unlock;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.UnlockHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Set;

@Environment(EnvType.CLIENT)
@Mixin(ItemStack.class)
public abstract class MixinItemStack {
    @Inject(
            method = "getTooltip",
            at = @At(
                    value = "INVOKE",
                    shift = At.Shift.BEFORE,
                    ordinal = 0,
                    target = "Lnet/minecraft/item/ItemStack;getHideFlags()I"
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    public void skillMmo$getTooltip(@Nullable PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, List<Text> list) {
        if (player == null) {
            return;
        }

        ItemStack itemStack = (ItemStack) (Object) this;
        if (PlayerSkillUnlockManager.getInstance().hasItemUnlock(player, itemStack)) {
            return;
        }

        Unlock unlock = UnlockHelper.forItemStack(itemStack);
        Set<SkillLevel> skillLevelSet = SkillManager.getInstance().getSkillLevelsAffecting(unlock);

        if (skillLevelSet.isEmpty()) {
            list.add(new TranslatableText("skillmmo.feedback.item.locked"));
        } else if (Screen.hasShiftDown()) {
            MutableText text = SkillMmoConfig.getConfig().requireAllLockingSkillsToBeUnlocked
                    ? new TranslatableText("skillmmo.feedback.item.locked.advanced.heading.all")
                    : new TranslatableText("skillmmo.feedback.item.locked.advanced.heading.any");
            list.add(text.setStyle(Style.EMPTY.withColor(Formatting.RED)));
            skillLevelSet.forEach(skillLevel -> {
                        list.add(
                                new TranslatableText(
                                        "skillmmo.feedback.item.locked.advanced.line",
                                        skillLevel.getSkill().getNameText(),
                                        skillLevel.getLevel()
                                ).setStyle(Style.EMPTY.withColor(Formatting.RED)));
                    }
            );
        } else {
            SkillLevel skillLevel = PlayerSkillManager.getInstance().getClosestLevel(player, skillLevelSet);
            list.add(
                    new TranslatableText(
                            "skillmmo.feedback.item.locked.basic",
                            skillLevel.getSkill().getNameText(),
                            skillLevel.getLevel()
                    ).setStyle(Style.EMPTY.withColor(Formatting.RED))
            );
        }
    }
}
