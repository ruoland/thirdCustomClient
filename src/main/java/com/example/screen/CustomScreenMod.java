package com.example.screen;

import customclient.CustomClient;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static void toggleEditMode(Screen screen){
        editMode = !editMode;
        ScreenFlow screenFlow = getScreen(screen);
        if(!editMode){ //편집 모드 종료, 파일 저장 후 스윙 정리
            screenFlow.save();
            if(screenFlow.getSwingHandler().isSwingOpen())
                screenFlow.getSwingHandler().swingClose();
            screenFlow.getSwingHandler().swingClose();
        }
        else {//편집 모드 실행, GUI 데이터 불러옴
            try {

                screenFlow.openScreen(screen);
                screenFlow.loadScreenData();
            }catch (NullPointerException exception){
                exception.printStackTrace();
            }
        }
        logger.info("편집 모드 변경. 현재 모드: {}", editMode);
    }

    public static boolean isEditMode() {
        return editMode;
    }

}
