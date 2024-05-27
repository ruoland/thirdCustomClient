package com.example.wrapper;

import com.example.ScreenAPI;
import customclient.FakeTextureWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class WidgetImageWrapper extends CustomWidgetWrapper {
        private transient ResourceLocation resourceLocation;
        private String resource;
        private boolean isVisible = true;

        WidgetImageWrapper(ResourceLocation resourceLocation, String fileName, int x, int y, int width, int height, float alpha){
            setX(x);
            setY(y);
            setWidth(width);
            setHeight(height);
            this.resource = fileName;
            this.resourceLocation = resourceLocation;
            setAlpha(alpha);
            createFakeWidget();
        }

        public ResourceLocation getResource() {
            return resourceLocation == null ? resourceLocation = new ResourceLocation("customclient", resource) : resourceLocation;
        }

        public void render(GuiGraphics pGuiGraphics) {
            if(isVisible)
                ScreenAPI.renderTexture(pGuiGraphics, getResource(), getX(), getY(), getWidth(), getHeight(), getAlpha());
        }

        public FakeTextureWidget createFakeWidget(){
            if(getWidget() == null) {
                FakeTextureWidget fakeTextureWidget = new FakeTextureWidget(getX(), getY(), getWidth(), getHeight(), Component.literal(resource));
                setAbstractWidget(fakeTextureWidget);
            }
            return (FakeTextureWidget) getWidget();

        }

    }
    