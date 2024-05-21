package customclient.swing;



import net.minecraft.client.Minecraft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SwingBackground extends JFrame implements ActionListener {
    public JTextField youtubeField = new JTextField(40);

    JButton gradientButton = new JButton("흐리게");
    JButton fileSelectButton = new JButton("배경 파일 선택");
    JButton splashButton = new JButton("스플래시: 켜짐");
    JButton imageButton = new JButton("이미지 추가하기");
    JButton updateButton = new JButton("업데이트");
    public SwingBackground() {

        setTitle("배경화면 설정");
        setSize(300, 200);
        setLocation(Minecraft.getInstance().getWindow().getX() - 300, Minecraft.getInstance().getWindow().getY());
        setLayout(new FlowLayout(FlowLayout.LEADING));
        fileSelectButton.addActionListener(this);
        imageButton.addActionListener(this);
        updateButton.addActionListener(this);
        add(youtubeField);
        add(fileSelectButton);
        add(imageButton);
        //add(updateButton);

        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}