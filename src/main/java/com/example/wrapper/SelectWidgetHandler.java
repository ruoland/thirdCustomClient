package com.example.wrapper;

import net.minecraft.client.gui.components.AbstractWidget;
import org.jetbrains.annotations.Nullable;

public class SelectWidgetHandler {
    private CustomWidgetWrapper selectWidget;
    private CustomWidgetWrapper lastSelectWidget;

    public SelectWidgetHandler(CustomWidgetWrapper widgetWrapper){
        this.selectWidget = widgetWrapper;
    }
    public void setPosition(int x, int y){
        selectWidget.setPosition(x,y);
        System.out.println(selectWidget.getMessage() +"  - "+selectWidget.getX() + " -"+selectWidget.getY());
    }

    public void setSize(int width, int height){
        selectWidget.setSize(width, height);
    }

    public void selectWidget(@Nullable CustomWidgetWrapper customWidgetWrapper) {
        if(customWidgetWrapper == null || this.selectWidget == customWidgetWrapper) {
            if(customWidgetWrapper == null)
                selectWidget = null;
            return;
        }
        this.selectWidget = customWidgetWrapper;
        lastSelectWidget = customWidgetWrapper;
    }

    public void addSelectWidth(int i){
        lastSelectWidget.setWidth(lastSelectWidget.getWidth() + i);
    }

    public void addSelectHeight(int i){
        lastSelectWidget.setHeight(lastSelectWidget.getHeight() + i);
    }

    public String getMessage(){
        return selectWidget.getMessage();
    }

    public CustomWidgetWrapper getSelectWidget() {
        return selectWidget;
    }

    @Override
    public String toString() {
        return "SelectWidgetHandler{" +
                "selectWidget=" + selectWidget +
                ", lastSelectWidget=" + lastSelectWidget +
                '}';
    }
}
