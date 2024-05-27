package com.example;

import com.example.gui.event.FilesDropEvent;
import com.mojang.blaze3d.systems.RenderSystem;
import customclient.CustomClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.List;

public class ScreenNewTitle extends TitleScreen implements ICustomBackground, ICustomRenderable {

    protected ResourceLocation BACKGROUND_IMAGE = new ResourceLocation(CustomClient.MODID, "textures/screenshot.png");

    public ScreenNewTitle(){
        try {
            System.out.println(getClass().getSuperclass().getSuperclass());
            Field field = getClass().getSuperclass().getSuperclass().getDeclaredField("title");
            field.setAccessible(true);
            field.set(this, Component.literal("ScreenNewTitle"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.setProperty("java.awt.headless", "false");
    }


    @Override
    protected void init() {
        super.init();
    }

    @Override
    public <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T pWidget) {
        return super.addRenderableWidget(pWidget);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if(hasBackground()) {
            renderBlurredBackground(pPartialTick);
            ScreenAPI.renderTexture(pGuiGraphics, BACKGROUND_IMAGE, 0, 0, width, height, 1);
        }
    }

    @Override
    protected void renderPanorama(GuiGraphics pGuiGraphics, float pPartialTick) {
        if(!hasBackground())
            super.renderPanorama(pGuiGraphics, pPartialTick);
    }

    public boolean hasBackground(){
        return (BACKGROUND_IMAGE != CustomScreenMod.DEFAULT_BACKGROUND_IMAGE);
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
