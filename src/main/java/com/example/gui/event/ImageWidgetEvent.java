package com.example.gui.event;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.openjdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.nio.file.Path;
import java.util.List;

public abstract class ImageWidgetEvent extends ScreenEvent implements ICancellableEvent {
    protected static final int YES_OPTION= JOptionPane.YES_OPTION, NO_OPTION = JOptionPane.NO_OPTION, CANCEL_OPTION = JOptionPane.CANCEL_OPTION;
    private ResourceLocation resourceLocation;
    private Path filePath;
    public ImageWidgetEvent(Screen screen, ResourceLocation resourceLocation, Path filePath){
        super(screen);

        this.resourceLocation = resourceLocation;
        this.filePath = filePath;
    }

    public String getFileName() {
        return filePath.getFileName().toString();
    }

    public ResourceLocation get(){
        return this.resourceLocation;
    }
    public static class Background extends ImageWidgetEvent{

        public Background(Screen screen,ResourceLocation resourceLocation, Path filePath) {
            super(screen, resourceLocation, filePath);
        }


    }
    public static class Image extends ImageWidgetEvent{

        public Image(Screen screen, ResourceLocation resourceLocation, Path filePath) {
            super(screen, resourceLocation, filePath);
        }
    }
}
