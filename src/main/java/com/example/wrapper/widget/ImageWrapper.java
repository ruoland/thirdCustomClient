package com.example.wrapper.widget;

import com.example.screen.ScreenAPI;
import customclient.FakeTextureWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageWrapper extends WidgetWrapper {
    private static final Logger log = LoggerFactory.getLogger(ImageWrapper.class);
    private String resourcePath;

    public ImageWrapper(String resourcePath, int x, int y, int width, int height, float alpha){
        createFakeWidget(x, y, width, height, resourcePath);
        this.resourcePath = resourcePath;
        setAlpha(alpha);
    }

    public ResourceLocation getResource() {
        return new ResourceLocation("customclient", resourcePath.replace("customclient:", ""));
    }

    @Override
    public String getMessage() {
        return resourcePath;
    }

    public FakeTextureWidget createFakeWidget(int x, int y, int width, int height, String resource){
        if(getWidget() == null) {
            FakeTextureWidget fakeTextureWidget = new FakeTextureWidget(x, y, width, height, Component.literal(resource));
            setAbstractWidget(fakeTextureWidget);
            setX(x);
            setY(y);
            setWidth(width);
            setHeight(height);
            this.resourcePath = resource;
        } else {
            getWidget().setX(x);
            getWidget().setY(y);
            getWidget().setWidth(width);
            getWidget().setHeight(height);
            getWidget().setMessage(Component.literal(resource));
        }

        return (FakeTextureWidget) getWidget();
    }

    public void render(GuiGraphics pGuiGraphics) {
        if(isVisible()) {
            if (getWidget() == null)
                setAbstractWidget(createFakeWidget(getX(), getY(), getWidth(), getHeight(), resourcePath));

            ScreenAPI.renderTexture(pGuiGraphics, getResource(), getX(), getY(), getWidth(), getHeight(), getAlpha());
        }
    }
}