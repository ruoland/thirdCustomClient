package com.example.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ScreenData(String name, int command) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ScreenData> TYPE = new CustomPacketPayload.Type<>(new ResourceLocation("customclient", "my_data"));

    // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
    // 'name' will be encoded and decoded as a string
    // 'age' will be encoded and decoded as an integer
    // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
    public static final StreamCodec<ByteBuf, ScreenData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8,
        ScreenData::name,
        ByteBufCodecs.VAR_INT,
        ScreenData::command,
        ScreenData::new
    );
    
    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}