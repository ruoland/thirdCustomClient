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

public class SwingButton extends JFrame implements ActionListener, KeyListener {
    private JTextField nameField = new JTextField(20);
    private JTextField actionField = new JTextField(20);
    private JTextField xField = new JTextField(4);
    private JTextField yField = new JTextField(4);
    private JTextField widthField = new JTextField(4);
    private JTextField heightField = new JTextField(4);
    private JButton visibleButton = new JButton("버튼 표시: 켜짐");
    private JComboBox<String> actionComboBox = new JComboBox<String>();
    protected GuiData.WidgetData guiButton;

    SwingButton(GuiData.WidgetData guiButton){
        setTitle("버튼 설정");
        setSize(500, 200);
        Window window = Minecraft.getInstance().getWindow();
        setLocation(window.getX() - 400, window.getY());
        setLayout(new FlowLayout(FlowLayout.LEADING));
        comboBoxAddItem();
        this.guiButton = guiButton;
        nameField.addKeyListener(this);
        nameField.setText(guiButton.abstractWidget.getMessage().getString());

        xField.addKeyListener(this);
        xField.setText(guiButton.x+"");
        yField.addKeyListener(this);
        yField.setText(guiButton.y+"");
        widthField.addKeyListener(this);
        widthField.setText(guiButton.width+"");
        heightField.addKeyListener(this);
        heightField.setText(guiButton.height+"");


        visibleButton.addActionListener(this);
        actionComboBox.addActionListener(this);
        actionField.addKeyListener(this);
        add(xField);
        add(yField);
        add(widthField);
        add(heightField);
        add(nameField);
        add(visibleButton);
        add(actionComboBox);
        add(actionField);
        setFocusableWindowState(false);
        setVisible(true);
        setFocusableWindowState(true);
    }

    public void comboBoxAddItem() {
        actionComboBox.addItem("선택안함");
        actionComboBox.addItem("열기:맵 선택");
        actionComboBox.addItem("열기:멀티");
        actionComboBox.addItem("열기:설정");
        actionComboBox.addItem("열기:모드");
        actionComboBox.addItem("열기:렐름");
        actionComboBox.addItem("접속:");
        actionComboBox.addItem("종료");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == visibleButton) {
            guiButton.setVisible(!guiButton.isVisible());
            visibleButton.setText(guiButton.isVisible() ? "버튼 표시: 켜짐" : "버튼 표시: 꺼짐");
        }

    }
    @Override
    public void dispose() {
        super.dispose();
        StringBuffer comboBox = new StringBuffer((String) actionComboBox.getSelectedItem());

        if (comboBox.toString().equals("접속:")) {
            comboBox.append(actionField.getText());
        }

        guiButton.setAction(comboBox.toString());
    }

    @Override
    public void update(Graphics g) {
        super.update(g);
    }

    @Override
    public void keyTyped(KeyEvent e) {

        if(e.getSource() == nameField){
            guiButton.setMessage(nameField.getText());
            guiButton.setAction(actionField.getText());
            return;
        }
        if(e.getKeyChar() != KeyEvent.CHAR_UNDEFINED && checkNumber(e.getKeyChar())) {
            if (e.getSource() == xField) {
                guiButton.setX(Integer.parseInt(xField.getText()));
            }
            if (e.getSource() == yField) {
                guiButton.setY(Integer.parseInt(yField.getText()));
            }
            if (e.getSource() == widthField) {
                guiButton.setWidth(Integer.parseInt(widthField.getText()));
            }
            if (e.getSource() == heightField) {
                guiButton.setHeight(Integer.parseInt(heightField.getText()));
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
