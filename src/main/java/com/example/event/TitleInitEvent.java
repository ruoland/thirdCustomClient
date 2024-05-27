package com.example.event;

import com.example.*;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class TitleInitEvent {
    /**
     * 이 클래스에서 커스터마이징할 스크린을 지정함
     * 현재는 Title Screen 클래스만 지정
     */
    @SubscribeEvent
    public void screenOpenEvent(ScreenEvent.Opening event) {
        String screenName = CustomScreenMod.getScreenName(event.getNewScreen());
        if (screenName != null && screenName.equals("TitleScreen")) {
            ScreenNewTitle newTitle = new ScreenNewTitle();
            event.setNewScreen(newTitle);
            CustomScreenMod.getScreen("ScreenNewTitle").openScreen(event.getNewScreen()); //스크린 열림
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void screenPostInitEvent(ScreenEvent.Init.Post event){
        if(isTitle(event.getScreen())) {
            CustomScreenMod.getScreen("ScreenNewTitle").loadScreenData();
        }
    }

    public boolean isTitle(Screen screen){
        return screen.getClass().getSimpleName().equals("ScreenNewTitle");
    }
}
