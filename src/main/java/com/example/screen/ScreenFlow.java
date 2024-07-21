package com.example.screen;

import com.example.ICustomBackground;
import com.example.gui.event.ImageWidgetEvent;
import com.example.swing.SwingHandler;
import com.example.wrapper.widget.ButtonWrapper;
import com.example.wrapper.widget.ImageWrapper;
import com.example.wrapper.widget.WidgetWrapper;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.nio.file.Path;
import java.util.LinkedList;

/**
 * Handler로 직접 접근 하지 않고도 여기서 처리할 수 있도록
 */
public class ScreenFlow {
    private static final Logger logger = LoggerFactory.getLogger(ScreenFlow.class);

    private Screen screen;
    private String screenName;
    private CustomScreenData data;

    private SwingHandler swingHandler = new SwingHandler();
    private ScreenHandler screenHandler;//loadScreenData 메서드에서 초기화됨
    private SelectHandler selectHandler;

    ScreenFlow(){

    }

    public void reset(boolean dataToo){
        selectHandler = null;
        if(swingHandler != null)
            swingHandler.swingClose();
        swingHandler = new SwingHandler();
        if(dataToo)
            data = null;
    }

    public boolean hasSelectWidget(){
        return selectHandler != null;
    }

    public boolean hasImageWidget(String resouce){
        for(ImageWrapper imageWrapper : screenHandler.getImageList()){
            if(imageWrapper.getResource().toString().equals(resouce)){
                return true;
            }
        }
        return false;
    }

    public Screen getScreen() {
        return screen;
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
        else {
            logger.error("{}의 스크린에서 오류 발생", getScreenName());
            throw new NullPointerException("스크린의 데이터 없음");
        }
    }

    public boolean isTitle(){
        return getScreenName().equals("ScreenNewTitle");
    }
    public JsonObject getCustomData(){
        return this.data.getCustomObject();
    }
    public void loadScreenData(){
        logger.info("화면 데이터 로딩 중: {}", screenName);
        screenHandler = new ScreenHandler(screen);
        data = new CustomScreenData(this, screenName);
        data.initFiles();

        data.loadCustomWidgets();
        if(screenName.equals("ScreenNewTitle"))
            screenHandler.loadDefaultWidgets();
        screenHandler.makeCustomButtons();
        screenHandler.syncWithSwing();

        if(screen instanceof ICustomBackground background)
            background.setBackground(new ResourceLocation(data.background));
        logger.debug("로드된 기본 버튼: {}", screenHandler.getDefaultButtons());
        logger.debug("로드된 커스텀 버튼: {}", screenHandler.getButtons());
        logger.debug("로드된 이미지: {}", screenHandler.getImageList());
    }
    public boolean clickWidget(double mouseX, double mouseY){
        logger.debug("위젯 클릭 - 좌표: ({}, {})", mouseX, mouseY);
        LinkedList<WidgetWrapper> allWidgets = screenHandler.getAllWidget();

        for(WidgetWrapper clickedWidget : allWidgets) {
            if (clickedWidget.isMouseOver(mouseX, mouseY) ) {
                if(selectHandler != null && selectHandler.getWidget() == clickedWidget)
                    return true;
                this.swingHandler.openSwing(clickedWidget);
                selectHandler = new SelectHandler(clickedWidget);
                return true;
            }
        }
        for(WidgetWrapper widget : allWidgets) {
            AbstractWidget widget1 = widget.getWidget();

            if (widget.isMouseOver(mouseX, mouseY)) {
                logger.debug("위젯 이름 {}이 마우스 오버 됨 ", widget1.getMessage().getString());
            }

        }
        logger.debug("선택된 위젯을 찾지 못함{} ", screen.renderables);


        return false;
    }

    @Nullable
    public SelectHandler selectWidget(){
        return selectHandler;
    }

    public ScreenHandler getWidget() {
        return screenHandler;
    }


    public void dragWidget(double mouseX, double mouseY){
        selectHandler.setPosition((int) mouseX, (int) mouseY);
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
            ImageWrapper image = new ImageWrapper(resourceLocation, path.getFileName().toString(), 0, 0, screen.width, screen.height, 1);
            screenHandler.addImage(image);
        }
    }

    public void renderImageWidget(GuiGraphics pGuiGraphics){
        if(!screenHandler.getImageList().isEmpty()) {
            for (ImageWrapper imageWrapper : screenHandler.getImageList()) {
                imageWrapper.render(pGuiGraphics);
            }
        }
    }

    public void addButton(String name, int width, int height, int x, int y){
        ButtonWrapper button = new ButtonWrapper(new Button.Builder(Component.literal(name), pButton -> {
        }).size(width, height).pos(x, y).build());
        screenHandler.addButton(button);
    }

    public static boolean isKeyDown(int key){
        Minecraft mc = Minecraft.getInstance();
        long windowLong = mc.getWindow().getWindow();
        return InputConstants.isKeyDown(windowLong, key);
    }
}
