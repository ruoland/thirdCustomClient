package com.example.packet;

import com.mojang.blaze3d.platform.NativeImage;
import customclient.HUDWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class ImageClientPayloadHandler {

    public static void handleData(final ImageData data, final IPayloadContext context) {
        context.enqueueWork(() -> {
                    Minecraft mc = Minecraft.getInstance();
                    int screenWidth = mc.getWindow().getGuiScaledWidth();
                    int screenHeight = mc.getWindow().getGuiScaledHeight();

                    // 상대적 위치를 절대 위치로 변환
                    int x = data.x();
                    int y = data.y();
                    // 이미지 원본 크기 가져오기 (이 부분은 실제 구현에 따라 다를 수 있음)
                    int[] imageDimensions = getImageDimensions(ResourceLocation.tryParse(data.imagePath()));
                    int imageWidth = imageDimensions[0];
                    int imageHeight = imageDimensions[1];
                    float scale = Math.min(data.scale() * screenWidth / imageWidth, data.scale() * screenHeight / imageHeight);
                    int displayWidth = (int)(imageWidth * scale);
                    int displayHeight = (int)(imageHeight * scale);
                    HUDWidget.addImage(data.id(), data.imagePath(), x, y, displayWidth, displayHeight).setDuration(data.duration());
                })
                .exceptionally(e -> {
                    context.disconnect(Component.translatable("customclient.networking.failed", e.getMessage()));
                    return null;
                });
    }
    public static int[] getImageDimensions(ResourceLocation resourceLocation) {
        try {
            Optional<Resource> resource = Minecraft.getInstance().getResourceManager().getResource(resourceLocation);
            if (resource.isPresent()) {
                try (InputStream inputStream = resource.get().open()) {
                    NativeImage nativeImage = NativeImage.read(inputStream);
                    int width = nativeImage.getWidth();
                    int height = nativeImage.getHeight();
                    nativeImage.close();
                    return new int[]{width, height};
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 이미지를 로드할 수 없는 경우 기본값 반환
        return new int[]{16, 16};
    }
}