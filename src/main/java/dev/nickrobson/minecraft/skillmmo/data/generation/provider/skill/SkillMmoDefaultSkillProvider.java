package dev.nickrobson.minecraft.skillmmo.data.generation.provider.skill;

import dev.nickrobson.minecraft.skillmmo.data.generation.spec.SkillMmoDefaultSkills;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class SkillMmoDefaultSkillProvider extends SkillProvider {
    public SkillMmoDefaultSkillProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    public String getName() {
        return "Default skills";
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        SkillMmoDefaultSkills.DEFAULT_SKILLS.forEach(this::addSkill);
    }
}
