package com.example.swing;


import com.example.swing.base.ICustomSwing;
import com.example.swing.base.SwingWidgetBase;
import com.example.wrapper.WidgetImageWrapper;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class SwingImage extends SwingWidgetBase implements ICustomSwing {
    private JTextField nameField = new JTextField(20);

    public WidgetImageWrapper widgetImage;

    public SwingImage(WidgetImageWrapper widgetImage){
        super(widgetImage, "이미지 설정");

        this.widgetImage = widgetImage;
        nameField.setText(widgetImage.getResource().toString());

        setFocusableWindowState(false);
        setVisible(true);
        setFocusableWindowState(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        super.keyTyped(e);
        if(e.getSource() == nameField){
            widgetImage.setTexture(nameField.getText());

        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }

}
