package dev.nickrobson.minecraft.skillmmo.network;

import dev.nickrobson.minecraft.skillmmo.experience.ExperienceLevelEquation;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayerConfigurationTask;

import java.util.Set;
import java.util.function.Consumer;

public record SkillMmoConfigurationTask(
        String version,
        Set<Skill> skills,
        ExperienceLevelEquation experienceLevelEquation
) implements ServerPlayerConfigurationTask {
    public static final ServerPlayerConfigurationTask.Key KEY = new ServerPlayerConfigurationTask.Key("skillmmo:modVersion");

    @Override
    public Key getKey() {
        return KEY;
    }

    @Override
    public void sendPacket(Consumer<Packet<?>> sender) {
        sender.accept(ServerConfigurationNetworking.createS2CPacket(
                new SkillMmoConfigurationS2CPacket(version, skills, experienceLevelEquation)
        ));
    }
}
