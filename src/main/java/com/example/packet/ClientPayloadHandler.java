package com.example.packet;

import com.example.userscreen.ScreenUserCustom;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {
    
    public static void handleData(final ScreenOpenData data, final IPayloadContext context) {
        // Do something with the data, on the network thread
        System.out.println(data.name());
        
        // Do something with the data, on the main thread
        context.enqueueWork(() -> {
            System.out.println("클라잉너트!!"+data.age());
                    Minecraft.getInstance().setScreen(new ScreenUserCustom(Component.literal(data.name())));
        })
        .exceptionally(e -> {
            // Handle exception
            context.disconnect(Component.translatable("customclient.networking.failed", e.getMessage()));
            return null;
        });
    }
}