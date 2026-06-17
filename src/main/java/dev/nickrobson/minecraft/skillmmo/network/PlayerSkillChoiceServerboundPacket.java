package dev.nickrobson.minecraft.skillmmo.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;

public record PlayerSkillChoiceServerboundPacket(
        Identifier skillId
) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PlayerSkillChoiceServerboundPacket> PACKET_ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(SkillMmoMod.MOD_ID, "player_skill_choice"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerSkillChoiceServerboundPacket> PACKET_CODEC = Identifier.STREAM_CODEC.map(PlayerSkillChoiceServerboundPacket::new, PlayerSkillChoiceServerboundPacket::skillId).cast();

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
