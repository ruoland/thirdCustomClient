package com.example.wrapper.widget;

import com.example.screen.CustomScreenMod;
import com.example.screen.ScreenFlow;
import com.mojang.realmsclient.RealmsMainScreen;
import customclient.CustomClient;
import customclient.FakeTextureWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.SafetyScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.ModListScreen;

public abstract class WidgetWrapper {
    private transient AbstractWidget abstractWidget;
    private int x, y, width, height, color;
    private String texture, message;
    private float alpha = 1;
    private boolean visible = true, lock = false;
    private String action, value;

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

    public int getWidth() {
        return abstractWidget == null ? width : abstractWidget.getWidth();
    }

    public boolean isVisible() {
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
    public void setX(int x) {
        this.x = x;
        abstractWidget.setX(x);
    }

    public void setY(int y) {
        this.y = y;
        abstractWidget.setY(y);
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

    public int getX() {
        return abstractWidget == null ? x:  abstractWidget.getX();
    }

    public int getY() {
        return abstractWidget == null ? y:  abstractWidget.getY();
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

    }
    public boolean isMouseOver(double mouseX, double mouseY ){
        if(abstractWidget ==null)
            return false;
        return abstractWidget.isMouseOver(mouseX, mouseY);
    }
    public boolean canSelectByMouse(double mouseX, double mouseY ){
        return !isLock() && abstractWidget.isMouseOver(mouseX, mouseY);
    }

    public void remove(){
        visible = false;
        abstractWidget.active = false;
        abstractWidget.visible = false;
    }

    public void setMessage(String message){
        this.message = message;
        abstractWidget.setMessage(Component.literal(message));
    }
    public void setAction(String action){
        this.action = action;
        if(action.contains(":")) {
            String[] actionValue = action.split(":");
            this.action = actionValue[0];
            this.value = actionValue[1];
            System.out.println(action+ " - "+value+ " - 값 설정됨");
        }
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
            switch (value) {
                case ("맵 선택"):
                    mc.setScreen(new SelectWorldScreen(screenFlow.getScreen()));
                    break;
                case ("멀티"):
                    Screen screen = (Screen) (mc.options.skipMultiplayerWarning ? new JoinMultiplayerScreen(screenFlow.getScreen()) : new SafetyScreen(screenFlow.getScreen()));
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

        if(action.contains("종료"))
            mc.stop();


        return true;
    }
    @Override
    public String toString() {
        return "WidgetWrapper{" +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", color=" + color +
                ", texture='" + texture + '\'' +
                ", message='" + message + '\'' +
                ", alpha=" + alpha +
                ", visible=" + visible +
                ", lock=" + lock +
                ", action='" + action + '\'' +
                '}';
    }

    public FakeTextureWidget createFakeWidget(int x, int y, int width, int height, String resource){
        return null;
    }


}
