package dev.nickrobson.minecraft.skillmmo.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;

public record PlayerSkillChoiceC2SPacket(
        Identifier skillId
) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PlayerSkillChoiceC2SPacket> PACKET_ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(SkillMmoMod.MOD_ID, "player_skill_choice"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerSkillChoiceC2SPacket> PACKET_CODEC = Identifier.STREAM_CODEC.map(PlayerSkillChoiceC2SPacket::new, PlayerSkillChoiceC2SPacket::skillId).cast();

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
