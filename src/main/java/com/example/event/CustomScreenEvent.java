package com.example.event;

import com.example.CustomScreenMod;
import com.example.ScreenCustom;
import com.example.ScreenFlow;
import com.example.gui.event.FilesDropEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class CustomScreenEvent {

    @SubscribeEvent
    public void screenRender(ScreenEvent.Render.Post render){
        Minecraft mc = Minecraft.getInstance();
        if(CustomScreenMod.hasScreen(render.getScreen())) {
            GuiGraphics pGuiGraphics = render.getGuiGraphics();
            if (CustomScreenMod.isEditMode()) {
                pGuiGraphics.drawString(mc.font, Component.literal("편집 모드 실행 중"), 0, 0, 0xFFFFFF, false);
            }
            CustomScreenMod.getScreen(render.getScreen()).getScreenWidgets().renderImage(pGuiGraphics);
        }
    }

}
