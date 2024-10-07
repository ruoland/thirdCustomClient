package com.example;

import com.example.screen.CustomScreenMod;
import com.example.screen.ScreenAPI;
import com.example.screen.ScreenFlow;
import customclient.CustomClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.List;

public class ScreenNewTitle extends TitleScreen implements ICustomBackground, ICustomRenderable {

    protected ResourceLocation BACKGROUND_IMAGE = new ResourceLocation(CustomClient.MODID, "textures/screenshot.png");
    private ScreenFlow screenFlow =  CustomScreenMod.getScreen(this);
    public ScreenNewTitle(){
        try {

            Field field = getClass().getSuperclass().getSuperclass().getDeclaredField("title");
            field.setAccessible(true);
            field.set(this, Component.literal("ScreenNewTitle"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.setProperty("java.awt.headless", "false");
    }

    @Override
    public  <T extends GuiEventListener & NarratableEntry> T addWidget(T pListener) {
        return super.addWidget(pListener);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T pWidget) {
        return super.addRenderableWidget(pWidget);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        //얘는 별개로 한번 더 아래 코드가 필요함
        if(screenFlow == null) {
            screenFlow = CustomScreenMod.getScreen(this);
        }


    }



    @Override
    public void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if(hasBackground()) {
            renderBlurredBackground(pPartialTick);
            ScreenAPI.renderTexture(pGuiGraphics, BACKGROUND_IMAGE, 0, 0, 0, width, height, 1);
        }
        else
            super.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    protected void renderPanorama(GuiGraphics pGuiGraphics, float pPartialTick) {
        if(!hasBackground())
            super.renderPanorama(pGuiGraphics, pPartialTick);
    }

    public boolean hasBackground(){
        return !(BACKGROUND_IMAGE.equals(CustomScreenMod.DEFAULT_BACKGROUND_IMAGE));
    }


    @Override
    public void onFilesDrop(List<Path> pPacks) {
        super.onFilesDrop(pPacks);
        screenFlow.fileDropEvent(pPacks.get(0));
        //setBackground(ScreenAPI.getDynamicTexture(pPacks.get(0)));
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
