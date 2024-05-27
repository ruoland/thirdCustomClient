package com.example;

import com.example.gui.event.ImageWidgetEvent;
import com.example.swing.SwingButton;
import com.example.swing.SwingImage;
import com.example.wrapper.CustomWidgetWrapper;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

public class ScreenAPI {
    private static CustomScreenData customScreenData;
    private static boolean editMode = false;


    public static void addSelectWidth(int i){
        customScreenData.setWidth(lastSelectWidget.getWidth() + i);
    }

    public static void addSelectHeight(int i){
        lastSelectWidget.setHeight(lastSelectWidget.getHeight() + i);
    }

    public static CustomScreenData getCustomScreenData() {
        return customScreenData;
    }

    public static void addTextfield(CustomWidgetWrapper.WidgetButtonWrapper text){
        customScreenData.addTextfield(text);
    }

    public static void addButton(CustomWidgetWrapper.WidgetButtonWrapper button){
        customScreenData.addButton(button);

    }
    public static void update(){
        customScreenData.update();
    }

    public static void dragWidget(double mouseX, double mouseY, double dragX, double dragY){

    }
    public static boolean mousePressed(int mouseButton, double mouseX, double mouseY){
        if(editMode){
            if(mouseButton == 0) {
                for (CustomWidgetWrapper.WidgetImageWrapper widgetImage : customScreenData.getWidgetImageList()) {
                    if (widgetImage.isMouseOver(mouseX, mouseY)) {
                        customScreenData.getWidgetHandler().selectWidget(widgetImage);
                        return true;
                    }
                }
                for (CustomWidgetWrapper.WidgetButtonWrapper widgetData : customScreenData.getWidgetList()) {
                    if (widgetData.isMouseOver(mouseX, mouseY)) {
                        if(selectSwing instanceof SwingButton){
                           customScreenData.getWidgetHandler().selectWidget(widgetData);
                           return true;
                        }
                        if(widgetData.abstractWidget instanceof Button){
                            selectSwing = new SwingButton(widgetData);
                        }
                        setSelectWidget(widgetData.getAbstractWidget());

                        return true;
                    }
                }

            }

        }
        return false;
    }


    public static void changeEditMode(Screen screen){
        editMode = !editMode;
        if(!editMode){
            customScreenData.update();
            customScreenData.save();
            if(selectSwing != null)
                selectSwing.dispose();
        }
        else {
            guiData = new GuiData((Screen) screen, screen.getClass().getSimpleName());
            guiData.syncWithDefaultWidget();
            if(screen instanceof ICustomBackground background)
                background.setBackground(new ResourceLocation(guiData.getBackground()));
        }
    }
    public static boolean isEditMode() {
        return editMode;
    }


    public static void renderImage(GuiGraphics guiGraphics)
    {
        if(editMode)
            guiData.renderImage(guiGraphics);
    }
    public static void setGui(Screen screen, String name){
        guiData = new GuiData(screen, name);
        guiData.syncWithDefaultWidget();
    }
    public static void fileDrops(Screen screen, Path pPacks){
        ResourceLocation resourceLocation = ScreenAPI.getDynamicTexture(pPacks);
        if(resourceLocation == null) {
            return;
        }
        int select = JOptionPane.showOptionDialog(null, "어떤 걸로 설정할까요?", "이미지 불러오기", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"배경화면", "이미지", "취소"}, "취소");
        switch (select) {
            case JOptionPane.YES_OPTION -> {
                guiData.dynamicBackground = resourceLocation.toString();
                guiData.background = "customclient:"+ pPacks.getFileName().toString();
            }
            case JOptionPane.NO_OPTION -> {
            }
        }
        if(select == JOptionPane.YES_OPTION)
            NeoForge.EVENT_BUS.post(new ImageWidgetEvent.Background(screen, resourceLocation, pPacks));
        else {
            GuiData.WidgetImage image =new GuiData.WidgetImage(resourceLocation, pPacks.getFileName().toString(), 0, 0, screen.width, screen.height, 1);
            if(selectSwing != null)
                selectSwing.dispose();
            selectSwing = new SwingImage(image);
            guiData.addImage(image);
        }
    }
    public static ResourceLocation getDynamicTexture(Path dropFile){

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
}
