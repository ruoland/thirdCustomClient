package com.example.swing;


import com.example.swing.base.ICustomSwing;
import com.example.swing.base.SwingWidgetBase;
import com.example.wrapper.widget.ImageWrapper;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class SwingImage extends SwingWidgetBase implements ICustomSwing {
    private JTextField nameField = new JTextField(20);

    public ImageWrapper widgetImage;

    public SwingImage(ImageWrapper widgetImage, String title) {
        super(widgetImage, title, true, true, true, true, true, false);
        this.widgetImage = widgetImage;
        visibleText = "이미지 표시";
    }
    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
    }
    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
    }

}
