package com.example.event;

import com.example.*;
import com.example.screen.CustomScreenMod;
import com.example.screen.ScreenFlow;
import com.example.wrapper.widget.ButtonWrapper;
import com.example.wrapper.widget.ImageWrapper;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class TitleInitEvent {
    /**
     * 이 클래스에서 커스터마이징할 스크린을 지정함
     * 현재는 Title Screen 클래스만 지정
     */
    int openCount = 0;
    @SubscribeEvent
    public void screenOpenEvent(ScreenEvent.Opening event) {
        String screenName = CustomScreenMod.getScreenName(event.getNewScreen());
        if (screenName != null && screenName.equals("TitleScreen")) {
            setFinalStatic("MINECRAFT_LOGO", new ResourceLocation("customclient:textures/logo.png"));
            setFinalStatic("MINECRAFT_EDITION", new ResourceLocation("customclient:textures/logo.png"));
            ScreenNewTitle newTitle = new ScreenNewTitle();
            event.setNewScreen(newTitle);
            ScreenFlow screenFlow =  CustomScreenMod.getScreen("ScreenNewTitle");
            screenFlow.openScreen(event.getNewScreen()); //스크린 열림
        }
        else if(screenName != null && !screenName.equals("ScreenNewTitle")){
            isLoad = -1;
            ScreenFlow screenFlow =  CustomScreenMod.getScreen("ScreenNewTitle");
            screenFlow.reset();
        }
    }
    private static int isLoad = -1;

    @SubscribeEvent
    public void screenPostInitEvent(ScreenEvent.Init.Post event){
        if(isLoad == 1) {
            System.out.println("이미 로딩됨");
            return;
        }

        if(isTitle(event.getScreen())) {
            ScreenFlow screenFlow =  CustomScreenMod.getScreen("ScreenNewTitle");

            isLoad = 1;
            screenFlow.loadScreenData();

            //타이틀에서는 로고가 있으니까 여기서 로고 정보 저장하고 불러옴
            if(screenFlow.getCustomData().has("logoVisible")){
                CustomScreenMod.setLogoVisible( screenFlow.getCustomData().getAsJsonPrimitive("logoVisible").getAsBoolean());
            }
            else{
                screenFlow.getCustomData().add("logoVisible", new JsonPrimitive(CustomScreenMod.isLogoVisible()));
            }

            if(CustomScreenMod.isLogoVisible())
                CustomScreenMod.logoRender(screenFlow);
            screenFlow.save();
            screenFlow.getWidget().update();
            CustomScreenMod.loadTitleWidgets();
        }


    }


    @SubscribeEvent
    public void screenSwingEvent(ScreenEvent.Render.Post event){
        if(CustomScreenMod.isEditMode()){
        }
    }

    void setFinalStatic(String field, Object newValue) {
        try {
            final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            final Unsafe unsafe = (Unsafe) unsafeField.get(null);

            final Field ourField = LogoRenderer.class.getDeclaredField(field);
            final Object staticFieldBase = unsafe.staticFieldBase(ourField);
            final long staticFieldOffset = unsafe.staticFieldOffset(ourField);
            unsafe.putObject(staticFieldBase, staticFieldOffset, newValue);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public boolean isTitle(Screen screen){
        return screen.getClass().getSimpleName().equals("ScreenNewTitle");
    }
}
