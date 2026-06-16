package dev.nickrobson.minecraft.skillmmo.data.generation.provider.skill;

import dev.nickrobson.minecraft.skillmmo.data.generation.spec.SkillMmoDefaultSkills;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

public class SkillMmoDefaultSkillProvider extends SkillProvider {
    public SkillMmoDefaultSkillProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    public String getName() {
        return "Default skills";
    }

    @Override
    protected void configure(HolderLookup.Provider lookup) {
        SkillMmoDefaultSkills.DEFAULT_SKILLS.forEach(this::addSkill);
    }
}
