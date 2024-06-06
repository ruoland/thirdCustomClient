package com.example.event;

import com.example.screen.CustomScreenMod;
import com.example.screen.ScreenFlow;
import com.example.screen.SelectHandler;
import com.mojang.blaze3d.platform.InputConstants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.lwjgl.glfw.GLFW;

public class KeyEvent {

    @SubscribeEvent
    public void sizeEditEvent(ScreenEvent.KeyPressed.Pre event){
        if(CustomScreenMod.isEditMode() && event.getKeyCode() != GLFW.GLFW_KEY_LEFT_ALT) {
            SelectHandler selectHandler = CustomScreenMod.getScreen(event.getScreen()).selectWidget();
            if(selectHandler != null) {
                switch (event.getKeyCode()) {
                    case GLFW.GLFW_KEY_UP -> selectHandler.addHeight(-1);
                    case GLFW.GLFW_KEY_DOWN -> selectHandler.addHeight(1);
                    case GLFW.GLFW_KEY_RIGHT -> selectHandler.addWidth(1);
                    case GLFW.GLFW_KEY_LEFT -> selectHandler.addWidth(-1);
                    case GLFW.GLFW_KEY_DELETE -> selectHandler.visible(false);
                }
            }

            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public void editModeEvent(ScreenEvent.KeyPressed.Post event){
        if(CustomScreenMod.hasScreen(event.getScreen()) && event.getKeyCode() == GLFW.GLFW_KEY_LEFT_ALT) {
            CustomScreenMod.changeEditMode(event.getScreen());
        }
    }
    @SubscribeEvent
    public void createButton(ScreenEvent.KeyPressed.Post event){
        if(CustomScreenMod.isEditMode()) {
            if (ScreenFlow.isKeyDown(InputConstants.KEY_LCONTROL) && ScreenFlow.isKeyDown(InputConstants.KEY_B)) {
                System.out.println("새 버튼 추가");
                CustomScreenMod.getScreen(event.getScreen()).addButton("새로운 버튼", 200, 20, 0,0);
            }
        }
    }



}
