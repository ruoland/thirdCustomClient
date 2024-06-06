package com.example.swing.base;

import com.example.wrapper.widget.WidgetWrapper;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SwingWidgetBase extends JFrame implements ICustomSwing, KeyListener, ActionListener, DocumentListener {
        protected JTextField nameField = new JTextField(20);
        protected JTextField actionField = new JTextField(20);
        protected JTextField xField = new JTextField(4);
        protected JTextField yField = new JTextField(4);
        protected JTextField widthField = new JTextField(4);
        protected JTextField heightField = new JTextField(4);
        protected JButton visibleButton = new JButton("버튼 표시: 켜짐");
        protected String visibleText = "버튼 표시: ";
        protected JComboBox<String> actionComboBox = new JComboBox<String>();

        protected WidgetWrapper widgetWrapper;
        public SwingWidgetBase(WidgetWrapper widgetWrapper, String title, boolean isNameText, boolean actionField, boolean positionField, boolean sizeField, boolean visibleButton) {
            setTitle(title);
            setSize(500, 200);
            Window window = Minecraft.getInstance().getWindow();
            setLocation(window.getX() - 400, window.getY());
            setLayout(new FlowLayout(FlowLayout.LEADING));
            this.widgetWrapper = widgetWrapper;
            if(isNameText) {
                nameField.addKeyListener(this);
                nameField.setText(widgetWrapper.getMessage());
                nameField.getDocument().addDocumentListener(this);
                add(nameField);
            }
            if(positionField) {
                xField.addKeyListener(this);
                xField.setText(widgetWrapper.getX() + "");
                yField.addKeyListener(this);
                yField.setText(widgetWrapper.getY() + "");
                xField.getDocument().addDocumentListener(this);
                yField.getDocument().addDocumentListener(this);
                add(xField);
                add(yField);
            }
            if(sizeField) {
                widthField.addKeyListener(this);
                widthField.setText(widgetWrapper.getWidth() + "");
                heightField.addKeyListener(this);
                heightField.setText(widgetWrapper.getHeight() + "");
                widthField.getDocument().addDocumentListener(this);
                heightField.getDocument().addDocumentListener(this);
                add(widthField);
                add(heightField);
            }
            if(visibleButton) {
                this.visibleButton.addActionListener(this);
                add(this.visibleButton);
            }

            if(actionField) {
                this.actionField.addKeyListener(this);
                actionComboBox.addActionListener(this);
                add(actionComboBox);
                add(this.actionField);
            }

            setFocusableWindowState(false);
            setVisible(true);
            setFocusableWindowState(true);
            comboBoxAddItem();
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
        public void keyTyped(KeyEvent e) {

        }
        
        @Override
        public void keyPressed(KeyEvent e) {

        }


        @Override
        public void keyReleased(KeyEvent e) {

        }

        public void update(){
            xField.setText(""+ widgetWrapper.getX());
            yField.setText(""+ widgetWrapper.getY());
            widthField.setText(""+ widgetWrapper.getWidth());
            heightField.setText(""+ widgetWrapper.getHeight());
        }

        public boolean checkNumber(char key){
            if(!Character.isDigit(key) ){
                System.out.println(key);
                JOptionPane.showMessageDialog(this, key+" 대신 숫자만 입력해주세요.");
                return false;
            }
            else
                return true;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == visibleButton) {
                widgetWrapper.setVisible(!widgetWrapper.isVisible());
                visibleButton.setText(widgetWrapper.isVisible() ? visibleText + ": 켜짐" : visibleText+": 꺼짐");
            }
        }
        @Override
        public void dispose() {
            super.dispose();
            StringBuffer comboBox = new StringBuffer((String) actionComboBox.getSelectedItem());

            if (comboBox.toString().equals("접속:")) {
                comboBox.append(actionField.getText());
            }

            widgetWrapper.setAction(comboBox.toString());
        }

        public void dataUpdate(){
            if(!xField.getText().isEmpty())
            widgetWrapper.setX(Integer.parseInt(xField.getText()));
            if(!yField.getText().isEmpty())
                widgetWrapper.setY(Integer.parseInt(yField.getText()));
            if(!widthField.getText().isEmpty())
                widgetWrapper.setWidth(Integer.parseInt(widthField.getText()));
            if(!heightField.getText().isEmpty())
                widgetWrapper.setHeight(Integer.parseInt(heightField.getText()));
        }
    @Override
    public void insertUpdate(DocumentEvent e) {
        dataUpdate();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        dataUpdate();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        dataUpdate();
    }
}