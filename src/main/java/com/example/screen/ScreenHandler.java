package com.example.screen;

import com.example.ICustomRenderable;
import com.example.wrapper.widget.ButtonWrapper;
import com.example.wrapper.widget.ImageWrapper;
import com.example.wrapper.widget.WidgetWrapper;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;

/*
    한 스크린의 위젯들을 관리하는 클래스
    위젯을 추가하거나 특정 위젯을 가져오려 할 때 여기서 가져옴
 */
public class ScreenHandler {
    private Screen screen;
    private final ArrayList<ButtonWrapper> defaultButtons = new ArrayList<>();
    private final ArrayList<ButtonWrapper> buttons = new ArrayList<>();
    private final ArrayList<ImageWrapper> images = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(ScreenHandler.class);

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
            logger.info("동기화 진행 중 {}", widget.getMessage());
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
        logger.info("기본 위젯 로딩 중");
        boolean add = false;

        for(int i = 0; i < 9;i++){
            AbstractWidget widget = (AbstractWidget) screen.children().get(i);
            if(defaultButtons.isEmpty())
                add = true;
            if(add)
                defaultButtons.add(new ButtonWrapper(widget));

            defaultButtons.get(i).setAbstractWidget(widget);
            logger.debug("스크린 {}에 버튼 추가 됨{}",screen.getTitle().getString(), widget.getMessage().getString());
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
        logger.info("커스텀 버튼 생성 중");
        for(ButtonWrapper data : buttons){
            if(data.isVisible()) {
                if(data.getWidget() == null) {
                    AbstractWidget abstractWidget = new Button.Builder(Component.literal(data.getMessage()), (Button.OnPress) pButton -> {
                    }).size(data.getWidth(), data.getHeight()).pos(data.getX(), data.getY()).build();
                    screen.renderables.add(abstractWidget);
                    data.setAbstractWidget(abstractWidget);
                } else {
                    AbstractWidget widget = data.getWidget();
                    widget.setWidth(data.getWidth());
                    widget.setHeight(data.getHeight());
                    widget.setPosition(data.getX(), data.getY());
                    widget.setMessage(Component.literal(data.getMessage()));
                }
                data.getWidget().visible = true;
                data.getWidget().active = true;
            } else {
                if(data.getWidget() != null) {
                    data.getWidget().visible = false;
                    data.getWidget().active = false;
                }
            }
            logger.debug("커스텀 버튼 업데이트됨: {}", data);
        }
    }
    public void addImage(ImageWrapper widgetImage){
        widgetImage.setTexture("customclient:"+widgetImage.getResource());
        images.add(widgetImage);
    }


}

