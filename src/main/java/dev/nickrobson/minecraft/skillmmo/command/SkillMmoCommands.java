package dev.nickrobson.minecraft.skillmmo.command;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.util.Identifier;

public class SkillMmoCommands {
    public static void register() {
        ArgumentTypeRegistry.registerArgumentType(
                new Identifier(SkillMmoMod.MOD_ID, "skill"),
                SkillArgumentType.class,
                SkillArgumentType.SERIALIZER
        );

        CommandRegistrationCallback.EVENT.register((dispatcher, registry, environment) -> {
            dispatcher.register(SkillsCommand.defineSkillsCommand());
            dispatcher.register(SkillCommand.defineSkillCommand());
        });
    }
}
