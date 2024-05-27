package com.example.event;

import com.example.ScreenAPI;
import com.example.ScreenNewTitle;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class TitleOpenEvent {

    @SubscribeEvent
    public void screenOpenEvent(ScreenEvent.Opening event) {
        if (event.getNewScreen().getClass().getSimpleName().equals("TitleScreen")) {
            event.setNewScreen(new ScreenNewTitle());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void screenPostInitEvent(ScreenEvent.Init.Post event){
        if(isTitle(event.getScreen())) {
            ScreenAPI.setGui(event.getScreen(), "ScreenNewTitle");
        }
    }

    public boolean isTitle(Screen screen){
        return screen.getClass().getSimpleName().equals("ScreenNewTitle");
    }
}
