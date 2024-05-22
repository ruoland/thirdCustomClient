package com.example;

import com.example.gui.event.FilesDropEvent;
import com.example.gui.event.ImageWidgetEvent;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import customclient.CustomClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ScreenNewTitle extends TitleScreen implements ICustomBackground {

    protected ResourceLocation BACKGROUND_IMAGE = new ResourceLocation(CustomClient.MODID, "textures/screenshot.png");
    private static final Path CUSTOM_CLIENT_PATH = Paths.get("D:\\Projects\\thirdCustomClient\\src\\main\\resources\\assets\\customclient/");

    ScreenNewTitle(){
        System.setProperty("java.awt.headless", "false");
    }
    @Override
    public void onFilesDrop(List<Path> pPacks) {
        super.onFilesDrop(pPacks);
        if(!NeoForge.EVENT_BUS.post(new FilesDropEvent(this, pPacks)).isCanceled()) {
            ResourceLocation resourceLocation = getTexture(pPacks.get(0));

            int select = JOptionPane.showOptionDialog(null, "어떤 걸로 설정할까요?", "이미지 불러오기", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"배경화면", "이미지", "취소"}, "취소");

            switch (select) {
                case JOptionPane.YES_OPTION -> {
                    BACKGROUND_IMAGE = resourceLocation;
                }
                case JOptionPane.NO_OPTION -> {
                }
            }
            if(select == JOptionPane.YES_OPTION)
                NeoForge.EVENT_BUS.post(new ImageWidgetEvent.Background(this, resourceLocation, pPacks.get(0)));
            else
                NeoForge.EVENT_BUS.post(new ImageWidgetEvent.Image(this, resourceLocation, pPacks.get(0)));
        }
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
    public ResourceLocation getTexture(Path dropFile){
        Path toPath = CUSTOM_CLIENT_PATH.resolve(dropFile.getFileName().toString().replace(" ","_"));
        try {
            if(!Files.exists(toPath))
                Files.copy(dropFile, toPath);

            NativeImage nativeImage = NativeImage.read(new FileInputStream(toPath.toString()));
            DynamicTexture dynamicTexture = new DynamicTexture(nativeImage);

            return Minecraft.getInstance().getTextureManager().register("customclient", dynamicTexture);
        }
        catch (IOException e){
            JOptionPane.showMessageDialog(null, "PNG 파일만 인식합니다!");
        }
        return null;
    }

    protected static void renderTexture(GuiGraphics pGuiGraphics, ResourceLocation pShaderLocation, int x, int y, int width, int height, float pAlpha) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, pAlpha);

        pGuiGraphics.blit(pShaderLocation, 0, 0, -90, 0.0F, 0.0F, pGuiGraphics.guiWidth(), pGuiGraphics.guiHeight(), pGuiGraphics.guiWidth(), pGuiGraphics.guiHeight());
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
