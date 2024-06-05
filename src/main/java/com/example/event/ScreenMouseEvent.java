package com.example.event;

import com.example.CustomScreenMod;
import com.example.ScreenAPI;
import com.example.ScreenFlow;
import com.example.gui.event.FilesDropEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class ScreenMouseEvent {

    /*
     * 파일을 드롭했을 때 이벤트입니다.
     */
    @SubscribeEvent
    public void fileDropEvent(FilesDropEvent event){
        if(CustomScreenMod.hasScreen(event.getScreen()) && CustomScreenMod.isEditMode()) {
            ScreenFlow screenFlow = CustomScreenMod.getScreen(event.getScreen().getTitle().toString());
            screenFlow.fileDropEvent(event.getFile());
        }
    }

    /*
    클릭한 위젯의 위치를 드래그합니다
     */
    @SubscribeEvent
    public void screenButton(ScreenEvent.MouseDragged.Post opening){
        if(CustomScreenMod.hasScreen(opening.getScreen()) && CustomScreenMod.isEditMode()){
            int mouseX = (int) (opening.getMouseX() + opening.getDragX());
            int mouseY = (int) (opening.getMouseY() + opening.getDragY());

            ScreenFlow screenFlow = CustomScreenMod.getScreen(opening.getScreen());
            System.out.println("드래그 중?" + screenFlow.getSelectWidget());
            if(screenFlow.hasSelectWidget())
                screenFlow.dragWidget(mouseX, mouseY);
        }

    }

    //마우스 클릭시 클릭한 위젯을 수정할 위젯으로 설정합니다
    @SubscribeEvent
    public void screenButton(ScreenEvent.MouseButtonPressed.Pre event){
        if(CustomScreenMod.hasScreen(event.getScreen())) {
            if (CustomScreenMod.isEditMode()) {
                event.setCanceled(true);
                CustomScreenMod.getScreen(event.getScreen()).clickWidget(event.getMouseX(), event.getMouseY());
                System.out.println("선택됨" +CustomScreenMod.getCurrentScreen());
            }
        }
    }

    /*
    마우스 땠을 때 선택한 위젯 제거합니다.
     */
    @SubscribeEvent
    public void mouseClick(ScreenEvent.MouseButtonReleased.Post event){
        if(CustomScreenMod.hasScreen(event.getScreen())) {
            ScreenFlow screenFlow = CustomScreenMod.getScreen(event.getScreen());
            if(screenFlow.hasSelectWidget())
                 screenFlow.reset();
        }
    }

}
