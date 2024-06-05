package com.example.swing;

import com.example.CustomScreenMod;
import com.example.ICustomBackground;
import com.example.ScreenCustom;
import com.example.swing.base.ICustomSwing;
import com.example.swing.base.SwingCustomGuiBase;
import com.example.wrapper.CustomWidgetWrapper;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SwingNewTitle extends SwingCustomGuiBase implements ItemListener {

    JToggleButton logoToggle = new JToggleButton("로고: ");
    public SwingNewTitle(){
        super();
        Minecraft mc = Minecraft.getInstance();
        setTitle("메인메뉴");
        logoToggle.addItemListener(this);
        this.add(logoToggle);
        setVisible(true);

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getItem() == logoToggle){
            if(logoToggle.isSelected()) {
                logoToggle.setText("로고 보이게 하기");
                CustomScreenMod.setLogoVisible(false);
            }
            else {
                logoToggle.setText("로고 사라지게 하기");
                CustomScreenMod.setLogoVisible(true);
            }
        }
    }
}
