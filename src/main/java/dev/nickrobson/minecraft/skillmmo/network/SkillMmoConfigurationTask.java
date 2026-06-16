package dev.nickrobson.minecraft.skillmmo.network;

import dev.nickrobson.minecraft.skillmmo.experience.ExperienceLevelEquation;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ConfigurationTask;
import java.util.Set;
import java.util.function.Consumer;

public record SkillMmoConfigurationTask(
        String version,
        Set<Skill> skills,
        ExperienceLevelEquation experienceLevelEquation
) implements ConfigurationTask {
    public static final ConfigurationTask.Type KEY = new ConfigurationTask.Type("skillmmo:modVersion");

    @Override
    public Type type() {
        return KEY;
    }

    @Override
    public void start(Consumer<Packet<?>> sender) {
        sender.accept(ServerConfigurationNetworking.createS2CPacket(
                new SkillMmoConfigurationS2CPacket(version, skills, experienceLevelEquation)
        ));
    }
}
