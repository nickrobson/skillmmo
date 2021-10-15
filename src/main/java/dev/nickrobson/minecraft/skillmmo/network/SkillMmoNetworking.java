package dev.nickrobson.minecraft.skillmmo.network;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import dev.nickrobson.minecraft.skillmmo.experience.ExperienceLevelEquation;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevel;
import dev.nickrobson.minecraft.skillmmo.skill.UnlockType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface SkillMmoNetworking {
    Identifier C2S_PLAYER_SKILL_CHOICE = new Identifier(SkillMmoMod.MOD_ID, "player_skill_choice");

    Identifier S2C_SKILLS = new Identifier(SkillMmoMod.MOD_ID, "skills");
    Identifier S2C_EXPERIENCE_LEVEL_EQUATION = new Identifier(SkillMmoMod.MOD_ID, "experience_level_equation");
    Identifier S2C_PLAYER_SKILLS = new Identifier(SkillMmoMod.MOD_ID, "player_skills");
    Identifier S2C_PLAYER_XP = new Identifier(SkillMmoMod.MOD_ID, "player_xp");

    static Skill readSkill(@Nonnull PacketByteBuf packetByteBuf) {
        Identifier id = packetByteBuf.readIdentifier();
        String nameKey = packetByteBuf.readString();
        String descriptionKey = packetByteBuf.readString();
        Set<SkillLevel> skillLevels = packetByteBuf.readCollection(HashSet::new, buf -> readSkillLevel(id, buf));

        return new Skill(id, nameKey, descriptionKey, skillLevels);
    }

    static void writeSkill(@Nonnull PacketByteBuf packetByteBuf, @Nonnull Skill skill) {
        packetByteBuf.writeIdentifier(skill.getId());
        packetByteBuf.writeString(skill.getNameKey());
        packetByteBuf.writeString(skill.getDescriptionKey());
        packetByteBuf.writeCollection(skill.getSkillLevels(), SkillMmoNetworking::writeSkillLevel);
    }

    static SkillLevel readSkillLevel(Identifier skillId, @Nonnull PacketByteBuf packetByteBuf) {
        int level = packetByteBuf.readVarInt();
        Map<UnlockType, Set<Identifier>> unlocks = packetByteBuf.readMap(
                HashMap::new,
                buf -> buf.readEnumConstant(UnlockType.class),
                buf -> buf.readCollection(HashSet::new, PacketByteBuf::readIdentifier)
        );

        return new SkillLevel(skillId, level, unlocks);
    }

    static void writeSkillLevel(@Nonnull PacketByteBuf packetByteBuf, @Nonnull SkillLevel skillLevel) {
        packetByteBuf.writeVarInt(skillLevel.getLevel());
        packetByteBuf.writeMap(
                skillLevel.getUnlocks(),
                PacketByteBuf::writeEnumConstant,
                (buf, ids) -> buf.writeCollection(ids, PacketByteBuf::writeIdentifier)
        );
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
