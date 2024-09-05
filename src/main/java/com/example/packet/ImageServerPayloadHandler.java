package com.example.packet;

import com.example.event.UIImage;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ImageServerPayloadHandler {
    
    public static void handleData(final ImageData data, final IPayloadContext context) {
        // Do something with the data, on the network thread

        
        // Do something with the data, on the main thread
        context.enqueueWork(() -> {

        })
        .exceptionally(e -> {
            // Handle exception
            context.disconnect(Component.translatable("customclient.networking.failed", e.getMessage()));
            return null;
        });
    }
}