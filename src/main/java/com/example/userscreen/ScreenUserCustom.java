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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;

public class ScreenUserCustom extends ScreenCustom implements ICustomRenderable {
    private static final Logger log = LoggerFactory.getLogger(ScreenUserCustom.class);
    private ScreenFlow screenFlow;
    public ScreenUserCustom(Component pTitle) {
        super(pTitle);

    }

    @Override
    protected void init() {
        super.init();
        String screenName = getTitle().getString();
        if(CustomScreenMod.hasScreen(screenName)){
            screenFlow = CustomScreenMod.getScreen(screenName);
        }
        else{
            screenFlow = CustomScreenMod.createScreenFlow(getTitle().getString());
        }
        screenFlow.reset(true);
        screenFlow.openScreen(this);
        screenFlow.loadScreenData();
        log.debug(screenFlow.getScreenName());
    }

    @Override
    public boolean isPauseScreen() {

        return false;

    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        log.debug(screenFlow.getScreenName());
    }

    @Override
    public <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T pWidget) {
        return super.addRenderableWidget(pWidget);
    }
}
