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
    private transient ResourceLocation resourceLocation;
    private String resource;

    public ImageWrapper(ResourceLocation resourceLocation, String fileName, int x, int y, int width, int height, float alpha){
        createFakeWidget(x,y,width,height,fileName);
        this.resourceLocation = resourceLocation;
        setAlpha(alpha);
    }

    public ImageWrapper(ResourceLocation resourceLocation, String fileName, int i, int j, int logoWidth, int logoHeight) {
            this(resourceLocation, fileName, i, j, logoWidth, logoHeight, 1);
    }

    public void init(){
        createFakeWidget(getX(),getY(),getWidth(),getHeight(), resource);
    }

    public ResourceLocation getResource() {
            return resourceLocation == null ? resourceLocation = new ResourceLocation("customclient", resource.replace("customclient:", "")) : resourceLocation;
    }

    @Override
    public String getMessage() {
        return getResource().toString();
    }

    public FakeTextureWidget createFakeWidget(int x, int y, int width, int height, String resource){
        if(getWidget() == null) {
            FakeTextureWidget fakeTextureWidget = new FakeTextureWidget(getX(), getY(), getWidth(), getHeight(), Component.literal(resource));
            setAbstractWidget(fakeTextureWidget);
            setX(x);
            setY(y);
            setWidth(width);
            setHeight(height);
            this.resource = resource;
        }
        else{
            getWidget().setX(getX());
            getWidget().setY(getY());
            getWidget().setWidth(getWidth());
            getWidget().setHeight(getHeight());
            getWidget().setMessage(Component.literal(resource));
        }

        return (FakeTextureWidget) getWidget();
    }

    public void render(GuiGraphics pGuiGraphics) {
        if(isVisible()) {
            if (getWidget() == null)
                setAbstractWidget(createFakeWidget(getX(), getY(), getWidth(), getHeight(), resource));
            ScreenAPI.renderTexture(pGuiGraphics, getResource(), getX(), getY(), getWidth(), getHeight(), getAlpha());
        }
        else
            log.error("이미지가 투명한 상태입니다.");
    }

}
    