package com.example.wrapper.widget;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.resources.language.I18n;

/**
 * 마인크래프트 버튼에 대한 데이터를 저장하고 연동하기 위해 Wrapper로 감싸두었음
 */
public class ButtonWrapper extends WidgetWrapper {

        public ButtonWrapper(AbstractWidget widget){
            super(widget);
            dataUpdate();
        }

    @Override
    public void setMessage(String message) {
        super.setMessage(message);
        if(message.startsWith("menu.") || message.startsWith("fml."))
            super.setMessage(I18n.get(message));
    }

    public void dataUpdate(){
            setX(getWidget().getX());
            setY(getWidget().getY());
            setWidth(getWidget().getWidth());
            setHeight(getWidget().getHeight());
            setVisible(getWidget().visible);
            setMessage(getWidget().getMessage().getString());

        }
}