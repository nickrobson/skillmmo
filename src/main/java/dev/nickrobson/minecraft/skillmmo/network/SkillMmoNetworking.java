package dev.nickrobson.minecraft.skillmmo.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public interface SkillMmoNetworking {
    static void registerPackets() {
        // Configuration - S2C
        PayloadTypeRegistry.configurationS2C().register(SkillMmoConfigurationS2CPacket.PACKET_ID, SkillMmoConfigurationS2CPacket.PACKET_CODEC);

        // Configuration - C2S
        PayloadTypeRegistry.configurationC2S().register(SkillMmoConfigurationC2SPacket.PACKET_ID, SkillMmoConfigurationC2SPacket.PACKET_CODEC);

        // Play - S2C
        PayloadTypeRegistry.playS2C().register(SetSkillsS2CPacket.PACKET_ID, SetSkillsS2CPacket.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(SetExperienceLevelEquationS2CPacket.PACKET_ID, SetExperienceLevelEquationS2CPacket.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(SetPlayerSkillsS2CPacket.PACKET_ID, SetPlayerSkillsS2CPacket.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(SetPlayerExperienceS2CPacket.PACKET_ID, SetPlayerExperienceS2CPacket.PACKET_CODEC);

        // Play - C2S
        PayloadTypeRegistry.playC2S().register(PlayerSkillChoiceC2SPacket.PACKET_ID, PlayerSkillChoiceC2SPacket.PACKET_CODEC);
    }
}
