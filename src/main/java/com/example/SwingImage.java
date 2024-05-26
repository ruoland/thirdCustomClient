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

public class SwingImage extends JFrame implements ActionListener, KeyListener {
    private JTextField nameField = new JTextField(20);
    private JTextField xField = new JTextField(4);
    private JTextField yField = new JTextField(4);
    private JTextField widthField = new JTextField(4);
    private JTextField heightField = new JTextField(4);

    protected GuiData.WidgetImage widgetImage;

    SwingImage(GuiData.WidgetImage widgetImage){
        setTitle("이미지 설정");
        setSize(500, 200);
        Window window = Minecraft.getInstance().getWindow();
        setLocation(window.getX() - 400, window.getY());
        setLayout(new FlowLayout(FlowLayout.LEADING));

        this.widgetImage = widgetImage;
        nameField.addKeyListener(this);
        nameField.setText(widgetImage.texture);

        xField.addKeyListener(this);
        xField.setText(widgetImage.x+"");
        yField.addKeyListener(this);
        yField.setText(widgetImage.y+"");
        widthField.addKeyListener(this);
        widthField.setText(widgetImage.width+"");
        heightField.addKeyListener(this);
        heightField.setText(widgetImage.height+"");

        add(xField);
        add(yField);
        add(widthField);
        add(heightField);
        add(nameField);

        setFocusableWindowState(false);
        setVisible(true);
        setFocusableWindowState(true);
    }



    @Override
    public void actionPerformed(ActionEvent e) {

    }
    @Override
    public void dispose() {
        super.dispose();

    }

    @Override
    public void update(Graphics g) {
        super.update(g);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getSource() == nameField){
            widgetImage.setTexture(nameField.getText());

            return;
        }
        if(e.getKeyChar() != KeyEvent.CHAR_UNDEFINED && checkNumber(e.getKeyChar())) {
            if (e.getSource() == xField) {
                widgetImage.setX(Integer.parseInt(xField.getText()));
            }
            if (e.getSource() == yField) {
                widgetImage.setY(Integer.parseInt(yField.getText()));
            }
            if (e.getSource() == widthField) {
                widgetImage.setWidth(Integer.parseInt(widthField.getText()));
            }
            if (e.getSource() == heightField) {
                widgetImage.setHeight(Integer.parseInt(heightField.getText()));
            }
        }else{
            e.consume();
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }

    public boolean checkNumber(char key){
        if(!Character.isDigit(key)){
            JOptionPane.showMessageDialog(this, "숫자만 입력해주세요.");
            return false;
        }
        else
            return true;
    }
}
