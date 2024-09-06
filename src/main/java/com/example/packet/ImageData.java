package com.example.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ImageData(int id, String imagePath, int x, int y, float scale, int duration) implements CustomPacketPayload {

    public static final Type<ImageData> TYPE = new Type<>(new ResourceLocation("customclient", "image_data"));

    // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
    // 'imagePath' will be encoded and decoded as a string
    // 'age' will be encoded and decoded as an integer
    // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
    public static final StreamCodec<ByteBuf, ImageData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ImageData::id,
        ByteBufCodecs.STRING_UTF8,
        ImageData::imagePath,
        ByteBufCodecs.VAR_INT,
        ImageData::x,
        ByteBufCodecs.VAR_INT,
        ImageData::y,
        ByteBufCodecs.FLOAT,
        ImageData::scale,
        ByteBufCodecs.VAR_INT,
        ImageData::duration,

        ImageData::new
    );
    
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}