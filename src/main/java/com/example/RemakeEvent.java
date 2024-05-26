package com.example;

import com.example.gui.event.FilesDropEvent;
import com.example.gui.event.ImageWidgetEvent;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.lwjgl.glfw.GLFW;

public class RemakeEvent {
    private String[] whiteList = {"ScreenNewTitle"};
    private boolean editMode;
    private AbstractWidget selectWidget, lastSelectWidget;
    private GuiData guiData;
    public boolean check(Screen screen){
        for(String black : whiteList) {
            if (screen.getClass().getSimpleName().equals(black))
                return true;
        }
        return false;
    }

    @SubscribeEvent
    public void screenOpenEvent(ScreenEvent.Opening event){
        if(event.getNewScreen().getClass().getSimpleName().equals("TitleScreen")){
            event.setNewScreen(new ScreenNewTitle());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void screenPostInitEvent(ScreenEvent.Init.Post event){
        if(check(event.getScreen())) {
            guiData = new GuiData(event.getScreen(), event.getScreen().getClass().getSimpleName());
            guiData.updateWidget();
            System.out.println("데이터 업데이트 됨");
        }
    }

    @SubscribeEvent
    public void mouseClick(ScreenEvent.MouseButtonReleased.Post event){
        if(editMode)
            if(selectWidget != null)
                selectWidget = null;
    }


    @SubscribeEvent
    public void screenRender(ScreenEvent.Render.Post render){
        Minecraft mc = Minecraft.getInstance();
        GuiGraphics pGuiGraphics = render.getGuiGraphics();
        if(editMode) {
            pGuiGraphics.drawString(mc.font, Component.literal("편집 모드 실행 중"), 0, 0, 0xFFFFFF, false);
        }
        if(guiData != null) {
            guiData.renderImage(pGuiGraphics);
        }
    }

    @SubscribeEvent
    public void sizeEditEvent(ScreenEvent.KeyPressed.Pre event){
        if(editMode && event.getKeyCode() != GLFW.GLFW_KEY_LEFT_ALT){
            Minecraft mc = Minecraft.getInstance();
            int num = InputConstants.isKeyDown(mc.getWindow().getWindow(), InputConstants.KEY_LSHIFT) ? 5 : 1;

            if(InputConstants.isKeyDown(mc.getWindow().getWindow(), InputConstants.KEY_LEFT)) {
                lastSelectWidget.setWidth(lastSelectWidget.getWidth() - 1);
            }
            if(InputConstants.isKeyDown(mc.getWindow().getWindow(), InputConstants.KEY_RIGHT)) {
                lastSelectWidget.setWidth(lastSelectWidget.getWidth() + 1);
            }
            if(InputConstants.isKeyDown(mc.getWindow().getWindow(), InputConstants.KEY_UP)) {
                lastSelectWidget.setHeight(lastSelectWidget.getHeight() - 1);
            }
            if(InputConstants.isKeyDown(mc.getWindow().getWindow(), InputConstants.KEY_DOWN)) {
                lastSelectWidget.setHeight(lastSelectWidget.getHeight() + 1);
            }
            guiData.updateData();
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void fileDropEvent(FilesDropEvent event){
        if(!editMode)
            event.setCanceled(true);
    }
    @SubscribeEvent
    public void imageBackground(ImageWidgetEvent.Background event){
        if(!editMode)
            event.setCanceled(true);
        else{
            System.out.println("배경화면 설정: "+event.getFileName());
            ICustomBackground customBackground = (ICustomBackground) event.getScreen();

        }

    }
    @SubscribeEvent
    public void imageWidgetAdd(ImageWidgetEvent.Image event){
        if(!editMode)
            event.setCanceled(true);
        else{
            System.out.println(event.getFileName());
            guiData.addImage(new GuiData.WidgetImage(event.getLocation(), event.getFileName(), 0,0, event.getScreen().width, event.getScreen().height, 1));
        }

    }
    @SubscribeEvent
    public void editModeEvent(ScreenEvent.KeyPressed.Post event){
        if(check(event.getScreen()) && event.getKeyCode() == GLFW.GLFW_KEY_LEFT_ALT) {
            changeEditMode();
            ICustomBackground screen = (ICustomBackground) event.getScreen();
            if(!editMode){
                guiData.updateData();
                guiData.background = screen.getBackgroundFileName().toString();
                guiData.save();
            }
            else {
                guiData = new GuiData(event.getScreen(), event.getScreen().getClass().getSimpleName());
                guiData.updateWidget();
                screen.setBackground(new ResourceLocation(guiData.background));
            }
        }
    }

    @SubscribeEvent
    public void screenButton(ScreenEvent.Render.Post event){
        event.getGuiGraphics().pose().pushPose();
        event.getGuiGraphics().pose().translate(0,0,-1);

        //ScreenNewTitle.renderTexture(event.getGuiGraphics(), BACKGROUND_IMAGE, 0,0, event.getScreen().width, event.getScreen().height, 1);
        event.getGuiGraphics().pose().popPose();

    }

    @SubscribeEvent
    public void screenButton(ScreenEvent.MouseDragged.Post opening){
        if(editMode){
            if(selectWidget != null){
                selectWidget.setX((int) (opening.getMouseX() + opening.getDragX()));
                selectWidget.setY((int) (opening.getMouseY() + opening.getDragY()));
                guiData.updateData();
            }
        }
    }
    private SwingButton selectSwingButton;
    @SubscribeEvent
    public void screenButton(ScreenEvent.MouseButtonPressed.Pre opening){
        if(editMode){
            if(opening.getButton() == 0) {
                for (GuiData.WidgetImage widgetImage : guiData.getWidgetImageList()) {
                    if (widgetImage.isMouseOver(opening.getMouseX(), opening.getMouseY())) {
                        setSelectWidget(widgetImage.getAbstractWidget());
                        System.out.println("위젯 이미지 선택됨");
                        opening.setCanceled(true);
                        return;
                    }
                }
                for (GuiData.WidgetData widgetData : guiData.getWidgetArrayList()) {
                    if (widgetData.isMouseOver(opening.getMouseX(), opening.getMouseY())) {
                        if(selectSwingButton != null){
                            if(selectSwingButton.guiButton.abstractWidget == widgetData.abstractWidget) {
                                opening.setCanceled(true);
                                setSelectWidget(widgetData.getAbstractWidget());
                                return;
                            }
                            else
                                selectSwingButton.dispose();
                        }
                        if(widgetData.abstractWidget instanceof Button){
                            selectSwingButton = new SwingButton(widgetData);
                        }
                        setSelectWidget(widgetData.getAbstractWidget());
                        opening.setCanceled(true);
                        return;
                    }
                }

            }
        }
    }

    public void setSelectWidget(AbstractWidget abstractWidget){
        selectWidget = abstractWidget;
        lastSelectWidget = abstractWidget;
    }
    public void changeEditMode(){
        editMode = !editMode;
        if(selectSwingButton != null){
            selectSwingButton.dispose();
            selectSwingButton = null;
        }
    }

}
