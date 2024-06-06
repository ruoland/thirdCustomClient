package com.example.swing;



import com.example.swing.base.SwingUserGuiBase;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.awt.event.ActionListener;

public class SwingUserGui extends SwingUserGuiBase implements ActionListener {

    public SwingUserGui() {
        super();
        setTitle("유저 스크린 설정");
        setSize(300, 200);
        setLocation(Minecraft.getInstance().getWindow().getX() - 300, Minecraft.getInstance().getWindow().getY());
        setLayout(new FlowLayout(FlowLayout.LEADING));

        widgetUpdate();
    }


}