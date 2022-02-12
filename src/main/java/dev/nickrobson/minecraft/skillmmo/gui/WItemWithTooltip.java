package dev.nickrobson.minecraft.skillmmo.gui;

import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class WItemWithTooltip extends WItem {
    private Text tooltipText;

    public WItemWithTooltip(ItemStack stack) {
        super(stack);
    }

    public WItemWithTooltip setTooltipText(Text tooltipText) {
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
