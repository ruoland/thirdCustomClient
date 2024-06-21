package com.example.screen;

import com.example.ICustomRenderable;
import com.example.wrapper.widget.ButtonWrapper;
import com.example.wrapper.widget.ImageWrapper;
import com.example.wrapper.widget.WidgetWrapper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.LinkedList;

/*
    한 스크린의 위젯들을 관리하는 클래스
    위젯을 추가하거나 특정 위젯을 가져오려 할 때 여기서 가져옴
 */
public class ScreenHandler {
    private Screen screen;
    private ArrayList<ButtonWrapper> defaultButtons = new ArrayList<>();
    private ArrayList<ButtonWrapper> buttons = new ArrayList<>();
    private ArrayList<ImageWrapper> images = new ArrayList<>();

    public ScreenHandler(Screen screen){
        this.screen = screen;
    }

    /**
     * 불러온 위젯의 정보를 동기화 하기(SwingWidget에서 값을 변경 했을 때 사용 되는 메서드)
     */
    public void syncWithSwing(){
        LinkedList<WidgetWrapper> wrapperLinkedList = getAllWidget();
        wrapperLinkedList.removeAll(getImageList());
        for(WidgetWrapper widget : wrapperLinkedList ){
            widget.update();
        }
    }

    public ArrayList<ButtonWrapper> getDefaultButtons() {
        return defaultButtons;
    }

    public ArrayList<ButtonWrapper> getButtons() {
        return buttons;
    }

    public LinkedList<WidgetWrapper> getAllWidget(){
        LinkedList<WidgetWrapper> allWidget = new LinkedList<>();
        allWidget.addAll(getButtons());
        allWidget.addAll(getDefaultButtons());
        allWidget.addAll(getImageList());
        return allWidget;
    }
    public ButtonWrapper getPressedButton(double mouseX, double mouseY) {
        for(ButtonWrapper buttonWrapper : getButtons())
        {
            if(buttonWrapper.isMouseOver(mouseX, mouseY))
                return buttonWrapper;
        }
        return null;
    }

    public ArrayList<ImageWrapper> getImageList() {
        return images;
    }

    public void loadDefaultWidgets(){
        for(int i = 0; i < screen.children().size();i++){
            AbstractWidget widget = (AbstractWidget) screen.children().get(i);
            if(defaultButtons.size() <= i)
                defaultButtons.add(new ButtonWrapper(widget));
            else
                defaultButtons.get(i).setAbstractWidget(widget);

        }

        
    }

    public void addTextfield(ButtonWrapper data){
        buttons.add(data);
        ICustomRenderable customRenderable = (ICustomRenderable) screen ;
        customRenderable.addRenderableWidget(data.getWidget());
    }

    public void addButton(ButtonWrapper data){
        buttons.add(data);
        ICustomRenderable customRenderable = (ICustomRenderable) screen ;
        customRenderable.addRenderableWidget(data.getWidget());
    }
    public void makeCustomButtons(){
        for(ButtonWrapper data : buttons){
            AbstractWidget abstractWidget = new Button.Builder(Component.literal(data.getMessage()), (Button.OnPress) pButton -> {
            }).size(data.getWidth(), data.getHeight()).pos(data.getX(), data.getY()).build();
            screen.renderables.add(abstractWidget);
            data.setAbstractWidget(abstractWidget);
            System.out.println(data+ " 커스텀 데이터가 추가됨");
        }
    }
    public void addImage(ImageWrapper widgetImage){
        widgetImage.setTexture("customclient:"+widgetImage.getResource());
        images.add(widgetImage);
    }

    public void renderImage(GuiGraphics guiGraphics){
        for(ImageWrapper image : images){
            if(image.getWidget() == null)
                image.createFakeWidget(image.getX(),image.getY(),image.getWidth(),image.getHeight(), image.getMessage());
            image.render(guiGraphics);
        }
    }

}

