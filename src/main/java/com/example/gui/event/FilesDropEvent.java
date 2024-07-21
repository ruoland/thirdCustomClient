package com.example.gui.event;

import net.minecraft.client.gui.screens.Screen;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

import java.nio.file.Path;

public class FilesDropEvent extends ScreenEvent implements ICancellableEvent {
    private Path pPacks;
    public FilesDropEvent(Screen screen, Path pPacks) {
        super(screen);
        this.pPacks = pPacks;
    }

    public Path getFile(){
        return pPacks;
    }
}
