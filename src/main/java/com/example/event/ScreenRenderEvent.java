package com.example.event;

import com.example.screen.CustomScreenMod;
import com.example.screen.ScreenFlow;
import com.example.wrapper.widget.ImageWrapper;
import customclient.HUDWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScreenRenderEvent {
    private static final Logger log = LoggerFactory.getLogger(ScreenRenderEvent.class);
    ScreenFlow screenFlow;
    @SubscribeEvent
    public void screenUI(RenderGuiEvent.Post event){
        if(!HUDWidget.isEmpty()) {
            for (ImageWrapper wrapper : HUDWidget.getImageList()) {
                wrapper.render(event.getGuiGraphics());

            }
        }
    }

    @SubscribeEvent
    public void tick(ServerTickEvent.Post event){
        HUDWidget.cooldown();
    }

    @SubscribeEvent
    public void screenRender(ScreenEvent.Render.Post render){
        Minecraft mc = Minecraft.getInstance();
        if(CustomScreenMod.hasScreen(render.getScreen())) {
            GuiGraphics pGuiGraphics = render.getGuiGraphics();
            if (CustomScreenMod.isEditMode()) {
                pGuiGraphics.drawString(mc.font, Component.literal("편집 모드 실행 중"), 0, 0, 0xFFFFFF, false);
            }
            if(screenFlow == null)
                screenFlow = CustomScreenMod.getScreen(render.getScreen());
            else if(screenFlow.getScreen() != render.getScreen()){
                screenFlow = CustomScreenMod.getScreen(render.getScreen());
            }
            if(!screenFlow.getWidget().getImageList().isEmpty()) {
                //log.debug("{}의 이미지에서 렌더링 중.",screenFlow.getScreenName());
                for (ImageWrapper imageWrapper : screenFlow.getWidget().getImageList()) {
                    if(imageWrapper.isVisible()) {
                        imageWrapper.render(pGuiGraphics);
                    }
                }
            }

        }
    }

}
