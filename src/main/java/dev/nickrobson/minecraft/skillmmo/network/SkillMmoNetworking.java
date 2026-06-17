package dev.nickrobson.minecraft.skillmmo.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public interface SkillMmoNetworking {
    static void registerPackets() {
        // Configuration - clientbound
        PayloadTypeRegistry.clientboundConfiguration().register(SkillMmoConfigurationClientboundPacket.PACKET_ID, SkillMmoConfigurationClientboundPacket.PACKET_CODEC);

        // Configuration - serverbound
        PayloadTypeRegistry.serverboundConfiguration().register(SkillMmoConfigurationServerboundPacket.PACKET_ID, SkillMmoConfigurationServerboundPacket.PACKET_CODEC);

        // Play - clientbound
        PayloadTypeRegistry.clientboundPlay().register(SetPlayerSkillsClientboundPacket.PACKET_ID, SetPlayerSkillsClientboundPacket.PACKET_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(SetPlayerExperienceClientboundPacket.PACKET_ID, SetPlayerExperienceClientboundPacket.PACKET_CODEC);

        // Play - serverbound
        PayloadTypeRegistry.serverboundPlay().register(PlayerSkillChoiceServerboundPacket.PACKET_ID, PlayerSkillChoiceServerboundPacket.PACKET_CODEC);
    }
}
