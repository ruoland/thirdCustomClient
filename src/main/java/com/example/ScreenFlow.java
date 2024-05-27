package com.example;

import com.example.wrapper.*;
import net.minecraft.client.gui.screens.Screen;

public class ScreenFlow {
    private Screen screen;
    private String screenName;
    private CustomScreenData data;
    private SwingHandler swingHandler = new SwingHandler();
    private WidgetHandler widgetHandler;
    private SelectWidgetHandler selectWidgetHandler;

    ScreenFlow(){
        widgetHandler = new WidgetHandler(screen);
    }

    public WidgetHandler getScreenWidgets() {
        return widgetHandler;
    }


    public SelectWidgetHandler getSelectWidget() {
        return selectWidgetHandler;
    }

    public SwingHandler getSwingHandler() {
        return swingHandler;
    }

    public void setScreenName(String name){
        this.screenName = name;
    }
    public void openScreen(Screen screen){
        this.screen = screen;
    }

    public void save(){
        data.save();;
    }


    /**
     * GUI 초기화 이후에 호출되어야 함
     * 왜냐면 초기화 메서드 호출 이전에 이 메서드 호출시 기본 GUI 위젯을 불러올 수 없음
     */
    public void loadScreenData(){
        data = new CustomScreenData(screen, this, screenName);
        data.initFiles(); //기본 파일 생성
        if(!screen.children().isEmpty())
            widgetHandler.loadDefaultWidgets();
        data.loadCustomWidgets();
        widgetHandler.makeCustomButtons();
    }


    public void clickWidget(double mouseX, double mouseY){
        CustomWidgetWrapper clickedWidget = null;
        for(WidgetButtonWrapper buttonWrapper : widgetHandler.getWidgetButtonList())
        {
            if(buttonWrapper.isMouseOver(mouseX, mouseY)){
                clickedWidget = buttonWrapper;
            }
        }

        for(WidgetImageWrapper imageWrapper : widgetHandler.getWidgetImageList())
        {
            if(imageWrapper.isMouseOver(mouseX, mouseY)){
                clickedWidget = imageWrapper;
            }
        }

        selectWidgetHandler.selectWidget(clickedWidget);
        swingHandler.openSwing(clickedWidget);
    }
    public void dragWidget(double mouseX, double mouseY){
        selectWidgetHandler.addSelectWidth();
    }
}
