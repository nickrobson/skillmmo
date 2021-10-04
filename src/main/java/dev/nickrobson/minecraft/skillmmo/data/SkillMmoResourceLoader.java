package dev.nickrobson.minecraft.skillmmo.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

public class SkillMmoResourceLoader implements SimpleSynchronousResourceReloadListener {
    private static final Logger logger = LoggerFactory.getLogger(SkillMmoResourceLoader.class);

    private final Gson gson = new GsonBuilder().create();

    @Override
    public Identifier getFabricId() {
        return new Identifier("skillmmo", "skills");
    }

    @Override
    public void reload(ResourceManager manager) {
        Collection<SkillData> skillsData = loadSkillLevelUnlocks(manager, SkillMmoDataType.SKILLS);
        Collection<SkillLevelBlockUnlocksData> blockUnlocksData = loadSkillLevelUnlocks(manager, SkillMmoDataType.BLOCKS);
        Collection<SkillLevelItemUnlocksData> itemUnlocksData = loadSkillLevelUnlocks(manager, SkillMmoDataType.ITEMS);

        // TODO: merge everything...
    }

    private <T extends DataValidatable> Collection<T> loadSkillLevelUnlocks(ResourceManager manager, SkillMmoDataType<T> type) {
        Collection<Identifier> resourceIdentifiers = manager.findResources(
                type.getResourceCategory(),
                path -> path.endsWith(".json")
        );

        Collection<T> unlocksList = new ArrayList<>();
        for (Identifier resourceIdentifier : resourceIdentifiers) {
            try (InputStreamReader resourceReader = new InputStreamReader(manager.getResource(resourceIdentifier).getInputStream())) {
                T unlocks = gson.fromJson(resourceReader, type.getResourceClass());
                Collection<String> errors = new ArrayList<>();
                unlocks.validate(errors);
                if (errors.isEmpty()) {
                    unlocksList.add(unlocks);
                    logger.info("Loaded resource for {}: '{}'", type.getResourceCategory(), resourceIdentifier);
                } else {
                    logger.warn("Ignoring resource '{}' for {} due to errors:\n\t- {}", resourceIdentifier, type.getResourceCategory(), String.join("\n\t- ", errors));
                }
            } catch (Exception ex) {
                logger.error(new StringFormattedMessage("Failed to load resource '{}' for type '{}'", resourceIdentifier, type.getResourceCategory()).getFormattedMessage(), ex);
            }
        }
        return unlocksList;
    }
}
