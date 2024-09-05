package com.example.userscreen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class ScreenCustomHUD extends ScreenCustom{
    protected ScreenCustomHUD(Component pTitle) {
        super(pTitle);
    }


    @Override
    public void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
    }

    @Override
    public boolean hasBackground() {
        return false;
    }
}
