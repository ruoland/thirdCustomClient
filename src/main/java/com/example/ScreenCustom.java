package com.example;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.List;

public class ScreenCustom extends Screen implements ICustomBackground{
    private ResourceLocation BACKGROUND_IMAGE = new ResourceLocation("customclient", "textures/screenshot.png");
    private String backgroundFileName;
    protected ScreenCustom(Component pTitle) {
        super(pTitle);
    }

    @Override
    public void onFilesDrop(List<Path> pPacks) {
        super.onFilesDrop(pPacks);
        backgroundFileName = pPacks.get(0).getFileName().toString();
    }

    @Override
    public ResourceLocation getBackground() {
        return BACKGROUND_IMAGE;
    }

    @Override
    public void setBackground(ResourceLocation resourceLocation) {
        BACKGROUND_IMAGE = resourceLocation;
    }

}
