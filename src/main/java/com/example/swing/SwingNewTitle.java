package com.example.swing;

import com.example.screen.CustomScreenMod;
import com.example.swing.base.SwingWidgetBase;
import net.minecraft.client.Minecraft;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class SwingNewTitle extends SwingWidgetBase implements ItemListener {

    JToggleButton logoToggle = new JToggleButton("로고: ");
    JToggleButton splashToggle = new JToggleButton("스플래시: ");
    public SwingNewTitle(){
        super(null, "메인메뉴", false, false, false, false, false);

        setTitle("메인메뉴");
        logoToggle.addItemListener(this);
        splashToggle.addItemListener(this);
        this.add(logoToggle);
        this.add(splashToggle);
        setVisible(true);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        // 사용 예:
        if (e.getItem() == logoToggle) {
            handleToggle(logoToggle, "로고 보이게 하기", "로고 사라지게 하기", true);
        } else if (e.getItem() == splashToggle) {
            handleToggle(splashToggle, "스플래시 보이게 하기", "스플래시 사라지게 하기", true);
        }

    }
    private void handleToggle(JToggleButton toggle, String showText, String hideText, boolean visibility) {
        if (toggle.isSelected()) {
            toggle.setText(showText);
            CustomScreenMod.setLogoVisible(!visibility);
        } else {
            toggle.setText(hideText);
            CustomScreenMod.setLogoVisible(visibility);}
    }
}
