package com.example.swing;

import com.example.screen.CustomScreenMod;
import com.example.swing.base.SwingWidgetBase;
import net.minecraft.client.Minecraft;

import javax.swing.*;
import java.awt.event.*;

public class SwingNewTitle extends SwingWidgetBase implements ItemListener {

    JToggleButton logoToggle = new JToggleButton("로고: ");
    JToggleButton splashToggle = new JToggleButton("스플래시: ");
    public SwingNewTitle(){
        super(null, "메인메뉴", false, false, false, false, false);
        Minecraft mc = Minecraft.getInstance();
        setTitle("메인메뉴");
        logoToggle.addItemListener(this);
        splashToggle.addItemListener(this);
        this.add(logoToggle);
        this.add(splashToggle);
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
        if(e.getItem() == splashToggle){
            if(splashToggle.isSelected()) {
                splashToggle.setText("스플래시 보이게 하기");
                CustomScreenMod.setLogoVisible(false);
            }
            else {
                splashToggle.setText("스플래시 사라지게 하기");
                CustomScreenMod.setLogoVisible(true);
            }
        }
    }
}
