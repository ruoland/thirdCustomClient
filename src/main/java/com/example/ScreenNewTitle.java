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
    private String backgroundFileName;
    ScreenNewTitle(){
        System.setProperty("java.awt.headless", "false");
    }
    @Override
    public void onFilesDrop(List<Path> pPacks) {
        super.onFilesDrop(pPacks);
        if(!NeoForge.EVENT_BUS.post(new FilesDropEvent(this, pPacks)).isCanceled()) {
            ResourceLocation resourceLocation = getTexture(pPacks.get(0));
            System.out.println("파일 드랍됨"+pPacks.get(0));
            if(resourceLocation == null) {
                return;
            }
            System.out.println("파일 선택 창 띄우기 전");
            int select = JOptionPane.showOptionDialog(null, "어떤 걸로 설정할까요?", "이미지 불러오기", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"배경화면", "이미지", "취소"}, "취소");
            System.out.println("파일 선택 창");
            switch (select) {
                case JOptionPane.YES_OPTION -> {
                    BACKGROUND_IMAGE = resourceLocation;
                    backgroundFileName = pPacks.get(0).getFileName().toString();
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

        try {
            NativeImage nativeImage = NativeImage.read(new FileInputStream(dropFile.toString()));
            DynamicTexture dynamicTexture = new DynamicTexture(nativeImage);

            return Minecraft.getInstance().getTextureManager().register("customclient", dynamicTexture);
        }
        catch (IOException e){
            JOptionPane.showMessageDialog(null, "인식할 수 없는 파일입니다! png 파일만 인식하며 윈도우 외의 환경에서는 정상작동 하지 않을 수 있습니다."+e.getCause().toString());

        }
        return null;
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

    @Override
    public String getBackgroundFileName() {
        return backgroundFileName;
    }
}
