package dev.nickrobson.minecraft.skillmmo.data.generation.provider.skill;

import dev.nickrobson.minecraft.skillmmo.data.SkillData;
import dev.nickrobson.minecraft.skillmmo.data.generation.spec.SkillDataGenSpec;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryWrapper;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class SkillProvider implements DataProvider {
    private final DataOutput.PathResolver pathResolver;
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture;
    private final List<SkillDataGenSpec> skillSpecs;

    public SkillProvider(
            DataOutput output,
            CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture
    ) {
        this.pathResolver = output.getResolver(DataOutput.OutputType.DATA_PACK, "skills");
        this.registryLookupFuture = registryLookupFuture;
        this.skillSpecs = new ArrayList<>();
    }

    protected abstract void configure(RegistryWrapper.WrapperLookup lookup);

    protected final void addSkill(SkillDataGenSpec spec) {
        this.skillSpecs.add(spec);
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return registryLookupFuture
                .thenCompose(registryWrapper -> {
                            skillSpecs.clear();
                            this.configure(registryWrapper);
                            return CompletableFuture.allOf(
                                    this.skillSpecs
                                            .stream()
                                            .map(
                                                    spec -> {
                                                        Path path = this.pathResolver.resolveJson(spec.id());
                                                        return DataProvider.writeCodecToPath(writer, registryWrapper, SkillData.CODEC, spec.toSkillData(), path);
                                                    }
                                            )
                                            .toArray(CompletableFuture[]::new)
                            );
                        }
                );
    }
}
