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
            ScreenAPI.setGui(event.getScreen());
            System.out.println("데이터 업데이트 됨");
        }
    }

    @SubscribeEvent
    public void mouseClick(ScreenEvent.MouseButtonReleased.Post event){
        ScreenAPI.setSelectWidget(null);
    }


    @SubscribeEvent
    public void screenRender(ScreenEvent.Render.Post render){
        Minecraft mc = Minecraft.getInstance();
        GuiGraphics pGuiGraphics = render.getGuiGraphics();
        if(ScreenAPI.isEditMode()) {
            pGuiGraphics.drawString(mc.font, Component.literal("편집 모드 실행 중"), 0, 0, 0xFFFFFF, false);
        }
        ScreenAPI.renderImage(pGuiGraphics);
    }

    @SubscribeEvent
    public void sizeEditEvent(ScreenEvent.KeyPressed.Pre event){
        if(ScreenAPI.isEditMode() && event.getKeyCode() != GLFW.GLFW_KEY_LEFT_ALT){
            Minecraft mc = Minecraft.getInstance();

            if(InputConstants.isKeyDown(mc.getWindow().getWindow(), InputConstants.KEY_LEFT)) {
                ScreenAPI.addWidth(-1);
            }
            if(InputConstants.isKeyDown(mc.getWindow().getWindow(), InputConstants.KEY_RIGHT)) {
                ScreenAPI.addWidth(1);
            }
            if(InputConstants.isKeyDown(mc.getWindow().getWindow(), InputConstants.KEY_UP)) {
                ScreenAPI.addHeight(-1);
            }
            if(InputConstants.isKeyDown(mc.getWindow().getWindow(), InputConstants.KEY_DOWN)) {
                ScreenAPI.addHeight(1);
            }
            ScreenAPI.update();
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void fileDropEvent(FilesDropEvent event){
        if(!ScreenAPI.isEditMode())
            event.setCanceled(true);
        ScreenAPI.fileDrops(event.getScreen(),event.getFile());
    }

    @SubscribeEvent
    public void editModeEvent(ScreenEvent.KeyPressed.Post event){
        if(check(event.getScreen()) && event.getKeyCode() == GLFW.GLFW_KEY_LEFT_ALT) {
            ScreenAPI.changeEditMode(event.getScreen());
        }
    }


    @SubscribeEvent
    public void screenButton(ScreenEvent.MouseDragged.Post opening){
        ScreenAPI.dragWidget(opening.getMouseX(), opening.getMouseY(), opening.getDragX(), opening.getDragX());
    }

    @SubscribeEvent
    public void screenButton(ScreenEvent.MouseButtonPressed.Pre opening){
        opening.setCanceled(ScreenAPI.mousePressed(opening.getButton(), opening.getMouseX(), opening.getMouseY()));
    }

}
