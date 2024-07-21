package com.example.screen;

import com.example.ICustomRenderable;
import com.example.wrapper.widget.ButtonWrapper;
import com.example.wrapper.widget.ImageWrapper;
import com.example.wrapper.widget.WidgetWrapper;
import net.minecraft.client.Minecraft;
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
public class WidgetHandler {
    private Screen screen;
    private final ArrayList<ButtonWrapper> defaultButtons = new ArrayList<>();
    private final ArrayList<ButtonWrapper> buttons = new ArrayList<>();
    private final ArrayList<ImageWrapper> images = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(WidgetHandler.class);

    public WidgetHandler(Screen screen){
        this.screen = screen;
        logger.debug("위젯 핸들러 생성됨 {}", screen.getTitle().getString());
    }

    /**
     * 불러온 위젯의 정보를 동기화 하기(SwingWidget에서 값을 변경 했을 때 사용 되는 메서드)
     */
    public void syncWithSwing(){
        LinkedList<WidgetWrapper> wrapperLinkedList = getAllWidget();
        wrapperLinkedList.removeAll(getImageList());
        for(WidgetWrapper widget : wrapperLinkedList ){
            logger.info("동기화 진행 중 {}", widget.getMessage());
            if(widget.getWidget() == null){
                logger.warn("연결된 위젯이 없습니다. 스킵: {}", widget.getMessage());
                continue;
            }
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
            ButtonWrapper buttonWrapper = defaultButtons.get(i);
            buttonWrapper.setAbstractWidget(widget);
            buttonWrapper.setX((int) (buttonWrapper.getX() * ((double) Minecraft.getInstance().getWindow().getScreenWidth()) / (double) buttonWrapper.getDesignWidth()));
            buttonWrapper.setY((int) (buttonWrapper.getY() * ((double) Minecraft.getInstance().getWindow().getScreenHeight()) / (double) buttonWrapper.getDesignHeight()));
            logger.debug("스크린 {}에 기본 버튼 추가 됨{}",screen.getTitle().getString(), widget.getMessage().getString());
        }
    }

    public void addTextfield(ButtonWrapper data){
        buttons.add(data);
        ICustomRenderable customRenderable = (ICustomRenderable) screen ;
        customRenderable.addRenderableWidget(data.getWidget());
    }

    public void addNewButton(String name, int width, int height, int x, int y){
        ButtonWrapper button = new ButtonWrapper(new Button.Builder(Component.literal(name), pButton -> {
        }).size(width, height).pos(x, y).build());
        addNewButton(button);
    }

    public void addNewButton(ButtonWrapper data){
        buttons.add(data);
        ICustomRenderable customRenderable = (ICustomRenderable) screen;
        customRenderable.addRenderableWidget(data.getWidget());
        logger.debug("새로운 버튼 하나 생성하였습니다. "+data.getMessage());
    }
    public void makeCustomButtons(){
        logger.info("커스텀 버튼 생성 중");
        for(ButtonWrapper buttonWrapper : buttons){
            if(buttonWrapper.isVisible()) {
                //getWidget이 없다면 새로 생성된 버튼이라 판단함
                if(buttonWrapper.getWidget() == null) {
                    //제대로 정보 생성 후 등록하기
                    AbstractWidget abstractWidget = new Button.Builder(Component.literal(buttonWrapper.getMessage()), (Button.OnPress) pButton -> {
                    }).size(buttonWrapper.getWidth(), buttonWrapper.getHeight()).pos(buttonWrapper.getX(), buttonWrapper.getY()).build();
                    screen.renderables.add(abstractWidget);
                    buttonWrapper.setAbstractWidget(abstractWidget);
                    logger.debug("버튼에 위젯이 없어 등록함{}", abstractWidget.getMessage());
                }
                buttonWrapper.getWidget().visible = true;
                buttonWrapper.getWidget().active = true;
            } else {
                if(buttonWrapper.getWidget() != null) {
                    buttonWrapper.getWidget().visible = false;
                    buttonWrapper.getWidget().active = false;
                }
            }
        }
    }
    public void addImage(ImageWrapper widgetImage){
        widgetImage.setTexture("customclient:"+widgetImage.getResource());
        images.add(widgetImage);
        logger.debug("이미지 추가 됨 {}",widgetImage.getResource());
    }


}

