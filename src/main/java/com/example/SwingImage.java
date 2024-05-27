package com.example;


import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SwingImage extends SwingCustom {
    private JTextField nameField = new JTextField(20);

    protected GuiData.WidgetImage widgetImage;

    SwingImage(GuiData.WidgetImage widgetImage){
        super(widgetImage, "이미지 설정");

        this.widgetImage = widgetImage;
        nameField.setText(widgetImage.texture);

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
