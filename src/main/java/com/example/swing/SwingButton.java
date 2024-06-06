package com.example.swing;


import com.example.swing.base.SwingWidgetBase;
import com.example.wrapper.widget.WidgetWrapper;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SwingButton extends SwingWidgetBase implements ActionListener, KeyListener {

    public SwingButton(WidgetWrapper widgetWrapper, String title) {
        super(widgetWrapper, title, true, true, true, true, true);
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
