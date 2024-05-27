package com.example.event;

import com.example.ScreenAPI;
import com.example.swing.SwingButton;
import com.example.wrapper.CustomWidgetWrapper;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.lwjgl.glfw.GLFW;

public class KeyEvent {

    @SubscribeEvent
    public void sizeEditEvent(ScreenEvent.KeyPressed.Pre event){
        if(ScreenAPI.isEditMode() && event.getKeyCode() != GLFW.GLFW_KEY_LEFT_ALT){
            Minecraft mc = Minecraft.getInstance();
            Window window = mc.getWindow();
            long windowLong = window.getWindow();
            System.out.println("키 입력 ");
            if(InputConstants.isKeyDown(windowLong, InputConstants.KEY_LEFT)) {
                ScreenAPI.addSelectWidth(-1);
            }
            if(InputConstants.isKeyDown(windowLong, InputConstants.KEY_RIGHT)) {
                ScreenAPI.addSelectWidth(1);
            }
            if(InputConstants.isKeyDown(windowLong, InputConstants.KEY_UP)) {
                ScreenAPI.addSelectHeight(-1);
            }
            if(InputConstants.isKeyDown(windowLong, InputConstants.KEY_DOWN)) {
                ScreenAPI.addSelectHeight(1);
            }
            if(InputConstants.isKeyDown(windowLong, InputConstants.KEY_LCONTROL) && InputConstants.isKeyDown(windowLong, InputConstants.KEY_B)) {
                CustomWidgetWrapper.WidgetButtonWrapper button = new CustomWidgetWrapper.WidgetButtonWrapper(new Button.Builder(Component.literal("새로운 버튼"), pButton -> {}).size(100, 20).pos(0,0).build());
                ScreenAPI.addButton(button);
            }

            ScreenAPI.update();
            if(ScreenAPI.getSelectSwing() != null)
                ScreenAPI.swingUpdate();
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public void editModeEvent(ScreenEvent.KeyPressed.Post event){
        if(isCustomGui(event.getScreen()) && event.getKeyCode() == GLFW.GLFW_KEY_LEFT_ALT) {
            ScreenAPI.changeEditMode(event.getScreen());
        }
    }

    public boolean isCustomGui(Screen screen){
        return screen.getClass().getSimpleName().equals("ScreenNewTitle") || screen.getClass().getSimpleName().equals("ScreenUserCustom");
    }
}
