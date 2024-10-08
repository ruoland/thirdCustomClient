package com.example.event;

import com.example.ScreenNewTitle;
import com.example.screen.CustomScreenMod;
import com.example.screen.ScreenFlow;
import com.example.wrapper.widget.ButtonWrapper;
import com.example.wrapper.widget.ImageWrapper;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class TitleInitEvent {
    private static final Logger logger = LoggerFactory.getLogger(TitleInitEvent.class);


    /**
     * 이 클래스에서 커스터마이징할 스크린을 지정함
     * 현재는 Title Screen 클래스만 지정
     */
    int openCount = 0;
    @SubscribeEvent
    public void screenOpenEvent(ScreenEvent.Opening event) {
        String screenName = CustomScreenMod.getScreenName(event.getNewScreen());
        logger.info("화면이 열렸습니다. {}",screenName);

        if (screenName != null && screenName.equals("TitleScreen")) {
            logger.info("화면 열림 이벤트: {}, 새로운 화면으로 대체합니다. ", event.getNewScreen().getTitle().getString());
            setFinalStatic("MINECRAFT_LOGO", new ResourceLocation("customclient:textures/logo.png"));
            setFinalStatic("MINECRAFT_EDITION", new ResourceLocation("customclient:textures/logo.png"));
            ScreenNewTitle newTitle = new ScreenNewTitle();
            event.setNewScreen(newTitle);
            ScreenFlow screenFlow =  CustomScreenMod.createScreenFlow("ScreenNewTitle");
            screenFlow.openScreen(event.getNewScreen()); //스크린 열림
        }
    }
    @SubscribeEvent
    public void screenCloseEvent(ScreenEvent.Closing event) {
        String screenName = CustomScreenMod.getScreenName(event.getScreen());
        if(screenName != null && screenName.equals("ScreenNewTitle")){
            logger.info("새로운 타이틀 스크린이 닫혔습니다. {}",screenName);
        }
    }



    @SubscribeEvent
    public void screenMousePressedPost(ScreenEvent.MouseButtonPressed.Pre event) {
        logger.debug("마우스 클릭 액션 이벤트 - 좌표: ({}, {})", event.getMouseX(), event.getMouseY());
        ScreenFlow screenFlow = CustomScreenMod.getScreen(event.getScreen());
        if(event.getButton() == 0 && !CustomScreenMod.isEditMode() && CustomScreenMod.hasScreen(event.getScreen())) {
            for(ButtonWrapper buttonWrapper : screenFlow.getWidget().getDefaultButtons()){
                if(buttonWrapper.isMouseOver(event.getMouseX(), event.getMouseY())){
                    logger.info("클릭된 버튼 : {}, 액션 : {}",buttonWrapper.getMessage(), buttonWrapper.getAction());
                    buttonWrapper.runAction();
                    event.setCanceled(true);
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public void screenPostInitEvent(ScreenEvent.Init.Post event){
        if(isTitle(event.getScreen())) {
            ScreenFlow screenFlow =  CustomScreenMod.getScreen("ScreenNewTitle");
            screenFlow.loadScreenData();
            //타이틀에서는 로고가 있으니까 여기서 로고 정보 저장하고 불러옴
            if(screenFlow.getCustomData().has("logoVisible")){
                CustomScreenMod.setLogoVisible( screenFlow.getCustomData().getAsJsonPrimitive("logoVisible").getAsBoolean());
            }
            else{
                screenFlow.getCustomData().add("logoVisible", new JsonPrimitive(CustomScreenMod.isLogoVisible()));
            }

            if(CustomScreenMod.isLogoVisible())
                logoRender(screenFlow);
            screenFlow.save();
            screenFlow.getWidget().syncWithSwing();
            logger.info("위젯 동기화 완료 ");
            loadTitleWidgets();
            screenFlow.onScreenResize(event.getScreen().width,event.getScreen().height);
        }
    }

    public static void loadTitleWidgets(){
        ScreenFlow screenFlow = CustomScreenMod.getScreen("ScreenNewTitle");

        for(int i = 0; i < screenFlow.getScreen().children().size();i++){
            AbstractWidget widget = (AbstractWidget) screenFlow.getScreen().children().get(i);
            ArrayList<ButtonWrapper> defaultButtons = screenFlow.getWidget().getDefaultButtons();
            if(defaultButtons.size() <= i){
                defaultButtons.add(new ButtonWrapper(widget));
            }
            else
                defaultButtons.get(i).setAbstractWidget(widget);
        }
    }
    private void logoRender(ScreenFlow screenFlow){
        Screen newTitle = Minecraft.getInstance().screen;
        int i = newTitle.width / 2 - 128;
        int j = newTitle.width / 2 - 64;
        int k = 30 + 44 - 7;
        boolean logo = false, edition = false;

        //중복 추가를 막기 위해
        for(ImageWrapper resource : screenFlow.getWidget().getImageList()){
            if(resource.getResource().toString().equals("customclient:textures/minecraft.png"))
                logo = true;
            if(resource.getResource().toString().equals("customclient:textures/edition.png"))
                edition = true;
        }

        if(!logo)
            screenFlow.getWidget().addImage(new ImageWrapper("textures/minecraft.png", i, 20, LogoRenderer.LOGO_WIDTH, LogoRenderer.LOGO_HEIGHT, 1));
        if(!edition)
            screenFlow.getWidget().addImage(new ImageWrapper("textures/edition.png", j, k, 128, 14, 1));
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
        return screen instanceof ScreenNewTitle;
    }
}
