package com.example;

import com.example.gui.event.ImageWidgetEvent;
import com.example.wrapper.*;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;

import javax.swing.*;
import java.nio.file.Path;

public class ScreenFlow {
    private Screen screen;
    private String screenName;
    private CustomScreenData data;

    private SwingHandler swingHandler = new SwingHandler();
    private WidgetHandler widgetHandler ;//loadScreenData 메서드에서 초기화됨
    private SelectWidgetHandler selectWidgetHandler;

    ScreenFlow(){

    }

    public WidgetHandler getScreenWidgets() {
        return widgetHandler;
    }

    public SelectWidgetHandler getSelectWidget() {
        return selectWidgetHandler;
    }

    public void reset(){
        selectWidgetHandler = null;
        swingHandler.swingClose();
        swingHandler = new SwingHandler();
        data = null;

    }

    public boolean hasSelectWidget(){
        return selectWidgetHandler != null;
    }
    public boolean hasImageWidget(String resouce){
        for(WidgetImageWrapper widgetImageWrapper : widgetHandler.getWidgetImageList()){
            if(widgetImageWrapper.getResource().toString().equals(resouce)){
                return true;
            }
        }
        return false;
    }

    public SwingHandler getSwingHandler() {
        return swingHandler;
    }

    public void setScreenName(String name){
        this.screenName = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void openScreen(Screen screen){
        this.screen = screen;
    }

    public void save(){
        if(data != null)
            data.save();
        else
            throw new NullPointerException("스크린의 데이터 없음");
    }

    public JsonObject getCustomData(){
        return this.data.getCustomObject();
    }

    public void loadScreenData(){
        widgetHandler = new WidgetHandler(screen);
        data = new CustomScreenData(this, screenName);
        data.initFiles(); //기본 파일 생성

        data.loadCustomWidgets();
        widgetHandler.loadDefaultWidgets();
        widgetHandler.makeCustomButtons();
        widgetHandler.update();


        if(screen instanceof ICustomBackground background)//백그라운드 설정 가능한 GUI라면
            background.setBackground(new ResourceLocation(data.background));
    }

    public void clickWidget(double mouseX, double mouseY){
        CustomWidgetWrapper clickedWidget = null;
        for(WidgetButtonWrapper buttonWrapper : widgetHandler.getWidgetDefaultButtonList())
        {
            if(buttonWrapper.isMouseOver(mouseX, mouseY)){
                clickedWidget = buttonWrapper;
                if(selectWidgetHandler != null && selectWidgetHandler.getSelectWidget() == buttonWrapper)
                    return;
                selectWidgetHandler = new SelectWidgetHandler((clickedWidget));
                swingHandler.openSwing(clickedWidget);
                System.out.println("기본 버튼에서ㅏ 선택됨" +clickedWidget);
                return;
            }
        }
        for(WidgetButtonWrapper buttonWrapper : widgetHandler.getWidgetButtonList())
        {
            if(buttonWrapper.isMouseOver(mouseX, mouseY)){
                if(selectWidgetHandler != null && selectWidgetHandler.getSelectWidget() == buttonWrapper)
                    return;
                clickedWidget = buttonWrapper;
                selectWidgetHandler = new SelectWidgetHandler((clickedWidget));
                swingHandler.openSwing(clickedWidget);
                System.out.println("버튼 선택됨" +clickedWidget);
                return;
            }
        }
        for(WidgetImageWrapper imageWrapper : widgetHandler.getWidgetImageList())
        {
            if(imageWrapper.isMouseOver(mouseX, mouseY)){
                if(selectWidgetHandler != null && selectWidgetHandler.getSelectWidget() == imageWrapper)
                    return;
                clickedWidget = imageWrapper;
                selectWidgetHandler = new SelectWidgetHandler((clickedWidget));
                swingHandler.openSwing(clickedWidget);
                System.out.println("이미지 선택됨" +clickedWidget);
                return;
            }
        }
        System.out.println(clickedWidget +"  - 위젯을 찾지 못함" + widgetHandler.getWidgetButtonList());
        swingHandler.swingClose();
    }
    public void dragWidget(double mouseX, double mouseY){
        selectWidgetHandler.setPosition((int) mouseX, (int) mouseY);
    }

    public void fileDropEvent(Path path){
        ResourceLocation resourceLocation = ScreenAPI.getDynamicTexture(path);
        if(resourceLocation == null) {
            return;
        }
        int select = JOptionPane.showOptionDialog(null, "어떤 걸로 설정할까요?", "이미지 불러오기", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"배경화면", "이미지", "취소"}, "취소");
        switch (select) {
            case JOptionPane.YES_OPTION -> {
                data.dynamicBackground = resourceLocation.toString();
                data.setBackground("customclient:"+ path.getFileName().toString());
                if(screen instanceof ICustomBackground background)//백그라운드 설정 가능한 GUI라면
                    background.setBackground(new ResourceLocation(data.background));
            }
            case JOptionPane.NO_OPTION -> {
            }
        }
        if(select == JOptionPane.YES_OPTION)
            NeoForge.EVENT_BUS.post(new ImageWidgetEvent.Background(screen, resourceLocation, path));
        else {
            WidgetImageWrapper image = new WidgetImageWrapper(resourceLocation, path.getFileName().toString(), 0, 0, screen.width, screen.height, 1);
            widgetHandler.addImage(image);
        }
    }

}
