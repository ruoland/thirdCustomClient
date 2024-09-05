package com.example.packet;

import com.example.userscreen.ScreenUserCustom;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ScreenClientPayloadHandler {
    
    public static void handleData(final ScreenData data, final IPayloadContext context) {
        // Do something with the data, on the network thread

        // Do something with the data, on the main thread
        context.enqueueWork(() -> {
            System.out.println("클라이언트!!"+data.command());
            if(data.command() == 0)
                    Minecraft.getInstance().setScreen(new ScreenUserCustom(Component.literal(data.name())));
            else if(data.command() == 1 && Minecraft.getInstance().screen instanceof ScreenUserCustom)
                Minecraft.getInstance().setScreen(null);
        })
        .exceptionally(e -> {
            // Handle exception
            context.disconnect(Component.translatable("customclient.networking.failed", e.getMessage()));
            return null;
        });
    }
}