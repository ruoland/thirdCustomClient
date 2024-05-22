package com.example;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import customclient.CustomClient;
import customclient.DrawTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ScreenNewTitle  extends TitleScreen {
    protected ResourceLocation BACKGROUND_IMAGE = new ResourceLocation(CustomClient.MODID, "textures/screenshot.png");

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        //for(DrawTexture drawTexture : ){
          //  renderTexture(pGuiGraphics, drawTexture.getTexture(), drawTexture.getX(), drawTexture.getY(), drawTexture.getWidth(), drawTexture.getHeight(), drawTexture.getAlpha());
        //}
    }

    @Override
    public void onFilesDrop(List<Path> pPacks) {
        super.onFilesDrop(pPacks);
        String fileName = pPacks.get(0).toFile().getName();
        ResourceLocation resourceLocation = getTexture(pPacks.get(0));
        int select = JOptionPane.showOptionDialog(null, "어떤 걸로 설정할까요?" , "이미지 불러오기", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"배경화면", "이미지", "취소"}, "취소");

        switch (select){
            case JOptionPane.YES_OPTION -> {
                BACKGROUND_IMAGE = resourceLocation;
            }
            case JOptionPane.NO_OPTION -> {
                //getData().getDrawTextureList().add(new DrawTexture(fileName, resourceLocation));
            }
        }
    }

    public ResourceLocation getTexture(Path dropFile){
        Path toPath = Paths.get("D:\\Projects\\thirdCustomClient\\src\\main\\resources\\assets\\customclient/"+dropFile.getFileName().toString().replace(" ","_"));
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

}
