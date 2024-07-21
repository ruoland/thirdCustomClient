package com.example.screen;

import com.example.wrapper.widget.WidgetWrapper;
import org.jetbrains.annotations.Nullable;

public class SelectHandler {
    private WidgetWrapper selectWidget;
    private WidgetWrapper lastSelectWidget;
    private int dragX = -1;
    private int dragY = -1;

    public SelectHandler(WidgetWrapper widgetWrapper){
        this.selectWidget = widgetWrapper;
        this.lastSelectWidget = widgetWrapper;
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
            setDragY(y - getWidget().getY()
            );
        }
        selectWidget.setPosition(x - dragX, y - dragY);
    }

    public void setSize(int width, int height){
        selectWidget.setSize(width, height);
    }

    public void setWidget(@Nullable WidgetWrapper widgetWrapper) {
        if(widgetWrapper == null || this.selectWidget == widgetWrapper) {
            if(widgetWrapper == null)
                selectWidget = null;
            return;
        }
        this.selectWidget = widgetWrapper;
        lastSelectWidget = widgetWrapper;
    }

    public void addWidth(int i){
        lastSelectWidget.setWidth(lastSelectWidget.getWidth() + i);
    }

    public void addHeight(int i){
        lastSelectWidget.setHeight(lastSelectWidget.getHeight() + i);
    }

    public void visible(boolean visible){
        lastSelectWidget.setVisible(visible);

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
                "selectWidget=" + selectWidget +
                ", lastSelectWidget=" + lastSelectWidget +
                '}';
    }
}
