package dev.nickrobson.minecraft.skillmmo.data.generation.provider.skill;

import com.google.gson.JsonElement;
import dev.nickrobson.minecraft.skillmmo.data.SkillMmoResourceLoader;
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
    private final SkillMmoResourceLoader skillMmoResourceLoader;

    public SkillProvider(
            DataOutput output,
            CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture
    ) {
        this.pathResolver = output.getResolver(DataOutput.OutputType.DATA_PACK, "skills");
        this.registryLookupFuture = registryLookupFuture;
        this.skillSpecs = new ArrayList<>();
        this.skillMmoResourceLoader = new SkillMmoResourceLoader();
    }

    @Override
    public String getName() {
        return "SkillMMO skill provider";
    }

    protected abstract void configure(RegistryWrapper.WrapperLookup lookup);

    protected final void addSkill(SkillDataGenSpec spec) {
        this.skillSpecs.add(spec);
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return registryLookupFuture
                .thenAccept(registryWrapper -> {
                    skillSpecs.clear();
                    this.configure(registryWrapper);
                })
                .thenCompose((_void) -> CompletableFuture.allOf(
                                this.skillSpecs
                                        .stream()
                                        .map(
                                                spec -> {
                                                    JsonElement jsonElement = skillMmoResourceLoader.getGson().toJsonTree(spec.toSkillData());
                                                    Path path = this.pathResolver.resolveJson(spec.id());
                                                    return DataProvider.writeToPath(writer, jsonElement, path);
                                                }
                                        )
                                        .toArray(CompletableFuture[]::new)
                        )
                );
    }
}
