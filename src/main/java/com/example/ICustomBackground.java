package com.example;

import net.minecraft.resources.ResourceLocation;

public interface ICustomBackground {
    public ResourceLocation getBackground();
    public void setBackground(ResourceLocation resourceLocation);

    public String getBackgroundFileName();
}
