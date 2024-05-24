package com.example;


import net.minecraft.client.Minecraft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SwingButton extends JFrame implements ActionListener, KeyListener {
    private JTextField nameField = new JTextField();
    private JTextField actionField = new JTextField();
    private JButton visibleButton = new JButton("버튼 표시: 켜짐");


    private JComboBox<String> actionComboBox = new JComboBox<String>();

    private GuiData.WidgetData guiButton;
    SwingButton(GuiData.WidgetData guiButton){
        setTitle("버튼 설정");
        setSize(300, 200);
        setLocation(Minecraft.getInstance().getWindow().getX() - 600, Minecraft.getInstance().getWindow().getY());
        setLayout(new FlowLayout(FlowLayout.LEADING));
        comboBoxAddItem();
        this.guiButton = guiButton;
        nameField.addKeyListener(this);
        visibleButton.addActionListener(this);
        actionComboBox.addActionListener(this);
        actionField.addKeyListener(this);
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
            visibleButton.setText(guiButton.isVisible() ? "버튼 표시: 켜짐" : "버튼 표시: 꺼짐(꺼져도 클릭은 가능합니다)");
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
    public void keyTyped(KeyEvent e) {
        if(e.getSource() == nameField){
            guiButton.setMessage(nameField.getText());
        }
        if(e.getSource() == actionField){
            guiButton.setAction(actionField.getText());
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
