package com.example.packet;

import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {
    
    public static void handleData(final ScreenData data, final IPayloadContext context) {
        // Do something with the data, on the network thread
        System.out.println(data.name());
        
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