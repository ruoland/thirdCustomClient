package com.example.event;

import com.example.CustomScreenMod;
import com.example.wrapper.SelectWidgetHandler;
import com.example.wrapper.WidgetButtonWrapper;
import com.example.wrapper.WidgetHandler;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.lwjgl.glfw.GLFW;

public class KeyEvent {

    @SubscribeEvent
    public void sizeEditEvent(ScreenEvent.KeyPressed.Pre event){
        if(CustomScreenMod.isEditMode() && event.getKeyCode() != GLFW.GLFW_KEY_LEFT_ALT){
            Minecraft mc = Minecraft.getInstance();
            SelectWidgetHandler selectWidgetHandler = CustomScreenMod.getRecntlyScreen().getSelectWidget();
            long windowLong = mc.getWindow().getWindow();

            if(InputConstants.isKeyDown(windowLong, InputConstants.KEY_LEFT)) {
                selectWidgetHandler.addSelectWidth(-1);
            }
            if(InputConstants.isKeyDown(windowLong, InputConstants.KEY_RIGHT)) {
                selectWidgetHandler.addSelectWidth(1);
            }
            if(InputConstants.isKeyDown(windowLong, InputConstants.KEY_UP)) {
                selectWidgetHandler.addSelectHeight(-1);
            }
            if(InputConstants.isKeyDown(windowLong, InputConstants.KEY_DOWN)) {
                selectWidgetHandler.addSelectHeight(1);
            }
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public void editModeEvent(ScreenEvent.KeyPressed.Post event){
        if(isCustomGui(event.getScreen()) && event.getKeyCode() == GLFW.GLFW_KEY_LEFT_ALT) {
            CustomScreenMod.changeEditMode(event.getScreen());
        }
    }
    @SubscribeEvent
    public void createButton(ScreenEvent.KeyPressed.Post event){
        Minecraft mc = Minecraft.getInstance();
        long windowLong =mc.getWindow().getWindow();
        WidgetHandler widgetHandler = CustomScreenMod.getRecntlyScreen().getScreenWidgets();
        if(InputConstants.isKeyDown(windowLong, InputConstants.KEY_LCONTROL) && InputConstants.isKeyDown(windowLong, InputConstants.KEY_B)) {
            WidgetButtonWrapper button = new WidgetButtonWrapper(new Button.Builder(Component.literal("새로운 버튼"), pButton -> {}).size(100, 20).pos(0,0).build());
            widgetHandler.addButton(button);
        }
    }



    public boolean isCustomGui(Screen screen){
        return screen.getClass().getSimpleName().equals("ScreenNewTitle") || screen.getClass().getSimpleName().equals("ScreenUserCustom");
    }
}
