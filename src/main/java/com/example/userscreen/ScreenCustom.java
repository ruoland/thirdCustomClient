package com.example.userscreen;

import com.example.ICustomBackground;
import com.example.screen.CustomScreenMod;
import com.example.screen.ScreenAPI;
import com.example.screen.ScreenFlow;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.List;

public class ScreenCustom extends Screen implements ICustomBackground {
    private ResourceLocation BACKGROUND_IMAGE = new ResourceLocation("customclient", "textures/screenshot.png");
    protected ScreenFlow screenFlow;
    protected ScreenCustom(Component pTitle) {
        super(pTitle);
    }
    @Override
    public ResourceLocation getBackground() {
        return BACKGROUND_IMAGE;
    }

    @Override
    public void setBackground(ResourceLocation resourceLocation) {
        BACKGROUND_IMAGE = resourceLocation;
    }

    @Override
    public void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if(hasBackground()) {
            renderBlurredBackground(pPartialTick);
            ScreenAPI.renderTexture(pGuiGraphics, BACKGROUND_IMAGE, 0, 0, 0, width, height, 1);
        }
    }
    public boolean hasBackground(){
        return !(BACKGROUND_IMAGE.equals(CustomScreenMod.DEFAULT_BACKGROUND_IMAGE));
    }

    @Override
    public void onFilesDrop(List<Path> pPacks) {
        super.onFilesDrop(pPacks);
        screenFlow.fileDropEvent(pPacks.get(0));
    }

}
