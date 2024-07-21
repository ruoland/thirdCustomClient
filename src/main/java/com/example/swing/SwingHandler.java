package com.example.swing;

import com.example.swing.base.EnumSwing;
import com.example.swing.base.ICustomSwing;
import com.example.swing.base.SwingWidgetBase;
import com.example.wrapper.widget.ImageWrapper;
import com.example.wrapper.widget.WidgetWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwingHandler {
    private SwingButton swingButton;
    private SwingImage swingImage;
    private static final Logger logger = LoggerFactory.getLogger(SwingHandler.class);

    private ICustomSwing selectSwing;

    public void updateSwingData(){
        SwingWidgetBase swingWidgetBase = (SwingWidgetBase) selectSwing;
        //만약 윈도우 포커스가 마인크래프트에 맞춰져 있다면 스윙창 업데이트
        if(!swingWidgetBase.isFocused())
            selectSwing.update();
    }

    public void openSwing( WidgetWrapper widgetWrapper){
        logger.debug("위젯용 Swing 창 열기: {}", widgetWrapper);
        EnumSwing enumSwing = EnumSwing.check(widgetWrapper);
        if(selectSwing != null) {
            swingClose();
            logger.debug("위젯용 Swing 창 닫힘: {}", widgetWrapper);
        }

        switch (enumSwing) {
            case IMAGE -> {
                selectSwing = build(EnumSwing.IMAGE, widgetWrapper, "이미지 위젯");
                selectSwing.init();
                return;
            }
            case BUTTON -> {
                selectSwing = build(EnumSwing.BUTTON, widgetWrapper, "버튼 위젯");
                selectSwing.init();
                return;
            }
            default -> {
                if (selectSwing != null) {
                    selectSwing.dispose();
                    selectSwing = null;
                    System.out.println("스윙 닫힘" + selectSwing);
                }
            }
        }
        System.out.println("스윙 열림" + selectSwing);
    }

    public boolean isSwingOpen(){
        return selectSwing != null || swingButton != null || swingImage != null;
    }

    public SwingWidgetBase build(EnumSwing swing, WidgetWrapper wrapperWidget, String title){
        switch(swing){
            case IMAGE -> {
                return new SwingImage((ImageWrapper) wrapperWidget, title);
            }
            case TITLE -> {
                return new SwingNewTitle();
            }
            case BUTTON -> {
                return new SwingButton(wrapperWidget, title);
            }
        }
        throw new NullPointerException("어떤 위젯을 만들지 알 수 없음");
    }
    public void swingClose(){
        if(swingImage != null)
            swingImage.dispose();
        if(swingButton != null)
            swingButton.dispose();
        if(selectSwing != null)
            selectSwing.dispose();
        selectSwing = null;
        swingImage = null;
        swingButton = null;
    }
}
