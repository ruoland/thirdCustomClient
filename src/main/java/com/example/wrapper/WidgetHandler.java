package com.example.wrapper;

import com.example.ICustomRenderable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;

/*
    한 스크린의 위젯들을 관리하는 클래스
    위젯을 추가하거나 특정 위젯을 가져오려 할 때 여기서 가져옴
 */
public class WidgetHandler {
    private Screen screen;
    private ArrayList<WidgetButtonWrapper> widgetDefaultButtonList = new ArrayList<>();

    private ArrayList<WidgetButtonWrapper> widgetButtonList = new ArrayList<>();
    private ArrayList<WidgetImageWrapper> widgetImageList = new ArrayList<>();

    public WidgetHandler(Screen screen){
        this.screen = screen;
    }

    /**
     * 불러온 위젯의 정보를 동기화 하기(SwingWidget에서 값을 변경 했을 때 사용 되는 메서드)
     */
    public void update(){
        for(int i = 0; i < widgetButtonList.size(); i++){

        }
    }

    public ArrayList<WidgetButtonWrapper> getWidgetDefaultButtonList() {
        return widgetDefaultButtonList;
    }

    public ArrayList<WidgetButtonWrapper> getWidgetButtonList() {
        return widgetButtonList;
    }

    public ArrayList<WidgetImageWrapper> getWidgetImageList() {
        return widgetImageList;
    }

    public void loadDefaultWidgets(){
        for(int i = 0; i < screen.children().size();i++){
            AbstractWidget widget = (AbstractWidget) screen.children().get(i);
            widgetDefaultButtonList.add(new WidgetButtonWrapper(widget));

        }
        widgetButtonList.addAll(widgetDefaultButtonList);
    }

    public void addTextfield(WidgetButtonWrapper data){
        widgetButtonList.add(data);
        ICustomRenderable customRenderable = (ICustomRenderable) screen ;
        customRenderable.addRenderableWidget(data.getWidget());
    }

    public void addButton(WidgetButtonWrapper data){
        widgetButtonList.add(data);
        ICustomRenderable customRenderable = (ICustomRenderable) screen ;
        customRenderable.addRenderableWidget(data.getWidget());
    }
    public void makeCustomButtons(){
        for(WidgetButtonWrapper data : widgetButtonList){
            screen.renderables.add(new Button.Builder(Component.literal(data.getMessage()), (Button.OnPress) pButton -> {
            }).size(data.getWidth(), data.getHeight()).pos(data.getX(), data.getY()).build());
        }
    }
    public void addImage(WidgetImageWrapper widgetImage){
        widgetImage.setTexture("customclient:"+widgetImage.getResource());
        widgetImageList.add(widgetImage);
    }

    public void renderImage(GuiGraphics guiGraphics){
        for(WidgetImageWrapper image : widgetImageList){
            image.render(guiGraphics);
        }
    }

}

