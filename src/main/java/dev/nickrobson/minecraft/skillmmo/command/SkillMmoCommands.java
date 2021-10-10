package dev.nickrobson.minecraft.skillmmo.command;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.util.Identifier;

public class SkillMmoCommands {
    public static void register() {
        ArgumentTypes.register(
                new Identifier(SkillMmoMod.MOD_ID, "skill").toString(),
                SkillArgumentType.class,
                new SkillArgumentType.Serializer()
        );

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(SkillsCommand.defineSkillsCommand());
            dispatcher.register(SkillCommand.defineSkillCommand());
        });
    }
}
