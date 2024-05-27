package com.example.event;

import com.example.CustomScreenMod;
import com.example.ScreenAPI;
import com.example.gui.event.FilesDropEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class ScreenMouseEvent {

    @SubscribeEvent
    public void mouseClick(ScreenEvent.MouseButtonReleased.Post event){
        CustomScreenMod.getRecntlyScreen().setSelectWidget(null);
    }

    @SubscribeEvent
    public void fileDropEvent(FilesDropEvent event){
        if(!CustomScreenMod.isEditMode())
            event.setCanceled(true);
        ScreenAPI.fileDrops(event.getScreen(),event.getFile());
    }


    @SubscribeEvent
    public void screenButton(ScreenEvent.MouseDragged.Post opening){
        if(CustomScreenMod.isEditMode()){

                int mouseX = (int) (opening.getMouseX() + opening.getDragX());
                int mouseY = (int) (opening.getMouseY() + opening.getDragY());
                CustomScreenMod.getRecntlyScreen().getScreenWidgets();

        }
        CustomScreenMod.getRecntlyScreen().getSelectWidget().dragWidget(opening.getMouseX(), opening.getMouseY(), opening.getDragX(), opening.getDragX());
    }

    @SubscribeEvent
    public void screenButton(ScreenEvent.MouseButtonPressed.Pre opening){
        if(CustomScreenMod.isEditMode())
            opening.setCanceled(true);
        CustomScreenMod.getRecntlyScreen().clickWidget(opening.getMouseX(), opening.getMouseY());
    }

}
