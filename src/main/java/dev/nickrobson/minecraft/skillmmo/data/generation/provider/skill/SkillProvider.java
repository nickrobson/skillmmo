package dev.nickrobson.minecraft.skillmmo.data.generation.provider.skill;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import dev.nickrobson.minecraft.skillmmo.data.SkillData;
import dev.nickrobson.minecraft.skillmmo.data.generation.spec.SkillDataGenSpec;

public abstract class SkillProvider implements DataProvider {
    private final PackOutput.PathProvider pathResolver;
    private final CompletableFuture<HolderLookup.Provider> registryLookupFuture;
    private final List<SkillDataGenSpec> skillSpecs;

    public SkillProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registryLookupFuture
    ) {
        this.pathResolver = output.createPathProvider(PackOutput.Target.DATA_PACK, "skills");
        this.registryLookupFuture = registryLookupFuture;
        this.skillSpecs = new ArrayList<>();
    }

    protected abstract void configure(HolderLookup.Provider lookup);

    protected final void addSkill(SkillDataGenSpec spec) {
        this.skillSpecs.add(spec);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        return registryLookupFuture
                .thenCompose(registryWrapper -> {
                            skillSpecs.clear();
                            this.configure(registryWrapper);
                            return CompletableFuture.allOf(
                                    this.skillSpecs
                                            .stream()
                                            .map(
                                                    spec -> {
                                                        Path path = this.pathResolver.json(spec.id());
                                                        return DataProvider.saveStable(writer, registryWrapper, SkillData.CODEC, spec.toSkillData(), path);
                                                    }
                                            )
                                            .toArray(CompletableFuture[]::new)
                            );
                        }
                );
    }
}
