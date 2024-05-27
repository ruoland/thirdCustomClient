package com.example.wrapper;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

/**
 * 마인크래프트 버튼에 대한 데이터를 저장하고 연동하기 위해 Wrapper로 감싸두었음
 */
public class WidgetButtonWrapper extends CustomWidgetWrapper {

        public WidgetButtonWrapper(AbstractWidget widget){
            super(widget);

            dataUpdate();
        }

        public void dataUpdate(){
            setX(getWidget().getX());
            setY(getWidget().getY());
            setWidth(getWidget().getWidth());
            setHeight(getWidget().getHeight());
            setVisible(getWidget().visible);
            setMessage(getWidget().getMessage().getString());
        }

        /**
         * 위젯에 불러온 정보를 부여함
         */

        public void loadToMCWidget(){
            getWidget().setX(getX());
            getWidget().setY(getY());
            getWidget().setWidth(getWidth());
            getWidget().setHeight(getHeight());
            getWidget().visible = isVisible();
            getWidget().setMessage(Component.literal(getMessage()));
        }


    }