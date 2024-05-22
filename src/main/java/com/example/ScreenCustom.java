package com.example;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ScreenCustom extends Screen implements ICustomBackground{
    protected ScreenCustom(Component pTitle) {
        super(pTitle);
    }

    @Override
    public ResourceLocation getBackground() {
        return null;
    }

    @Override
    public void setBackground(ResourceLocation resourceLocation) {

    }
}
