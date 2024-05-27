package com.example.wrapper;

import com.example.ScreenNewTitle;
import customclient.FakeTextureWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class CustomWidgetWrapper {
    private transient AbstractWidget abstractWidget;
    private int x, y, width, height, color;
    private String texture, message;
    private float alpha = 1;
    private boolean visible = true, lock = false;
    private String action;

    public CustomWidgetWrapper() {
    }

    CustomWidgetWrapper(AbstractWidget widget) {
        this.abstractWidget = widget;
        abstractWidget.active =true;
        update();
    }

    public int getColor() {
        return abstractWidget.getFGColor();
    }

    public int getHeight() {
        return abstractWidget.getHeight();
    }

    public int getWidth() {
        return abstractWidget.getWidth();
    }

    public boolean isVisible() {
        return abstractWidget.visible;
    }

    public void setVisible(boolean visible) {

        abstractWidget.visible = visible;
        this.visible = visible;
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
        return abstractWidget.getX();
    }

    public int getY() {
        return abstractWidget.getY();
    }

    public float getAlpha() {
        return alpha;
    }

    public void update(){
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

    public AbstractWidget getAbstractWidget() {
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
        return "CustomWidgetWrapper{" +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", color=" + color +
                ", texture='" + texture + '\'' +
                ", alpha=" + alpha +
                ", visible=" + visible +
                ", lock=" + lock +
                '}';
    }

    public static class WidgetImageWrapper extends CustomWidgetWrapper {
        private transient ResourceLocation resourceLocation;
        private String resource;
        private boolean isVisible = true;

        WidgetImageWrapper(ResourceLocation resourceLocation, String fileName, int x, int y, int width, int height, float alpha){
            setX(x);
            setY(y);
            setWidth(width);
            setHeight(height);
            this.resource = fileName;
            this.resourceLocation = resourceLocation;
            setAlpha(alpha);
            createFakeWidget();
        }

        public ResourceLocation getResource() {
            return resourceLocation == null ? resourceLocation = new ResourceLocation("customclient", resource) : resourceLocation;
        }

        protected void render(GuiGraphics pGuiGraphics) {
            if(isVisible)
                ScreenNewTitle.renderTexture(pGuiGraphics, getResource(), getX(), getY(), getWidth(), getHeight(), getAlpha());
        }

        public FakeTextureWidget createFakeWidget(){
            if(getAbstractWidget() == null) {
                FakeTextureWidget fakeTextureWidget = new FakeTextureWidget(getX(), getY(), getWidth(), getHeight(), Component.literal(resource));
                setAbstractWidget(fakeTextureWidget);
            }
            return (FakeTextureWidget) getAbstractWidget();

        }

    }
    public static class WidgetButtonWrapper extends CustomWidgetWrapper {
        protected boolean isTextField = false;

        public WidgetButtonWrapper(AbstractWidget widget){
            super(widget);

            dataUpdate();
        }

        public void dataUpdate(){
            x = abstractWidget.getX();
            y = abstractWidget.getY();
            width = abstractWidget.getWidth();
            height = abstractWidget.getHeight();
            visible = abstractWidget.visible;
            message = abstractWidget.getMessage().getString();
        }

        /**
         * 위젯에 불러온 정보를 부여함
         */

        public void loadToMCWidget(){
            abstractWidget.setX(x);
            abstractWidget.setY(y);
            abstractWidget.setWidth(width);
            abstractWidget.setHeight(height);
            abstractWidget.visible = visible;
            abstractWidget.setMessage(Component.literal(message));
        }


        public boolean isTextField() {
            return isTextField;
        }
    }
}
