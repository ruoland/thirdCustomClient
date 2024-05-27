package com.example;

import com.example.gui.event.FilesDropEvent;
import com.mojang.blaze3d.systems.RenderSystem;
import customclient.CustomClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;

import java.nio.file.Path;
import java.util.List;

public class ScreenNewTitle extends TitleScreen implements ICustomBackground, ICustomRenderable {

    protected ResourceLocation BACKGROUND_IMAGE = new ResourceLocation(CustomClient.MODID, "textures/screenshot.png");

    public ScreenNewTitle(){
        System.setProperty("java.awt.headless", "false");
    }
    @Override
    public void onFilesDrop(List<Path> pPacks) {
        super.onFilesDrop(pPacks);
        if(!NeoForge.EVENT_BUS.post(new FilesDropEvent(this, pPacks.get(0))).isCanceled()) {
            ScreenAPI.fileDrops(this, pPacks.get(0));

        }

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
            renderTexture(pGuiGraphics, BACKGROUND_IMAGE, 0, 0, width, height, 1);
        }
    }

    @Override
    protected void renderPanorama(GuiGraphics pGuiGraphics, float pPartialTick) {
        if(!hasBackground())
            super.renderPanorama(pGuiGraphics, pPartialTick);
    }

    public boolean hasBackground(){
        return (BACKGROUND_IMAGE != GuiData.DEFAULT_BACKGROUND_IMAGE);
    }


    protected static void renderTexture(GuiGraphics pGuiGraphics, ResourceLocation pShaderLocation, int x, int y, int width, int height, float pAlpha) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, pAlpha);

        pGuiGraphics.blit(pShaderLocation, x, y, -90, 0.0F, 0.0F, width, height, width, height);
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
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
