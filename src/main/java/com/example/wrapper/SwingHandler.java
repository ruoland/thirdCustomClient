package com.example.wrapper;

import com.example.swing.SwingButton;
import com.example.swing.SwingCustom;
import com.example.swing.SwingImage;

public class SwingHandler {
    private SwingButton swingButton;
    private SwingImage swingImage;

    private SwingCustom selectSwing;

    public void updateSwingData(){
        selectSwing.update();
    }

    public void openSwing(CustomWidgetWrapper customWidgetWrapper){
        if(selectSwing != null)
            selectSwing.dispose();
        if(customWidgetWrapper instanceof WidgetImageWrapper widgetImage ) {
            selectSwing = new SwingImage(widgetImage);
            updateSwingData();
        }
        else if(customWidgetWrapper instanceof WidgetButtonWrapper widgetButton){
            selectSwing = new SwingButton(widgetButton);
            updateSwingData();
        }
        else if(customWidgetWrapper == null) {
            if (selectSwing != null) {
                selectSwing.dispose();
                selectSwing = null;
            }
        }

    }
    public boolean isSwingOpen(){
        return swingButton != null || swingImage != null;
    }

    public void swingClose(){
        if(swingImage != null)
            swingImage.dispose();
        if(swingButton != null)
            swingButton.dispose();

        swingImage = null;
        swingButton = null;
    }
}
