package com.example.event;

import com.example.*;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class TitleOpenEvent {

    @SubscribeEvent
    public void screenOpenEvent(ScreenEvent.Opening event) {
        if (event.getNewScreen().getClass().getSimpleName().equals("TitleScreen")) {
            ScreenNewTitle newTitle = new ScreenNewTitle();
            event.setNewScreen(newTitle);

            CustomScreen.getScreen("ScreenNewTitle").openScreen(event.getNewScreen()); //스크린 열림
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void screenPostInitEvent(ScreenEvent.Init.Post event){
        if(isTitle(event.getScreen())) {
            CustomScreen.getScreen("ScreenNewTitle").loadScreenData();
        }
    }

    public boolean isTitle(Screen screen){
        return screen.getClass().getSimpleName().equals("ScreenNewTitle");
    }
}
