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
        protected JComboBox<String> actionComboBox;

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
                actionComboBox = new JComboBox<String>();
                this.actionField.addKeyListener(this);
                actionComboBox.addActionListener(this);
                this.actionField.getDocument().addDocumentListener(this);

                add(actionComboBox);
                add(this.actionField);
                comboBoxAddItem();
            }

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
            actionComboBox.addItem("종료:");

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

            if(actionComboBox != null){
                actionComboBox.setSelectedItem(widgetWrapper.getAction()+":"+widgetWrapper.getValue());

            }
            if(actionField != null){
                actionField.setText(widgetWrapper.getValue());
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == visibleButton) {
                widgetWrapper.setVisible(!widgetWrapper.isVisible());
                visibleButton.setText(widgetWrapper.isVisible() ? visibleText + ": 켜짐" : visibleText+": 꺼짐");
            }
            if(e.getSource() == actionComboBox){
                widgetWrapper.setAction((String) actionComboBox.getSelectedItem());
            }

        }
        @Override
        public void dispose() {
            super.dispose();

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

            if(actionComboBox != null){
                widgetWrapper.setAction((String) actionComboBox.getSelectedItem());
            }
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