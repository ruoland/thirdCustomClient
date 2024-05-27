package com.example;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;

//스크린을 찾거나, 스크린들의 전반에 걸쳐 관리하는 클래스
public class CustomScreenMod {
    private static final LinkedHashMap<String, ScreenFlow> screenMap = new LinkedHashMap<>();
    private static String recntlyName;
    private static boolean editMode;
    public static ScreenFlow getScreen(String name){
        recntlyName = name;
        if(screenMap.containsKey(name)) {
            return screenMap.get(name);
        }
        else {
            ScreenFlow screenFlow = new ScreenFlow();
            screenFlow.setScreenName(name);
            screenMap.put(name, screenFlow);
            return screenMap.get(name);
        }
    }

    public static ScreenFlow getRecntlyScreen() {
        return screenMap.get(recntlyName);
    }

    public static void changeEditMode(Screen screen){
        editMode = !editMode;
        if(!editMode){ //편집 모드 종료, 파일 저장 후 스윙 정리
            getRecntlyScreen().save();
            if(getRecntlyScreen().getScreenWidgets().isSwingOpen())
                swingHandler.swingClose();
        }
        else {//편집 모드 실행, GUI 데이터 불러옴
            new CustomScreenData(this , screen.getClass().getSimpleName());

            if(screen instanceof ICustomBackground background)//백그라운드 설정 가능한 GUI라면
                background.setBackground(new ResourceLocation(data.background));
        }
    }
    public static boolean isEditMode() {
        return editMode;
    }

}
