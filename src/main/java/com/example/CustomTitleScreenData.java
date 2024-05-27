package com.example;

import com.example.wrapper.CustomWidgetWrapper;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;

public class CustomTitleScreenData extends CustomScreenData{
    private ArrayList<CustomWidgetWrapper.WidgetButtonWrapper> defaultWidgetArrayList = new ArrayList<>();

    CustomTitleScreenData(Screen screen, String name){
        super(screen, name);

        //Screen에 기본 위젯, 불러오기
        loadDefaultWidget();

        //위젯과 정보 동기화
        syncWithDefaultWidget();
    }

    @Override
    public void update(){
        for(int i = 0; i < getCurrentScreen().children().size(); i++) {
            defaultWidgetArrayList.get(i).abstractWidget = (AbstractWidget) getCurrentScreen().children().get(i);
            defaultWidgetArrayList.get(i).dataUpdate();
        }
    }

    public void loadDefaultWidget(){
        if(defaultWidgetArrayList == null || defaultWidgetArrayList.isEmpty()) {
            for(int i = 0; i < getCurrentScreen().children().size();i++) {
                defaultWidgetArrayList.add(new CustomWidgetWrapper.WidgetButtonWrapper((AbstractWidget) getCurrentScreen().children().get(i)));
            }
        }
    }


    public void syncWithDefaultWidget(){
        for(int i = 0; i < getCurrentScreen().children().size(); i++) {
            defaultWidgetArrayList.get(i).abstractWidget = (AbstractWidget) getCurrentScreen().children().get(i);
            defaultWidgetArrayList.get(i).loadToMCWidget();
        }
        ICustomBackground customBackground = (ICustomBackground) getCurrentScreen();
        customBackground.setBackground(new ResourceLocation(getBackground()));
    }
}
