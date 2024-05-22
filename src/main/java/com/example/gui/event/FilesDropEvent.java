package com.example.gui.event;

import net.minecraft.client.gui.screens.Screen;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

import java.nio.file.Path;
import java.util.List;

public class FilesDropEvent extends ScreenEvent implements ICancellableEvent {
    private List<Path> pPacks;
    public FilesDropEvent(Screen screen, List<Path> pPacks) {
        super(screen);
        this.pPacks = pPacks;
    }

    public List<Path> getFiles(){
        return pPacks;
    }
}
