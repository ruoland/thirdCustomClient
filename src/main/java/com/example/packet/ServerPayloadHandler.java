package com.example.packet;

import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {
    
    public static void handleData(final ScreenOpenData data, final IPayloadContext context) {
        // Do something with the data, on the network thread
        System.out.println(data.name());
        
        // Do something with the data, on the main thread
        context.enqueueWork(() -> {
            System.out.println("서버 사이드! "+data.age());
        })
        .exceptionally(e -> {
            // Handle exception
            context.disconnect(Component.translatable("customclient.networking.failed", e.getMessage()));
            return null;
        });
    }
}