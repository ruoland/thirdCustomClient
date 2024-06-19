package customclient.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CCData() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CCData> TYPE = new CustomPacketPayload.Type<>(new ResourceLocation("mymod", "ack"));

    // Unit codec with no data to write
    public static final StreamCodec<ByteBuf, CCData> STREAM_CODEC = StreamCodec.unit(new CCData());

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
