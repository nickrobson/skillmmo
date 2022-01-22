package dev.nickrobson.minecraft.skillmmo.network;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import dev.nickrobson.minecraft.skillmmo.experience.ExperienceLevelEquation;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import javax.annotation.Nonnull;

public interface SkillMmoNetworking {
    Identifier LOGIN_HANDSHAKE = new Identifier(SkillMmoMod.MOD_ID, "handshake");

    Identifier C2S_PLAYER_SKILL_CHOICE = new Identifier(SkillMmoMod.MOD_ID, "player_skill_choice");

    Identifier S2C_SKILLS = new Identifier(SkillMmoMod.MOD_ID, "skills");
    Identifier S2C_EXPERIENCE_LEVEL_EQUATION = new Identifier(SkillMmoMod.MOD_ID, "experience_level_equation");
    Identifier S2C_PLAYER_SKILLS = new Identifier(SkillMmoMod.MOD_ID, "player_skills");
    Identifier S2C_PLAYER_XP = new Identifier(SkillMmoMod.MOD_ID, "player_xp");

    static Skill readSkill(@Nonnull PacketByteBuf packetByteBuf) {
        Identifier id = packetByteBuf.readIdentifier();
        Text nameText = packetByteBuf.readText();
        Text descriptionText = packetByteBuf.readText();
        int maxLevel = packetByteBuf.readVarInt();
        Item iconItem = Item.byRawId(packetByteBuf.readVarInt());

        return new Skill(id, nameText, descriptionText, maxLevel, iconItem);
    }

    static void writeSkill(@Nonnull PacketByteBuf packetByteBuf, @Nonnull Skill skill) {
        packetByteBuf.writeIdentifier(skill.getId());
        packetByteBuf.writeText(skill.getName());
        packetByteBuf.writeText(skill.getDescription());
        packetByteBuf.writeVarInt(skill.getMaxLevel());
        packetByteBuf.writeVarInt(Item.getRawId(skill.getIconItem()));
    }

    static ExperienceLevelEquation readExperienceLevelEquation(@Nonnull PacketByteBuf packetByteBuf) {
        long baseCost = packetByteBuf.readLong();
        double multiplier = packetByteBuf.readDouble();
        double levelExponent = packetByteBuf.readDouble();

        return new ExperienceLevelEquation(baseCost, multiplier, levelExponent);
    }

    static void writeExperienceLevelEquation(@Nonnull PacketByteBuf packetByteBuf, @Nonnull ExperienceLevelEquation equation) {
        packetByteBuf.writeLong(equation.getBaseCost());
        packetByteBuf.writeDouble(equation.getMultiplier());
        packetByteBuf.writeDouble(equation.getLevelExponent());
    }
}
