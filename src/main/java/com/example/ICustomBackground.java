package com.example;

import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.List;

public interface ICustomBackground {
    public ResourceLocation getBackground();
    public void setBackground(ResourceLocation resourceLocation);
    public void onFilesDrop(List<Path> pPacks);
}
