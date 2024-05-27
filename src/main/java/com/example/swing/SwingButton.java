package com.example.swing;


import com.example.wrapper.CustomWidgetWrapper;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SwingButton extends SwingCustom implements ActionListener, KeyListener {
  

    public SwingButton(CustomWidgetWrapper.WidgetButtonWrapper guiButton){
        super(guiButton, "버튼 설정");
    }


    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
