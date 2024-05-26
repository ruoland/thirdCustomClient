package com.example.gui.event;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class ImageWidgetEvent extends ScreenEvent implements ICancellableEvent {
    protected static final int YES_OPTION= JOptionPane.YES_OPTION, NO_OPTION = JOptionPane.NO_OPTION, CANCEL_OPTION = JOptionPane.CANCEL_OPTION;
    private static final Path CUSTOM_CLIENT_PATH = Paths.get("D:\\Projects\\thirdCustomClient\\src\\main\\resources\\assets\\customclient/");

    private ResourceLocation resourceLocation;

    private Path filePath;

    public ImageWidgetEvent(Screen screen, ResourceLocation resourceLocation, Path filePath){
        super(screen);

        this.resourceLocation = resourceLocation;
        this.filePath = filePath;

        Path toPath = CUSTOM_CLIENT_PATH.resolve(filePath.getFileName().toString().replace(" ","_"));
        if(!Files.exists(toPath)) {
            try {
                Files.copy(filePath, toPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public String getFileName() {
        return filePath.getFileName().toString();
    }

    public ResourceLocation getLocation(){
        return this.resourceLocation;
    }
    public static class Background extends ImageWidgetEvent{

        public Background(Screen screen,ResourceLocation resourceLocation, Path dynamicPath) {
            super(screen, resourceLocation, dynamicPath);
        }
    }
    public static class Image extends ImageWidgetEvent{

        public Image(Screen screen, ResourceLocation resourceLocation, Path dynamicPath) {
            super(screen, resourceLocation, dynamicPath);
        }
    }
}
