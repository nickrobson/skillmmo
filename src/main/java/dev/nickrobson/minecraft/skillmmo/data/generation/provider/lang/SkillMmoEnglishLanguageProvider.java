package dev.nickrobson.minecraft.skillmmo.data.generation.provider.lang;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class SkillMmoEnglishLanguageProvider extends FabricLanguageProvider {
    public SkillMmoEnglishLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public String getName() {
        return "Language: en_US";
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("skillmmo.command.skills.heading", "Skills (%d)");
        translationBuilder.add("skillmmo.command.skills.skill_line", "- %s");
        translationBuilder.add("skillmmo.command.skills.skill_line_with_level", "- %s (level %d/%d)");
        translationBuilder.add("skillmmo.command.skills.available_points_self", "You have %d available skill points");
        translationBuilder.add("skillmmo.command.skills.available_points_other", "%s has %d available skill points");
        translationBuilder.add("skillmmo.command.skills.player_experience_self", "You are level %d overall (%d%% to level %d)");
        translationBuilder.add("skillmmo.command.skills.player_experience_other", "%s is level %d overall (%d%% to level %d)");

        translationBuilder.add("skillmmo.command.skill.acquire.success", "You're now level %d in %s");
        translationBuilder.add("skillmmo.command.skill.acquire.failure_max_level", "You're already at max level in %s!");
        translationBuilder.add("skillmmo.command.skill.acquire.failure_no_available_points", "You have no available skill points!");
        translationBuilder.add("skillmmo.command.skill.info.skill", "%s (%d levels)");
        translationBuilder.add("skillmmo.command.skill.info.description", "%s");
        translationBuilder.add("skillmmo.command.skill.player_is_level", "%s is level %d in %s");
        translationBuilder.add("skillmmo.command.skill.player_is_now_level", "%s is now level %d in %s");
        translationBuilder.add("skillmmo.command.skill.you_are_now_level", "You are now level %d in %s");

        translationBuilder.add("skillmmo.feedback.deny.block.break", "You need %s %d to break this %s");
        translationBuilder.add("skillmmo.feedback.deny.block.interact", "You need %s %d to use this %s");
        translationBuilder.add("skillmmo.feedback.deny.block.place", "You need %s %d to place this %s");
        translationBuilder.add("skillmmo.feedback.deny.item.use", "You need %s %d to use this %s");
        translationBuilder.add("skillmmo.feedback.deny.item.collect.dragon.breath", "You need %s %d to collect %s");
        translationBuilder.add("skillmmo.feedback.deny.entity.interact", "You need %s %d to interact with a %s");
        translationBuilder.add("skillmmo.feedback.deny.villager.interact", "You need %s %d to trade with a Villager");
        translationBuilder.add("skillmmo.feedback.item.locked", "Locked");
        translationBuilder.add("skillmmo.feedback.item.locked.basic", "Locked - %s %d");
        translationBuilder.add("skillmmo.feedback.item.locked.advanced.heading.all", "Locked - requires:");
        translationBuilder.add("skillmmo.feedback.item.locked.advanced.heading.any", "Locked - requires one of:");
        translationBuilder.add("skillmmo.feedback.item.locked.advanced.line", "- %s %d");
        translationBuilder.add("skillmmo.feedback.player.death.lost_level", "You lost %d level in each skill.");
        translationBuilder.add("skillmmo.feedback.player.death.lost_levels", "You lost %d levels in each skill.");
        translationBuilder.add("skillmmo.feedback.player.death.lost_level.in.skill", "You lost %d %s level.");
        translationBuilder.add("skillmmo.feedback.player.death.lost_levels.in.skill", "You lost %d %s levels.");
        translationBuilder.add("skillmmo.feedback.player.level_up", "You've reached level %d! You have %d available skill points.");
        translationBuilder.add("skillmmo.feedback.player.skill_choice.failed_max_level", "You've already reached the maximum level of %d!");
        translationBuilder.add("skillmmo.feedback.player.skill_choice.failed_no_points", "You don't have any available skill points to spend!");

        translationBuilder.add("skillmmo.gui.skills.title", "Skills");
        translationBuilder.add("skillmmo.gui.skills.info.level", "Level %d");
        translationBuilder.add("skillmmo.gui.skills.info.xp_progress", "XP %d/%d");
        translationBuilder.add("skillmmo.gui.skills.info.available_points", "Available points, %d");
        translationBuilder.add("skillmmo.gui.skills.info.acquire_skill.narration", "Level up %s");
        translationBuilder.add("skillmmo.gui.skills.skill.name", "%s");
        translationBuilder.add("skillmmo.gui.skill.info.current_level", "Level %d/%d");
        translationBuilder.add("skillmmo.gui.skill.unlocks.title", "Unlocks:");
        translationBuilder.add("skillmmo.gui.skill.unlocks.show_unlocked", "Show unlocked");
        translationBuilder.add("skillmmo.gui.skill.unlocks.level", "Level %d");

        translationBuilder.add("skillmmo.keybindings.category", "SkillMMO");
        translationBuilder.add("skillmmo.keybindings.binding.open_skills", "Open Skills");

        translationBuilder.add("skillmmo.skill.agriculture.name", "Agriculture");
        translationBuilder.add("skillmmo.skill.agriculture.description", "Master the natural world!");
        translationBuilder.add("skillmmo.skill.animalhusbandry.name", "Animal Husbandry");
        translationBuilder.add("skillmmo.skill.animalhusbandry.description", "Domesticate and tame our precious wildlife!");
        translationBuilder.add("skillmmo.skill.building.name", "Building");
        translationBuilder.add("skillmmo.skill.building.description", "Construct your next masterpiece!");
        translationBuilder.add("skillmmo.skill.combat.name", "Combat");
        translationBuilder.add("skillmmo.skill.combat.description", "Gain an advantage on the battlefield!");
        translationBuilder.add("skillmmo.skill.engineering.name", "Engineering");
        translationBuilder.add("skillmmo.skill.engineering.description", "Develop mechanical contraptions!");
        translationBuilder.add("skillmmo.skill.mining.name", "Mining");
        translationBuilder.add("skillmmo.skill.mining.description", "Everything under the earth!");
        translationBuilder.add("skillmmo.skill.sorcery.name", "Sorcery");
        translationBuilder.add("skillmmo.skill.sorcery.description", "Control the magic and the mystical!");
        translationBuilder.add("skillmmo.skill.storage.name", "Storage");
        translationBuilder.add("skillmmo.skill.storage.description", "Create containers to hold your items!");
        translationBuilder.add("skillmmo.skill.survival.name", "Survival");
        translationBuilder.add("skillmmo.skill.survival.description", "Extend your life in comfort!");
        translationBuilder.add("skillmmo.skill.trading.name", "Trading");
        translationBuilder.add("skillmmo.skill.trading.description", "Exchange goods with the locals!");
    }
}
