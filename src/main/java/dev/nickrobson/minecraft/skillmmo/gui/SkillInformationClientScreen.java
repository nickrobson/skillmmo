package dev.nickrobson.minecraft.skillmmo.gui;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.util.TriState;

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

@Environment(EnvType.CLIENT)
public class SkillInformationClientScreen extends CottonClientScreen {
    public static void open(LocalPlayer player, Skill skill) {
        open(player, skill, Minecraft.getInstance().screen);
    }

    public static void open(LocalPlayer player, Skill skill, @Nullable Screen parent) {
        Minecraft.getInstance().setScreen(new SkillInformationClientScreen(player, skill, parent));
    }

    @Nullable
    private final Screen parent;

    public SkillInformationClientScreen(LocalPlayer player, Skill skill, @Nullable Screen parent) {
        super(new SkillInformationGui(player, skill));
        this.parent = parent;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(parent);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class SkillInformationGui extends LightweightGuiDescription {
        private static final int GRID_SIZE = 18;

        private static final int ROOT_WIDTH = 12;
        private static final int SKILL_LEVEL_WIDTH = 2;
        private static final int ICON_GRID_SIZE = 1;
        private static final int ITEMS_PER_ROW = ROOT_WIDTH - 1;

        public SkillInformationGui(LocalPlayer player, Skill skill) {
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

        private WWidget createSkillInfoPanel(LocalPlayer player, Skill skill) {
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
                    new WDynamicLabel(() -> I18n.get("skillmmo.gui.skill.info.current_level", PlayerSkillManager.getInstance().getSkillLevel(player, skill), skill.getMaxLevel()))
                            .setHorizontalAlignment(HorizontalAlignment.RIGHT),
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

        private WWidget createUnlocksPanel(LocalPlayer player, Skill skill) {
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
                                explodeTagItems(BuiltInRegistries.BLOCK, skillLevel.getUnlocksTag(VanillaUnlockables.BLOCK)),
                                explodeTagItems(BuiltInRegistries.ITEM, skillLevel.getUnlocksTag(VanillaUnlockables.ITEM))
                        )
                        .sorted(Comparator.comparing(BuiltInRegistries.ITEM::getId))
                        .distinct()
                        .map(ItemStack::new)
                        .filter(itemStack -> !itemStack.isEmpty())
                        .toList();

                if (items.isEmpty()) {
                    continue;
                }

                WPlainPanel skillLevelUnlocksPanel = new WPlainPanel();
                skillLevelUnlocksPanel.add(
                        new WLabel(Component.translatable("skillmmo.gui.skill.unlocks.level", skillLevel.getLevel()))
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
                                    .setTooltipText(itemStack.getHoverName()),
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

        private <T extends ItemLike> Stream<Item> explodeTagItems(Registry<T> registry, TagKey<T> tag) {
            Optional<HolderSet.Named<T>> entryListOpt = registry.get(tag);

            if (entryListOpt.isEmpty()) {
                return Stream.empty();
            }

            return entryListOpt.get().stream()
                    .map(Holder::value)
                    .filter(Objects::nonNull)
                    .map(ItemLike::asItem);
        }
    }
}
