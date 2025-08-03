package dev.nickrobson.minecraft.skillmmo.network;

import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayerConfigurationTask;

import java.util.function.Consumer;

public record SkillMmoConfigurationTask(
        String version
) implements ServerPlayerConfigurationTask {
    public static final ServerPlayerConfigurationTask.Key KEY = new ServerPlayerConfigurationTask.Key("skillmmo:modVersion");

    @Override
    public Key getKey() {
        return KEY;
    }

    @Override
    public void sendPacket(Consumer<Packet<?>> sender) {
        sender.accept(ServerConfigurationNetworking.createS2CPacket(new SkillMmoConfigurationS2CPacket(version)));
    }
}
