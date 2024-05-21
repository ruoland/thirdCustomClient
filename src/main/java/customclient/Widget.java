package customclient;

import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;

public abstract class Widget {
    protected int id =0;
    protected transient AbstractWidget abstractWidget;
    protected int x, y, width, height, color;
    protected String texture;
    protected float alpha = 1;
    protected boolean visible = true, lock = false;

    Widget() {
    }

    Widget(AbstractWidget widget) {
        this.abstractWidget = widget;
        abstractWidget.active =true;
    }
    public void setID(int ID) {
        this.id = ID;


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

    public int getID() {
        return id;
    }

    public void update(){
        this.setPosition(x, y);
        this.setHeight(height);
        this.setWidth(width);
        this.setVisible(isVisible());
        this.setAlpha(getAlpha());
    }
    public boolean isMouseOver(double mouseX, double mouseY ){
        return abstractWidget.isMouseOver(mouseX, mouseY);
    }

    public void remove(){
        visible = false;
        abstractWidget.active = false;
        abstractWidget.visible = false;
    }
    public void setAbstractWidget(AbstractWidget abstractWidget) {
        this.abstractWidget = abstractWidget;
    }
}
