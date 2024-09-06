package com.example.swing.base;

import com.example.wrapper.widget.ButtonWrapper;
import com.example.wrapper.widget.ImageWrapper;
import com.example.wrapper.widget.StringWrapper;
import com.example.wrapper.widget.WidgetWrapper;

public enum EnumSwing {
    IMAGE,
    BUTTON,
    TITLE,
    STRING,
    CUSTOM_GUI;

    public static EnumSwing check(WidgetWrapper widgetWrapper){
        if(widgetWrapper instanceof ImageWrapper)
            return IMAGE;
        if(widgetWrapper instanceof ButtonWrapper)
            return BUTTON;
        if(widgetWrapper instanceof StringWrapper)
            return STRING;
        return null;
    }
}
