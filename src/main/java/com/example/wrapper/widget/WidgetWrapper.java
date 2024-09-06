package com.example.wrapper.widget;

import com.example.screen.CustomScreenMod;
import com.example.screen.ScreenFlow;
import com.mojang.realmsclient.RealmsMainScreen;

import customclient.FakeTextureWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.SafetyScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.gui.ModListScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WidgetWrapper implements IWidget {
    private static final Logger logger = LoggerFactory.getLogger(WidgetWrapper.class);

    private transient AbstractWidget abstractWidget;
    private int x, y, width, height;
    private String texture, message, customFont;
    private float alpha = 1;
    private boolean visible = true, lock = false;
    private String action, value;
            private int id;

    public WidgetWrapper() {
    }

    WidgetWrapper(AbstractWidget widget) {
        this.abstractWidget = widget;
        abstractWidget.active =true;
    }

    public int getColor() {
        return abstractWidget.getFGColor();
    }

    public int getHeight() {
        return abstractWidget == null ? height: abstractWidget.getHeight();
    }

    public String getCustomFont() {
        return customFont;
    }

    public int getWidth() {
        return abstractWidget == null ? width : abstractWidget.getWidth();
    }

    public boolean isVisible() {
        if(CustomScreenMod.isEditMode())
            return true;
        else
            return abstractWidget == null ? visible: abstractWidget.visible;
    }

    public void setVisible(boolean visible) {
        abstractWidget.visible = visible;
        this.visible = visible;
    }

    public void setSize(int width, int height){
        this.width = width;
        this.height = height;
    }

    public void setWidth(int width) {
        if(width <= 0)
            return;
        this.width = width;
        abstractWidget.setWidth(width);
    }

    public void setHeight(int height) {
        if(height <= 0)
            return;
        this.height = height;
        abstractWidget.setHeight(height);
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;

        abstractWidget.setPosition(x, y);
    }
    public int getX() {
        return abstractWidget == null ? x:  abstractWidget.getX();
    }

    public void setX(int x) {
        this.x = x;
        abstractWidget.setX(x);
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        abstractWidget.setAlpha(alpha);
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }
    public void setTexture(String texture){
        this.texture = texture;
    }

    public int getY() {
        return abstractWidget == null ? y:  abstractWidget.getY();
    }

    public void setY(int y) {
        this.y = y;
        abstractWidget.setY(y);
    }

    public float getAlpha() {
        return alpha;
    }

    public void update(){
        createFakeWidget(getX(), getY(), getWidth(), getHeight(), getMessage());
        this.setPosition(x, y);
        this.setHeight(height);
        this.setWidth(width);
        this.setVisible(isVisible());
        this.setAlpha(getAlpha());

        this.setMessage(message);

    }
    public boolean isMouseOver(double mouseX, double mouseY ){
        if(abstractWidget ==null ) {
            if(texture == null)
                logger.error("클릭한 버튼에 위젯이 연결되어 있지 않음 이 위젯 정보:{}", this);
            return false;
        }
        return mouseX >= (double)getWidget().getX() &&
                mouseY >= (double)getWidget().getY() &&
                mouseX < (double)(getWidget().getX() + getWidget().getWidth()) &&
                mouseY < (double)(getWidget().getY() + getWidget().getHeight()) && visible;
    }
    public boolean canSelectByMouse(double mouseX, double mouseY ){
        return !isLock() && abstractWidget.isMouseOver(mouseX, mouseY);
    }

    public void setCustomFont(String customFont) {
        this.customFont = customFont;
    }

    public void setMessage(String message){
        this.message = message;
        Component textComponent;
        ResourceLocation fontLocation = null;
        if(customFont != null)
             fontLocation = ResourceLocation.tryBuild("customclient", customFont);
        
        if(!ScreenFlow.getGlobalFont().getPath().isEmpty()){
            textComponent = Component.literal(message).withStyle(Style.EMPTY.withFont(ScreenFlow.getGlobalFont()));
            logger.info("글로벌 폰트:{}",ScreenFlow.getGlobalFont());
        }
        else if(customFont != null && fontLocation != null){

            textComponent = Component.literal(message).withStyle(Style.EMPTY.withFont(fontLocation));
            logger.info(new ResourceLocation("customclient", customFont).toString());
        }
        else{
            textComponent = Component.literal(message);

        }

        getWidget().setMessage(textComponent);
    }

    public void setAction(String action){
        logger.debug("1.액션이 {}로 설정 되었습니다.",action);
        this.action = action;
    }

    public void setAbstractWidget(AbstractWidget abstractWidget) {
        this.abstractWidget = abstractWidget;

    }

    public AbstractWidget getWidget() {
        return abstractWidget;
    }

    public String getMessage() {
        return message;
    }

    public String getAction() {
        return action;
    }

    public String getValue() {
        return value;
    }

    public boolean runAction(){
        Minecraft mc = Minecraft.getInstance();
        if(action == null || action.contains("선택안함"))
            return false;
        ScreenFlow screenFlow =  CustomScreenMod.getScreen(mc.screen);
        if(screenFlow == null)
            screenFlow = CustomScreenMod.createScreenFlow("null");


        if(action.contains("열기")) {
            String value = action.split(":")[1];
            switch (value) {
                case ("맵 선택"):
                    mc.setScreen(new SelectWorldScreen(screenFlow.getScreen()));
                    break;
                case ("멀티"):
                    Screen screen = (mc.options.skipMultiplayerWarning ? new JoinMultiplayerScreen(screenFlow.getScreen()) : new SafetyScreen(screenFlow.getScreen()));
                    mc.setScreen(screen);
                    break;
                case ("설정"):
                    mc.setScreen(new OptionsScreen(screenFlow.getScreen(), mc.options));
                    break;
                case ("모드"):
                    mc.setScreen(new ModListScreen(screenFlow.getScreen()));
                    break;
                case ("렐름"):
                    mc.setScreen(new RealmsMainScreen(mc.screen));
                    break;

                case "언어":
                    mc.setScreen(new LanguageSelectScreen(mc.screen, mc.options, mc.getLanguageManager()));
                    break;
                case "접근성":
                    mc.setScreen(new AccessibilityOptionsScreen(mc.screen, mc.options));
                    break;

            }
        }

        if(action.equals("접속")) {
            String IP = value;
            ServerData serverData = new ServerData(IP, "", ServerData.Type.OTHER);
            System.out.println("접속!" + IP);
            ConnectScreen.startConnecting(mc.screen, mc, ServerAddress.parseString(IP), serverData, false, null);
            return true;
        }

        if(action.equals("명령어")){
            if (mc.player != null) {
                for(String command : value.split(" "))
                    if(command.equals("@c"))
                        value = value.replace("@c", mc.player.getDisplayName().getString());
                mc.player.connection.sendCommand(value);
            }
        }

        if(action.contains("종료")){
            if(action.contains("게임"))
                mc.stop();
            if(action.contains("화면"))
                mc.setScreen( null);
        }

        if(action.contains("배경 변경:")){
            screenFlow.setBackground(value);
        }

        if(action.contains("버튼 표시")) {
            screenFlow.getWidget().findButtonByString(value).setVisible(true);
        }

        if(action.contains("버튼 숨기기")) {
            screenFlow.getWidget().findButtonByString(value).setVisible(false);
        }

        if(action.contains("이미지 표시")) {
            screenFlow.getWidget().findImageByString(value).setVisible(true);
        }

        if(action.contains("이미지 숨기기")) {
            screenFlow.getWidget().findImageByString(value).setVisible(false);
        }
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "WidgetWrapper{" +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", texture='" + texture + '\'' +
                ", message='" + message + '\'' +
                ", alpha=" + alpha +
                ", visible=" + visible +
                ", lock=" + lock +
                ", action='" + action + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public FakeTextureWidget createFakeWidget(int x, int y, int width, int height, String resource){
        return null;
    }


    public void setValue(String text) {
        this.value = text;
    }
}
