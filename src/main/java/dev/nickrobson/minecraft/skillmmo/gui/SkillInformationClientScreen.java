package dev.nickrobson.minecraft.skillmmo.gui;

import dev.nickrobson.minecraft.skillmmo.api.unlockable.VanillaUnlockables;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevel;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.WDynamicLabel;
import io.github.cottonmc.cotton.gui.widget.WItem;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WScrollPanel;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class SkillInformationClientScreen extends CottonClientScreen {
    public static void open(ClientPlayerEntity player, Skill skill) {
        open(player, skill, MinecraftClient.getInstance().currentScreen);
    }

    public static void open(ClientPlayerEntity player, Skill skill, @Nullable Screen parent) {
        MinecraftClient.getInstance().setScreen(new SkillInformationClientScreen(player, skill, parent));
    }

    @Nullable
    private final Screen parent;

    public SkillInformationClientScreen(ClientPlayerEntity player, Skill skill, @Nullable Screen parent) {
        super(new SkillInformationGui(player, skill));
        this.parent = parent;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class SkillInformationGui extends LightweightGuiDescription {
        private static final int GRID_SIZE = 18;

        private static final int ROOT_WIDTH = 12;
        private static final int SKILL_LEVEL_WIDTH = 2;
        private static final int ICON_GRID_SIZE = 1;
        private static final int ITEMS_PER_ROW = ROOT_WIDTH - 1;

        public SkillInformationGui(ClientPlayerEntity player, Skill skill) {
            WBox root = new WBox(Axis.VERTICAL);
            setRootPanel(root);
            root.setInsets(new Insets(4));

            root.add(
                    createSkillInfoPanel(player, skill),
                    GRID_SIZE * ROOT_WIDTH,
                    GRID_SIZE * 2
            );

            root.add(
                    createUnlocksPanel(player, skill),
                    GRID_SIZE * ROOT_WIDTH,
                    GRID_SIZE * 7 + 9
            );

            root.validate(this);
        }

        private WWidget createSkillInfoPanel(ClientPlayerEntity player, Skill skill) {
            WPlainPanel infoPanel = new WPlainPanel();
            infoPanel.setInsets(new Insets(2, 4));

            infoPanel.add(
                    new WItem(new ItemStack(skill.getIconItem())),
                    0,
                    0,
                    GRID_SIZE * ICON_GRID_SIZE,
                    GRID_SIZE * ICON_GRID_SIZE
            );

            infoPanel.add(
                    new WLabel(skill.getName())
                            .setHorizontalAlignment(HorizontalAlignment.LEFT)
                            .setVerticalAlignment(VerticalAlignment.CENTER),
                    GRID_SIZE * ICON_GRID_SIZE + 3,
                    0,
                    GRID_SIZE * (ROOT_WIDTH - ICON_GRID_SIZE - SKILL_LEVEL_WIDTH) - 4,
                    GRID_SIZE
            );

            infoPanel.add(
                    new WDynamicLabel(() -> I18n.translate("skillmmo.gui.skill.info.current_level", PlayerSkillManager.getInstance().getSkillLevel(player, skill), skill.getMaxLevel()))
                            .setAlignment(HorizontalAlignment.RIGHT),
                    GRID_SIZE * (ROOT_WIDTH - SKILL_LEVEL_WIDTH),
                    5,
                    GRID_SIZE * SKILL_LEVEL_WIDTH - 3,
                    GRID_SIZE
            );

            infoPanel.add(
                    new WLabel(skill.getDescription())
                            .setHorizontalAlignment(HorizontalAlignment.LEFT)
                            .setVerticalAlignment(VerticalAlignment.CENTER),
                    0,
                    GRID_SIZE,
                    GRID_SIZE * ROOT_WIDTH,
                    GRID_SIZE
            );

            return infoPanel;
        }

        private WWidget createUnlocksPanel(ClientPlayerEntity player, Skill skill) {
            WPlainPanel skillUnlocksPanel = new WPlainPanel();

            WWidget skillLevelsPanel = createSkillLevelsPanel(skill.getSkillLevels());
            skillUnlocksPanel.add(
                    skillLevelsPanel,
                    0,
                    0,
                    GRID_SIZE * ROOT_WIDTH,
                    GRID_SIZE * 7 + 4
            );

            return skillUnlocksPanel;
        }

        private WWidget createSkillLevelsPanel(List<SkillLevel> skillLevels) {
            WBox skillLevelsPanel = new WBox(Axis.VERTICAL);
            skillLevelsPanel.setInsets(new Insets(2, 4));

            for (SkillLevel skillLevel : skillLevels) {
                List<ItemStack> items = Stream.concat(
                                explodeTagItems(Registries.BLOCK, skillLevel.getUnlocksTag(VanillaUnlockables.BLOCK)),
                                explodeTagItems(Registries.ITEM, skillLevel.getUnlocksTag(VanillaUnlockables.ITEM))
                        )
                        .sorted(Comparator.comparing(Registries.ITEM::getRawId))
                        .distinct()
                        .map(ItemStack::new)
                        .filter(itemStack -> !itemStack.isEmpty())
                        .toList();

                if (items.isEmpty()) {
                    continue;
                }

                WPlainPanel skillLevelUnlocksPanel = new WPlainPanel();
                skillLevelUnlocksPanel.add(
                        new WLabel(Text.translatable("skillmmo.gui.skill.unlocks.level", skillLevel.getLevel()))
                                .setVerticalAlignment(VerticalAlignment.TOP)
                                .setHorizontalAlignment(HorizontalAlignment.LEFT),
                        0,
                        0,
                        GRID_SIZE * 3,
                        GRID_SIZE - 4
                );

                for (ListIterator<ItemStack> it = items.listIterator(); it.hasNext(); ) {
                    int i = it.nextIndex();
                    ItemStack itemStack = it.next();
                    int rowOffset = i / ITEMS_PER_ROW;
                    int columnOffset = i % ITEMS_PER_ROW;

                    skillLevelUnlocksPanel.add(
                            new WItemWithTooltip(itemStack)
                                    .setTooltipText(itemStack.getName()),
                            GRID_SIZE * columnOffset,
                            GRID_SIZE * (rowOffset + 1) - 4,
                            GRID_SIZE * ICON_GRID_SIZE,
                            GRID_SIZE * ICON_GRID_SIZE
                    );
                }

                skillLevelsPanel.add(skillLevelUnlocksPanel);
            }

            return new WScrollPanel(skillLevelsPanel)
                    .setScrollingHorizontally(TriState.FALSE)
                    .setScrollingVertically(TriState.DEFAULT);
        }

        private <T extends ItemConvertible> Stream<Item> explodeTagItems(Registry<T> registry, TagKey<T> tag) {
            Optional<RegistryEntryList.Named<T>> entryListOpt = registry.getEntryList(tag);

            if (entryListOpt.isEmpty()) {
                return Stream.empty();
            }

            return entryListOpt.get().stream()
                    .map(RegistryEntry::value)
                    .filter(Objects::nonNull)
                    .map(ItemConvertible::asItem);
        }
    }
}
