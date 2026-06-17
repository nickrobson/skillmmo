package dev.nickrobson.minecraft.skillmmo.command;

import net.minecraft.resources.Identifier;

import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;

public class SkillMmoCommands {
    public static void register() {
        ArgumentTypeRegistry.registerArgumentType(
                Identifier.fromNamespaceAndPath(SkillMmoMod.MOD_ID, "skill"),
                SkillArgumentType.class,
                SkillArgumentType.SERIALIZER
        );

        CommandRegistrationCallback.EVENT.register((dispatcher, registry, environment) -> {
            dispatcher.register(SkillsCommand.defineSkillsCommand());
            dispatcher.register(SkillCommand.defineSkillCommand());
        });
    }
}
