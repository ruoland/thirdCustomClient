package customclient;

import net.minecraft.client.gui.components.AbstractWidget;

import java.io.Serializable;

public class GuiButton implements Serializable {
    transient AbstractWidget abstractWidget;
    int buttonID = -1;
    String buttonText = "";
    int x, y, width, height, color;
    private boolean visible = true;
    private String originalName;

    public GuiButton(int id, AbstractWidget widget){
        this.abstractWidget = widget;
        this.buttonID = id;
        this.buttonText = widget.getMessage().getString();
        this.x = widget.getX();
        this.y = widget.getY();
        this.width = widget.getWidth();
        this.height = widget.getHeight();
        this.color = widget.getFGColor();
    }

    public void setButtonID(int buttonID) {
        this.buttonID = buttonID;
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
        this.width = width;
        abstractWidget.setWidth(width);
    }

    public void setHeight(int height) {
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getButtonID() {
        return buttonID;
    }

    public void update(){
        this.setPosition(x, y);
        this.setHeight(height);
        this.setWidth(width);
        this.setVisible(isVisible());
    }
    public boolean isMouseOver(double mouseX, double mouseY ){

        return abstractWidget.isMouseOver(mouseX, mouseY);
    }

    public void setAbstractWidget(AbstractWidget abstractWidget) {
        this.abstractWidget = abstractWidget;
    }
}


