package com.example;

import com.example.gui.event.FilesDropEvent;
import com.example.gui.event.ImageWidgetEvent;
import customclient.CustomClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
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
    private AbstractWidget selectWidget;
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
        }
    }

    @SubscribeEvent
    public void mouseClick(ScreenEvent.MouseButtonReleased.Post event){
        if(editMode)
            if(selectWidget != null)
                selectWidget = null;
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

    @SubscribeEvent
    public void screenRender(ScreenEvent.Render.Post render){
        Minecraft mc = Minecraft.getInstance();
        GuiGraphics pGuiGraphics = render.getGuiGraphics();
        if(editMode) {
            pGuiGraphics.drawString(mc.font, Component.literal("편집 모드 실행 중"), 0, 0, 0xFFFFFF, false);
        }
        if(guiData != null)
            guiData.renderImage(pGuiGraphics);
    }

    @SubscribeEvent
    public void sizeEditEvent(ScreenEvent.KeyPressed.Post event){
        if(editMode && event.getKeyCode() != GLFW.GLFW_KEY_LEFT_ALT){
            switch (event.getKeyCode()){
                case GLFW.GLFW_KEY_LEFT: {
                    selectWidget.setWidth(selectWidget.getWidth() - 1);
                    break;
                }
                case GLFW.GLFW_KEY_RIGHT: {
                    selectWidget.setWidth(selectWidget.getWidth() + 1);
                    break;
                }
                case GLFW.GLFW_KEY_UP: {
                    selectWidget.setHeight(selectWidget.getHeight() + 1);
                    break;
                }
                case GLFW.GLFW_KEY_DOWN: {
                    selectWidget.setWidth(selectWidget.getHeight() - 1);
                    break;
                }
            }
            guiData.updateData();
        }
    }
    @SubscribeEvent
    public void fileDropEvent(FilesDropEvent event){
        if(!editMode)
            event.setCanceled(true);

    }
    @SubscribeEvent
    public void imageWidgetAdd(ImageWidgetEvent.Image event){
        if(!editMode)
            event.setCanceled(true);
        else{
            guiData.addImage(new GuiData.WidgetImage(event.get(), event.getFileName(), 0,0, event.getScreen().width, event.getScreen().height, 1));
        }

    }
    @SubscribeEvent
    public void editModeEvent(ScreenEvent.KeyPressed.Post event){
        if(check(event.getScreen()) && event.getKeyCode() == GLFW.GLFW_KEY_LEFT_ALT) {
            editMode = !editMode;
            ICustomBackground screen = (ICustomBackground) event.getScreen();
            if(!editMode){
                //에딧 모드 종료

                guiData.updateData();

                guiData.background = screen.getBackground().toString();
                guiData.save();
            }
            else {
                guiData = new GuiData(event.getScreen(), event.getScreen().getClass().getSimpleName());
                guiData.updateWidget();
                screen.setBackground(new ResourceLocation(guiData.background));
            }
        }
    }
    protected ResourceLocation BACKGROUND_IMAGE = new ResourceLocation(CustomClient.MODID, "textures/screenshot.png");

    @SubscribeEvent
    public void screenButton(ScreenEvent.Render.Post event){
        event.getGuiGraphics().pose().pushPose();
        event.getGuiGraphics().pose().translate(0,0,-1);

        //ScreenNewTitle.renderTexture(event.getGuiGraphics(), BACKGROUND_IMAGE, 0,0, event.getScreen().width, event.getScreen().height, 1);
        event.getGuiGraphics().pose().popPose();

    }

    @SubscribeEvent
    public void screenButton(ScreenEvent.MouseButtonPressed.Pre opening){
        if(editMode){
            if(opening.getButton() == 0) {
                for (GuiEventListener guiEventListener : opening.getScreen().children()) {
                    if (guiEventListener instanceof AbstractWidget && guiEventListener.isMouseOver(opening.getMouseX(), opening.getMouseY())) {
                        this.selectWidget = (AbstractWidget) guiEventListener;
                        opening.setCanceled(true);
                        return;
                    }
                }

            }
        }
    }

    public void reset(){
        editMode = false;
    }
}
