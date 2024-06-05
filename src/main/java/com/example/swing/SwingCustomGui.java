package com.example.swing;



import com.example.ICustomBackground;
import com.example.swing.base.SwingCustomGuiBase;
import net.minecraft.client.Minecraft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingCustomGui extends SwingCustomGuiBase implements ActionListener {

    public SwingCustomGui() {
        super();
        setTitle("유저 스크린 설정");
        setSize(300, 200);
        setLocation(Minecraft.getInstance().getWindow().getX() - 300, Minecraft.getInstance().getWindow().getY());
        setLayout(new FlowLayout(FlowLayout.LEADING));

        widgetUpdate();
    }


}