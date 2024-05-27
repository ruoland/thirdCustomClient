package com.example.screen;

import com.example.ICustomRenderable;
import com.example.ScreenAPI;
import com.example.gui.event.FilesDropEvent;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.NeoForge;

import java.nio.file.Path;
import java.util.List;

public class ScreenUserCustom extends ScreenCustom implements ICustomRenderable {
    protected ScreenUserCustom(Component pTitle) {
        super(pTitle);
        ScreenAPI.setGui(this, getTitle().getString());
    }

    @Override
    public void onFilesDrop(List<Path> pPacks) {
        super.onFilesDrop(pPacks);
        if(!NeoForge.EVENT_BUS.post(new FilesDropEvent(this, pPacks.get(0))).isCanceled()) {
            ScreenAPI.fileDrops(this, pPacks.get(0));

        }

    }


    @Override
    public <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T pWidget) {
        return null;
    }
}
