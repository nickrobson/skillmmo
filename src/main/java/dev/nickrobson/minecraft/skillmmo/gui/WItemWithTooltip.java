package dev.nickrobson.minecraft.skillmmo.gui;

import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class WItemWithTooltip extends WItem {
    private Component tooltipText;

    public WItemWithTooltip(ItemStack stack) {
        super(stack);
    }

    public WItemWithTooltip setTooltipText(Component tooltipText) {
        this.tooltipText = tooltipText;
        return this;
    }

    @Override
    public void addTooltip(TooltipBuilder tooltip) {
        if (tooltip != null) {
            tooltip.add(tooltipText);
        }
    }
}
