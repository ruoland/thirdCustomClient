package com.example.wrapper.widget;

import customclient.FakeTextureWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public abstract class WidgetWrapper {
    private transient AbstractWidget abstractWidget;
    private int x, y, width, height, color;
    private String texture, message;
    private float alpha = 1;
    private boolean visible = true, lock = false;
    private String action;

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
        System.out.println("업데이트됨"+y);
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
