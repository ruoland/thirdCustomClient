package com.example.event;

import com.example.*;
import com.example.swing.SwingNewTitle;
import com.example.wrapper.WidgetImageWrapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
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
            System.out.println("초기화됨");
            ScreenFlow screenFlow =  CustomScreenMod.getScreen("ScreenNewTitle");
            screenFlow.reset();
        }
    }
    private static int isLoad = -1;
    @SubscribeEvent
    public void screenPostInitEvent(ScreenEvent.Init.Post event){
        ScreenFlow screenFlow =  CustomScreenMod.getScreen("ScreenNewTitle");
        if(isLoad == 1) {
            System.out.println("이미 로딩됨");
            return;
        }

        if(isTitle(event.getScreen())) {
            isLoad = 1;

            screenFlow.loadScreenData();

            if( screenFlow.getCustomData().has("logoVisible")){
                CustomScreenMod.setLogoVisible( screenFlow.getCustomData().getAsJsonPrimitive("logoVisible").getAsBoolean());
            }
            else{
                screenFlow.getCustomData().add("logoVisible", new JsonPrimitive(CustomScreenMod.isLogoVisible()));
                System.out.println("로고 상태: " + CustomScreenMod.isLogoVisible());
            }

            if(!CustomScreenMod.isLogoVisible()){
                Screen newTitle = event.getScreen();
                int i = newTitle.width / 2 - 128;
                int j = newTitle.width / 2 - 64;
                int k = 30 + 44 - 7;
                if(screenFlow.hasImageWidget("customclient:textures/minecraft.png"))
                    screenFlow.getScreenWidgets().addImage(new WidgetImageWrapper(new ResourceLocation("customclient:textures/minecraft.png"), "textures/minecraft.png", i,30,LogoRenderer.LOGO_WIDTH,LogoRenderer.LOGO_HEIGHT,1));

                if (screenFlow.hasImageWidget("customclient:textures/edition.png"))
                    screenFlow.getScreenWidgets().addImage(new WidgetImageWrapper(new ResourceLocation("customclient:textures/edition.png"), "textures/edition.png", j,k,LogoRenderer.LOGO_WIDTH,LogoRenderer.LOGO_HEIGHT,1));
            }
        }
        if(screenFlow.getScreenWidgets() != null){
            screenFlow.getScreenWidgets().update();
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
