package com.example.wrapper.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringWrapper extends WidgetWrapper{
    private static final Minecraft mc = Minecraft.getInstance();
    private static final Logger log = LoggerFactory.getLogger(StringWrapper.class);

    public StringWrapper(String text, int x, int y){
        this(text, null, x,y, mc.font.width(Component.literal(text).getVisualOrderText()), 9);
    }
    public StringWrapper(String text, String customFont, int x, int y, int width, int height){
        super(new StringWidget(Component.literal(text), Minecraft.getInstance().font));
        this.setCustomFont(customFont);
        this.setX(x);
        this.setY(y);
        this.setWidth(width);
        this.setHeight(height);
        this.setVisible(true);

        createWidget(text);

    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {

        return super.isMouseOver(mouseX, mouseY);
    }

    public void createWidget(String text){
        MutableComponent textComponent = Component.literal(text);
        if(getCustomFont() != null)
            textComponent = textComponent.withStyle(Style.EMPTY.withFont(new ResourceLocation("customclient", getCustomFont())));

        Font font = Minecraft.getInstance().font;
        StringWidget widget = new StringWidget(
                getX(), getY(), font.width(textComponent.getVisualOrderText()), 9,
                textComponent,
                font
        );
        setAbstractWidget(widget);
        setMessage(text);
        setWidth(font.width(textComponent.getVisualOrderText()));
        setHeight(9);
    }


    public void render(GuiGraphics pGuiGraphics, float partica){
        getWidget().render(pGuiGraphics, 0,0, partica);
    }
}
