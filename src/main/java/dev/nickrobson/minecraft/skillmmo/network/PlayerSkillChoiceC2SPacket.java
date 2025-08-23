package dev.nickrobson.minecraft.skillmmo.network;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record PlayerSkillChoiceC2SPacket(
        Identifier skillId
) implements CustomPayload {
    public static final CustomPayload.Id<PlayerSkillChoiceC2SPacket> PACKET_ID = new CustomPayload.Id<>(Identifier.of(SkillMmoMod.MOD_ID, "player_skill_choice"));
    public static final PacketCodec<RegistryByteBuf, PlayerSkillChoiceC2SPacket> PACKET_CODEC = Identifier.PACKET_CODEC.xmap(PlayerSkillChoiceC2SPacket::new, PlayerSkillChoiceC2SPacket::skillId).cast();

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
