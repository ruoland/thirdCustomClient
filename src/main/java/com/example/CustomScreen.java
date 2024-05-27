package com.example;

import java.util.LinkedHashMap;

public class CustomScreen {
    private static final LinkedHashMap<String, ScreenFlow> screenMap = new LinkedHashMap<>();

    public static ScreenFlow getScreen(String name){
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
}
