package com.example.wrapper;

import net.minecraft.client.gui.components.AbstractWidget;
import org.jetbrains.annotations.Nullable;

public class SelectWidgetHandler {
    private AbstractWidget selectWidget, lastSelectWidget;
    public void setPosition(int x, int y){
        selectWidget.setPosition(x,y);
    }

    public void setSize(int width, int height){
        selectWidget.setSize(width, height);
    }

    public void selectWidget(@Nullable CustomWidgetWrapper customWidgetWrapper) {
        if(this.selectWidget == customWidgetWrapper.getWidget())
            return;
        this.selectWidget = customWidgetWrapper.getWidget();
        lastSelectWidget = customWidgetWrapper.getWidget();
    }

    public void addSelectWidth(int i){
        lastSelectWidget.setWidth(lastSelectWidget.getWidth() + i);
    }

    public void addSelectHeight(int i){
        lastSelectWidget.setHeight(lastSelectWidget.getHeight() + i);
    }

    dra
}
