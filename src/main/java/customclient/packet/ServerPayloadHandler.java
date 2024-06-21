package customclient.packet;

import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {

    public static void onMyData(MyData data, IPayloadContext context) {
        context.enqueueWork(() -> {
                    System.out.println(data.name());
                })
                .exceptionally(e -> {
                    // Handle exception
                    context.disconnect(Component.translatable("my_mod.configuration.failed", e.getMessage()));
                    return null;
                })
                .thenAccept(v -> {
                    context.reply(new AckPayload());
                });
    }
}