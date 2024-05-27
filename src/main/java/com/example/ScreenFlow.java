package com.example;

import com.example.wrapper.CustomWidgetWrapper;
import net.minecraft.client.gui.screens.Screen;

public class ScreenFlow {
    private Screen screen;
    private String screenName;
    private CustomScreenData data;
    private CustomWidgetWrapper clickedWidget;

    public void setScreenName(String name){
        this.screenName = name;
    }
    public void openScreen(Screen screen){
        this.screen = screen;

    }

    public void loadScreenData(){
        data = new CustomScreenData(screen, screenName);
        data.initFiles(); //기본 파일 생성
        if(!screen.children().isEmpty())
            data.loadDefaultWidgets();
        data.loadCustomWidgets();
        data.makeCustomButtons();
    }

    public void clickWidget(double mouseX, double mouseY){
        for(CustomWidgetWrapper.WidgetButtonWrapper buttonWrapper : data.getWidgetButtonList())
        {
            if(buttonWrapper.isMouseOver(mouseX, mouseY)){
                clickedWidget = buttonWrapper;
            }
        }
        for(CustomWidgetWrapper.WidgetImageWrapper imageWrapper : data.getWidgetImageList())
        {
            if(imageWrapper.isMouseOver(mouseX, mouseY)){
                clickedWidget = imageWrapper;
            }
        }

    }

}
