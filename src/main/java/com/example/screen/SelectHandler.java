package com.example.screen;

import com.example.wrapper.widget.WidgetWrapper;

public class SelectHandler {
    private final WidgetWrapper selectWidget;
    private int dragX = -1;
    private int dragY = -1;

    public SelectHandler(WidgetWrapper widgetWrapper){
        this.selectWidget = widgetWrapper;
    }

    public boolean isDrag(){
        return dragX != -1 && dragY != -1;
    }
    public void setDragX(int dragX) {
        this.dragX = dragX;
    }

    public void setDragY(int dragY) {
        this.dragY = dragY;
    }

    public void setPosition(int x, int y){
        if(!isDrag()) {
            setDragX(x - getWidget().getX());
            setDragY(y - getWidget().getY());
        }
        selectWidget.setPosition(x - dragX, y - dragY);
    }

    public void addWidth(int i){
        selectWidget.setWidth(selectWidget.getWidth() + i);
    }

    public void addHeight(int i){
        selectWidget.setHeight(selectWidget.getHeight() + i);
    }

    public String getMessage(){
        return selectWidget.getMessage();
    }

    public WidgetWrapper getWidget() {
        return selectWidget;
    }

    @Override
    public String toString() {
        return "SelectHandler{" +
                "selectWidget=" + selectWidget+
                '}';
    }
}
