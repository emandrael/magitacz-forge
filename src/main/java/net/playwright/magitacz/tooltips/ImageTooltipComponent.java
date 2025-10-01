package net.playwright.magitacz.tooltips;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;

public class ImageTooltipComponent implements ClientTooltipComponent {
    private final ResourceLocation imageLocation;
    private final int width;
    private final int height;

    public ImageTooltipComponent(ResourceLocation imageLocation, int width, int height) {
        this.imageLocation = imageLocation;
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth(Font font) {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        guiGraphics.blit(imageLocation, x, y, 0, 0, width, height, width, height);        // Render the image at the specified position
        // The rendering code depends on your specific requirements
    }
}