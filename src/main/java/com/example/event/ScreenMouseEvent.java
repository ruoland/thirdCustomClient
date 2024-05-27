package com.example.event;

import com.example.ScreenAPI;
import com.example.wrapper.WidgetHandler;
import com.example.gui.event.FilesDropEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class ScreenMouseEvent {

    @SubscribeEvent
    public void mouseClick(ScreenEvent.MouseButtonReleased.Post event){
        ScreenAPI.setSelectWidget(null);
    }

    @SubscribeEvent
    public void fileDropEvent(FilesDropEvent event){
        if(!ScreenAPI.isEditMode())
            event.setCanceled(true);
        ScreenAPI.fileDrops(event.getScreen(),event.getFile());
    }


    @SubscribeEvent
    public void screenButton(ScreenEvent.MouseDragged.Post opening){
        if(ScreenAPI.isEditMode()){
            if(ScreenAPI.getCustomScreenData() != null){
                int mouseX = (int) (opening.getMouseX() + opening.getDragX());
                int mouseY = (int) (opening.getMouseY() + opening.getDragY());
                WidgetHandler widgetHandler = ScreenAPI.getCustomScreenData().getWidgetHandler();;

                widgetHandler.setPosition(mouseX, mouseY);

                guiData.syncWithDefault();
            }
        }
        ScreenAPI.dragWidget(opening.getMouseX(), opening.getMouseY(), opening.getDragX(), opening.getDragX());
    }

    @SubscribeEvent
    public void screenButton(ScreenEvent.MouseButtonPressed.Pre opening){
        if(ScreenAPI.isEditMode())
            opening.setCanceled(true);
        ScreenAPI.mousePressed(opening.getButton(), opening.getMouseX(), opening.getMouseY());
    }

}
