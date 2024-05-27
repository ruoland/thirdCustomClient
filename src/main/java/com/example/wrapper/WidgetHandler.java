package com.example.wrapper;

import com.example.swing.SwingButton;
import com.example.swing.SwingCustom;
import com.example.swing.SwingImage;
import com.example.wrapper.CustomWidgetWrapper;
import net.minecraft.client.gui.components.AbstractWidget;
import org.jetbrains.annotations.Nullable;

public class WidgetHandler {
    private AbstractWidget selectWidget, lastSelectWidget;
    private SwingCustom selectSwing;
    public void setPosition(int x, int y){
        selectWidget.setPosition(x,y);
        updateSwingData();
    }

    public void setSize(int width, int height){
        selectWidget.setSize(width, height);
        updateSwingData();
    }
    public void updateSwingData(){
        selectSwing.update();
    }

    public void selectWidget(@Nullable CustomWidgetWrapper customWidgetWrapper) {
        if(this.selectWidget == customWidgetWrapper.abstractWidget)
            return;

        this.selectWidget = customWidgetWrapper.abstractWidget;
        lastSelectWidget = customWidgetWrapper.abstractWidget;
        openSwing(customWidgetWrapper);

    }

    public void openSwing(CustomWidgetWrapper customWidgetWrapper){
        if(selectSwing != null)
            selectSwing.dispose();
        if(customWidgetWrapper instanceof CustomWidgetWrapper.WidgetImageWrapper widgetImage ) {
            selectSwing = new SwingImage(widgetImage);
        }
        if(customWidgetWrapper instanceof CustomWidgetWrapper.WidgetButtonWrapper widgetButton){
            selectSwing = new SwingButton(widgetButton);
        }
        updateSwingData();
    }

}

