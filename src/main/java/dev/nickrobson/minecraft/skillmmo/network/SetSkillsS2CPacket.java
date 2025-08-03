package dev.nickrobson.minecraft.skillmmo.network;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public record SetSkillsS2CPacket(
        Set<Skill> skills
) implements CustomPayload {
    private static final PacketCodec<RegistryByteBuf, Skill> SKILL_PACKET_CODEC = PacketCodec.ofStatic(SetSkillsS2CPacket::writeSkill, SetSkillsS2CPacket::readSkill);

    public static final CustomPayload.Id<SetSkillsS2CPacket> PACKET_ID = new CustomPayload.Id<>(new Identifier(SkillMmoMod.MOD_ID, "set_skills"));
    public static final PacketCodec<RegistryByteBuf, SetSkillsS2CPacket> PACKET_CODEC = SKILL_PACKET_CODEC.collect(PacketCodecs.toCollection(SetSkillsS2CPacket::toSet)).xmap(SetSkillsS2CPacket::new, SetSkillsS2CPacket::skills);

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    private static <T> Set<T> toSet(int capacity) {
        return new HashSet<>(capacity);
    }

    private static Skill readSkill(@Nonnull PacketByteBuf packetByteBuf) {
        Identifier id = packetByteBuf.readIdentifier();
        Text nameText = TextCodecs.PACKET_CODEC.decode(packetByteBuf);
        Text descriptionText = TextCodecs.PACKET_CODEC.decode(packetByteBuf);
        int maxLevel = packetByteBuf.readVarInt();
        Item iconItem = Registries.ITEM.get(packetByteBuf.readIdentifier());

        return new Skill(id, nameText, descriptionText, maxLevel, iconItem);
    }

    private static void writeSkill(@Nonnull PacketByteBuf packetByteBuf, @Nonnull Skill skill) {
        packetByteBuf.writeIdentifier(skill.getId());
        TextCodecs.PACKET_CODEC.encode(packetByteBuf, skill.getName());
        TextCodecs.PACKET_CODEC.encode(packetByteBuf, skill.getDescription());
        packetByteBuf.writeVarInt(skill.getMaxLevel());
        packetByteBuf.writeIdentifier(Registries.ITEM.getId(skill.getIconItem()));
    }
}
