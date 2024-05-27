package com.example.swing;



import com.example.wrapper.CustomWidgetWrapper;
import com.example.ScreenAPI;
import com.example.screen.ScreenUserCustom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingCustomGUI extends JFrame implements ActionListener {
    private ScreenUserCustom screenUserCustom;
    private JButton fileSelectButton = new JButton("배경 파일 선택");
    private JButton addImageButton = new JButton("이미지 추가하기");
    private JButton mcButtonButton = new JButton("버튼 추가하기");
    private JButton addTextField = new JButton("텍스트필드 추가하기");

    public SwingCustomGUI(ScreenUserCustom userCustom) {
        this.screenUserCustom = userCustom;
        setTitle("유저 스크린 설정");
        setSize(300, 200);
        setLocation(Minecraft.getInstance().getWindow().getX() - 300, Minecraft.getInstance().getWindow().getY());
        setLayout(new FlowLayout(FlowLayout.LEADING));

        widgetUpdate();
    }
    public void widgetUpdate() {


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
        if(e.getSource() == mcButtonButton){
            ScreenAPI.addButton(new CustomWidgetWrapper.WidgetButtonWrapper(new Button.Builder(Component.literal("ㅌ테스트"), new Button.OnPress() {
                @Override
                public void onPress(Button pButton) {

                }
            }).size(40,20)
                    .pos(0,0).build()));

        }
        if(e.getSource() == addTextField){
            ScreenAPI.addTextfield(new GuiData.WidgetData(new EditBox(Minecraft.getInstance().font, 200, 20, Component.literal("기본값"))));
        }
    }

}