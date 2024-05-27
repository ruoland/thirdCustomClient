package com.example;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public abstract class SwingCustom extends JFrame implements KeyListener, ActionListener {
    protected JTextField nameField = new JTextField(20);
    protected JTextField actionField = new JTextField(20);
    protected JTextField xField = new JTextField(4);
    protected JTextField yField = new JTextField(4);
    protected JTextField widthField = new JTextField(4);
    protected JTextField heightField = new JTextField(4);
    protected JButton visibleButton = new JButton("버튼 표시: 켜짐");
    protected JComboBox<String> actionComboBox = new JComboBox<String>();

    protected NewWidget guiButton;
    SwingCustom(NewWidget guiButton, String title){
        setTitle(title);
        setSize(500, 200);
        Window window = Minecraft.getInstance().getWindow();
        setLocation(window.getX() - 400, window.getY());
        setLayout(new FlowLayout(FlowLayout.LEADING));

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

        actionField.addKeyListener(this);
        actionComboBox.addActionListener(this);
        add(actionComboBox);
        add(xField);
        add(yField);
        add(widthField);
        add(heightField);
        add(nameField);
        add(visibleButton);

        add(actionField);
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
        if (e.getSource() == nameField) {
            guiButton.setMessage(nameField.getText());
            return;
        }
        if(e.getExtendedKeyCode() == KeyEvent.VK_ESCAPE)
        {
            dispose();
            ScreenAPI.setSelectWidget(null);
        }

    }


    @Override
    public void keyReleased(KeyEvent e) {

        if (e.getKeyChar() != KeyEvent.CHAR_UNDEFINED && !e.isActionKey() && (e.getKeyChar() == KeyEvent.VK_BACK_SPACE || checkNumber(e.getKeyChar()))) {
            if (e.getSource() == xField) {
                if(!xField.getText().isEmpty())
                    guiButton.setX(Integer.parseInt(xField.getText()));
            }
            if (e.getSource() == yField) {
                if(!yField.getText().isEmpty())
                    guiButton.setY(Integer.parseInt(yField.getText()));
            }
            if (e.getSource() == widthField) {
                if(!widthField.getText().isEmpty())
                    guiButton.setWidth(Integer.parseInt(widthField.getText()));
            }
            if (e.getSource() == heightField) {
                if(!heightField.getText().isEmpty())
                    guiButton.setHeight(Integer.parseInt(heightField.getText()));
            }

        } else {
            e.consume();
        }
    }

    public void update(){
        xField.setText(""+guiButton.getX());
        yField.setText(""+guiButton.getY());
        widthField.setText(""+guiButton.getWidth());
        heightField.setText(""+guiButton.getHeight());
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
}
