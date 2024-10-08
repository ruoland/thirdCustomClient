package com.example.event;

import com.example.gui.event.FilesDropEvent;
import com.example.screen.CustomScreenMod;
import com.example.screen.ScreenFlow;
import com.example.wrapper.widget.ButtonWrapper;
import com.example.wrapper.widget.ImageWrapper;
import com.example.wrapper.widget.StringWrapper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScreenMouseEvent {
    private static final Logger logger = LoggerFactory.getLogger(ScreenMouseEvent.class);

    @SubscribeEvent
    public void editModeButton(ScreenEvent.MouseButtonPressed.Pre event){
        if(CustomScreenMod.isEditMode() && CustomScreenMod.hasScreen(event.getScreen())) {
            event.setCanceled(true);
            boolean isClickButton = CustomScreenMod.getScreen(event.getScreen()).clickWidget(event.getMouseX(), event.getMouseY());
            //클릭했는데 버튼이 없는 경우, 리셋
            if (!isClickButton) {
                logger.debug("클릭된 위젯이 없습니다.");
                CustomScreenMod.getScreen(event.getScreen()).reset(false);
            }

        }
    }
    @SubscribeEvent
    public void customButtonPressed(ScreenEvent.MouseButtonPressed.Pre event) {
        if(!CustomScreenMod.isEditMode() && CustomScreenMod.hasScreen(event.getScreen())) {
            ScreenFlow screenFlow = CustomScreenMod.getScreen(event.getScreen());
            if(!screenFlow.hasSelectWidget()) {
                if (event.getButton() == 0) {
                    for (ButtonWrapper buttonWrapper : screenFlow.getWidget().getButtons()) {
                        if (buttonWrapper.isMouseOver(event.getMouseX(), event.getMouseY())) {
                            logger.info("클릭된 버튼 : {}, 액션 : {}", buttonWrapper.getMessage(), buttonWrapper.getAction());
                            buttonWrapper.runAction();
                            event.setCanceled(true);
                            break;
                        }

                    }
                    for (ImageWrapper imageWrapper : screenFlow.getWidget().getImageList()) {
                        if (imageWrapper.isMouseOver(event.getMouseX(), event.getMouseY())) {
                            logger.info("클릭된 이미지 : {}, 액션 : {}", imageWrapper.getMessage(), imageWrapper.getAction());
                            imageWrapper.runAction();
                            event.setCanceled(true);
                            break;
                        }
                    }
                    for (StringWrapper stringWrapper : screenFlow.getWidget().getStringWrappers()) {
                        if (stringWrapper.isMouseOver(event.getMouseX(), event.getMouseY())) {
                            logger.info("클릭된 텍스트 : {}, 액션 : {}", stringWrapper.getMessage(), stringWrapper.getAction());
                            event.setCanceled(true);
                            break;
                        }
                    }
                }
            }
        }
    }
    /*
     * 파일을 드롭했을 때 이벤트입니다.
     */
    @SubscribeEvent
    public void fileDropEvent(FilesDropEvent event){
        if(CustomScreenMod.hasScreen(event.getScreen()) && CustomScreenMod.isEditMode()) {
            ScreenFlow screenFlow = CustomScreenMod.getScreen(event.getScreen().getTitle().toString());
            screenFlow.fileDropEvent(event.getFile());
        }
    }

    /*
    클릭한 위젯을 마우스 따라 이동합니다
     */
    @SubscribeEvent
    public void screenButton(ScreenEvent.MouseDragged.Post opening){
        if(CustomScreenMod.isEditMode() && CustomScreenMod.hasScreen(opening.getScreen())){

            int mouseX = (int) (opening.getMouseX() + opening.getDragX());
            int mouseY = (int) (opening.getMouseY() + opening.getDragY());
            ScreenFlow screenFlow = CustomScreenMod.getScreen(opening.getScreen());
            if(screenFlow.hasSelectWidget()) {
                screenFlow.getSwingHandler().updateSwingData();
                screenFlow.dragWidget(mouseX, mouseY);
            }
        }
    }

}
