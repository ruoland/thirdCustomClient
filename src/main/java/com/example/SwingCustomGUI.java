package com.example;



import customclient.swing.SwingComponentBase;
import customclient.swing.SwingManager;
import net.minecraft.client.Minecraft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class SwingCustomGUI extends SwingComponentBase implements ActionListener {

    JButton fileSelectButton = new JButton("배경 파일 선택");
    JButton addImageButton = new JButton("이미지 추가하기");
    JButton mcButtonButton = new JButton("버튼 추가하기");
    JButton textfieldButton = new JButton("텍스트 필드 추가하기");

    public SwingCustomGUI(boolean addDefault) {
        super(addDefault);
    }


    @Override
    public void init(SwingManager buttonManager) {
        super.init(buttonManager);
        setTitle("배경화면 설정");
        setSize(300, 200);
        setLocation(Minecraft.getInstance().getWindow().getX() - 300, Minecraft.getInstance().getWindow().getY());
        setLayout(new FlowLayout(FlowLayout.LEADING));
    }

    @Override
    public void widgetUpdate() {
        super.widgetUpdate();
        fileSelectButton.addActionListener(this);
        addImageButton.addActionListener(this);
        mcButtonButton.addActionListener(this);

        add(fileSelectButton);
        add(addImageButton);
        add(mcButtonButton);
        //add(updateButton);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == fileSelectButton){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(this);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}