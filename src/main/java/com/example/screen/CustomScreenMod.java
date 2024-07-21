package com.example.screen;

import com.example.wrapper.widget.ButtonWrapper;
import com.example.wrapper.widget.ImageWrapper;
import customclient.CustomClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;

//스크린을 찾거나, 스크린들의 전반에 걸쳐 관리하는 클래스
public class CustomScreenMod {
    private static final Logger logger = LoggerFactory.getLogger(CustomScreenMod.class);

    public static final ResourceLocation DEFAULT_BACKGROUND_IMAGE = new ResourceLocation(CustomClient.MODID, "textures/screenshot.png");

    private static final LinkedHashMap<String, ScreenFlow> screenMap = new LinkedHashMap<>();

    private static boolean editMode;
    private static boolean mcLogoVisible = true;

    @Nullable
    public static ScreenFlow getScreen(Screen screen){
        logger.debug("화면 가져오기 요청: {}", screen.getTitle().getString());
        //StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        return getScreen(screen.getTitle().getString());
    }

    @Nullable
    public static ScreenFlow getScreen(String name){
        if(screenMap.containsKey(name)) {
            return screenMap.get(name);
        }
        return null;
    }

    public static ScreenFlow createScreenFlow(String name){
            ScreenFlow screenFlow = new ScreenFlow();
            screenFlow.setScreenName(name);
            screenMap.put(name, screenFlow);
            System.out.println("스크린 흐름 생성됨"+name);
            return screenMap.get(name);
    }
    public static boolean hasScreen(Screen screen){
        return hasScreen(screen.getTitle().getString());
    }
    public static boolean hasScreen(String name){
        return screenMap.containsKey(name);
    }

    public static String getScreenName(Screen screen){
        switch (screen.getClass().getSimpleName()) {
            case "TitleScreen":
                return "TitleScreen";
            case "ScreenNewTitle":
                return "ScreenNewTitle";
            case "ScreenUserCustom":
                return "ScreenUserCustom";
        }
        return null;
    }

    public static boolean isLogoVisible() {
        return mcLogoVisible;
    }

    public static void setLogoVisible(boolean mcLogoVisible) {
        CustomScreenMod.mcLogoVisible = mcLogoVisible;
    }

    public static void changeEditMode(Screen screen){
        editMode = !editMode;

        ScreenFlow screenFlow = getScreen(screen);

        if(!editMode){ //편집 모드 종료, 파일 저장 후 스윙 정리
            screenFlow.save();
            if(screenFlow.getSwingHandler().isSwingOpen())
                screenFlow.getSwingHandler().swingClose();
            screenFlow.reset(true);
            screenFlow.getSwingHandler().swingClose();
        }
        else {//편집 모드 실행, GUI 데이터 불러옴
            screenFlow.loadScreenData();
        }
        logger.info("편집 모드 변경. 현재 모드: {}", editMode);
    }
    public static boolean isEditMode() {
        return editMode;
    }

    public static void logoRender(ScreenFlow screenFlow){
        Screen newTitle = Minecraft.getInstance().screen;
        int i = newTitle.width / 2 - 128;
        int j = newTitle.width / 2 - 64;
        int k = 30 + 44 - 7;
        boolean logo = false, edition = false;
        for(ImageWrapper resource : screenFlow.getWidget().getImageList()){
            if(resource.getResource().toString().equals("customclient:textures/minecraft.png"))
                logo = true;
            if(resource.getResource().toString().equals("customclient:textures/edition.png"))
                edition = true;
        }
        if(!logo)
            screenFlow.getWidget().addImage(new ImageWrapper(new ResourceLocation("customclient:textures/minecraft.png"), "textures/minecraft.png", i, 20, LogoRenderer.LOGO_WIDTH, LogoRenderer.LOGO_HEIGHT, 1));
        if(!edition)
            screenFlow.getWidget().addImage(new ImageWrapper(new ResourceLocation("customclient:textures/edition.png"), "textures/edition.png", j, k, 128, 14, 1));
    }


    public static void loadTitleWidgets(){
        ScreenFlow screenFlow = getScreen("ScreenNewTitle");

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
}
