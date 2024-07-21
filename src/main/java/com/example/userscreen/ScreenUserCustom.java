package com.example.userscreen;

import com.example.ICustomRenderable;
import com.example.screen.CustomScreenMod;
import com.example.screen.ScreenAPI;
import com.example.screen.ScreenFlow;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;
import java.util.List;

public class ScreenUserCustom extends ScreenCustom implements ICustomRenderable {
    public ScreenUserCustom(Component pTitle) {
        super(pTitle);

    }

    @Override
    protected void init() {
        super.init();
        if(!CustomScreenMod.hasScreen(getTitle().getString())) {
            ScreenFlow screenFlow = CustomScreenMod.createScreenFlow(getTitle().getString());
            screenFlow.openScreen(this);
            screenFlow.loadScreenData();
        }
        else {
            ScreenFlow screenFlow =CustomScreenMod.getScreen(getTitle().getString());
            screenFlow.openScreen(this);
            screenFlow.loadScreenData();

        }
    }



    @Override
    public <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T pWidget) {
        return super.addRenderableWidget(pWidget);
    }
}
