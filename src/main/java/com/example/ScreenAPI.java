package com.example;

import com.example.gui.event.ImageWidgetEvent;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

public class ScreenAPI {
    private static GuiData guiData;
    private static boolean editMode = false;
    private static AbstractWidget selectWidget, lastSelectWidget;
    private static SwingCustom selectSwing;


    public static void setSelectSwing(SwingCustom selectSwing) {
        ScreenAPI.selectSwing = selectSwing;
    }

    public static SwingCustom getSelectSwing() {
        return selectSwing;
    }

    public static void swingUpdate(){
        System.out.println(ScreenAPI.getSelectSwing() + " 안에서");
        selectSwing.update();
    }
    public static void addSelectWidth(int i){
        lastSelectWidget.setWidth(lastSelectWidget.getWidth() + i);
    }

    public static void addSelectHeight(int i){
        lastSelectWidget.setHeight(lastSelectWidget.getHeight() + i);
    }

    public static void addTextfield(GuiData.WidgetData text){

        guiData.addTextfield(text);
    }

    public static void addButton(GuiData.WidgetData button){
        guiData.addButton(button);
    }
    public static void update(){
        guiData.syncWithDefault();
    }
    public static void setSelectWidget(@Nullable AbstractWidget selectWidget) {
        if(isEditMode()) {
            ScreenAPI.selectWidget = selectWidget;
            if(selectWidget != null)
                ScreenAPI.lastSelectWidget = selectWidget;

        }
    }

    public static void dragWidget(double mouseX, double mouseY, double dragX, double dragY){
        if(editMode){
            if(selectWidget != null){
                selectWidget.setX((int) (mouseX +dragX));
                selectWidget.setY((int) (mouseY +dragY));
                System.out.println("드래그 중?");
                guiData.syncWithDefault();

                ScreenAPI.swingUpdate();
            }
        }
    }
    public static boolean mousePressed(int mouseButton, double mouseX, double mouseY){
        if(editMode){
            if(mouseButton == 0) {
                for (GuiData.WidgetImage widgetImage : guiData.getWidgetImageList()) {
                    if (widgetImage.isMouseOver(mouseX, mouseY)) {
                        setSelectWidget(widgetImage.getAbstractWidget());
                        System.out.println("위젯 이미지 선택됨");
                        if(selectSwing instanceof SwingImage image) {
                            if(image.widgetImage != widgetImage)
                                image.dispose();
                            else
                                return true;
                        }

                        selectSwing = new SwingImage(widgetImage);
                        return true;
                    }
                }
                for (GuiData.WidgetData widgetData : guiData.getWidgetArrayList()) {
                    System.out.println("버튼 확인 " +widgetData.message + " - "+widgetData.isMouseOver(mouseX, mouseY));
                    if (widgetData.isMouseOver(mouseX, mouseY)) {
                        if(selectSwing instanceof SwingButton){
                            if(selectSwing.guiButton.abstractWidget == widgetData.abstractWidget) {
                                setSelectWidget(widgetData.getAbstractWidget());
                                System.out.println("선택 위젯이 같음");
                                return true;
                            }
                            else
                                selectSwing.dispose();
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
            guiData.syncWithDefault();
            guiData.save();
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
