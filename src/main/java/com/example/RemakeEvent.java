package com.example;

import com.example.gui.event.FilesDropEvent;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.lwjgl.glfw.GLFW;

public class RemakeEvent {


    public boolean canEdit(Screen screen){
        return screen.getClass().getSimpleName().equals("ScreenNewTitle") || screen.getClass().getSimpleName().equals("ScreenUserCustom");
    }

    @SubscribeEvent
    public void screenOpenEvent(ScreenEvent.Opening event){
        if(event.getNewScreen().getClass().getSimpleName().equals("TitleScreen")){
            event.setNewScreen(new ScreenNewTitle());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void screenPostInitEvent(ScreenEvent.Init.Post event){
        if(canEdit(event.getScreen())) {
            ScreenAPI.setGui(event.getScreen(), "ScreenNewTitle");

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
                GuiData.WidgetData button = new GuiData.WidgetData(new Button.Builder(Component.literal("새로운 버튼"), pButton -> {}).size(100, 20).pos(0,0).build());
                ScreenAPI.addButton(button);
                ScreenAPI.setSelectWidget(button.getAbstractWidget());
                ScreenAPI.setSelectSwing(new SwingButton(button));


            }

            ScreenAPI.update();
            if(ScreenAPI.getSelectSwing() != null)
                ScreenAPI.swingUpdate();
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
        if(canEdit(event.getScreen()) && event.getKeyCode() == GLFW.GLFW_KEY_LEFT_ALT) {
            ScreenAPI.changeEditMode(event.getScreen());
        }
    }


    @SubscribeEvent
    public void screenButton(ScreenEvent.MouseDragged.Post opening){
        System.out.println(opening.getDragX());
        ScreenAPI.dragWidget(opening.getMouseX(), opening.getMouseY(), opening.getDragX(), opening.getDragX());
    }

    @SubscribeEvent
    public void screenButton(ScreenEvent.MouseButtonPressed.Pre opening){

        opening.setCanceled(ScreenAPI.mousePressed(opening.getButton(), opening.getMouseX(), opening.getMouseY()));
    }

}
