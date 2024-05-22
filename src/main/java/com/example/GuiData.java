package com.example;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import customclient.CustomClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.core.util.JsonUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class GuiData {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Screen screen;
    private ArrayList<WidgetData> widgetArrayList = new ArrayList<>();
    private ArrayList<WidgetImage> widgetImageList = new ArrayList<>();
    private JsonObject widgetObject = new JsonObject();
    private Path filePath;
    public GuiData(Screen screen, String screenName){
        this.screen = screen;

        filePath = Path.of("./customclient/").resolve(screenName+".json");

        makeFiles();
        loadWidgetList();
        if(widgetArrayList == null || widgetArrayList.isEmpty()) {
            for(int i = 0; i < screen.children().size();i++) {
                widgetArrayList.add(new WidgetData((AbstractWidget) screen.children().get(i)));
            }
        }
        updateWidget();
    }


    public void updateWidget(){
        for(int i = 0; i < screen.children().size(); i++) {
            widgetArrayList.get(i).widget = (AbstractWidget) screen.children().get(i);
            widgetArrayList.get(i).widgetUpdate();
        }

        if(screen.children().isEmpty() || widgetArrayList.isEmpty())
        {
            throw new NullPointerException(filePath.toString() + "의 커스텀 위젯과 화면의 위젯이 존재하지 않음");
        }
    }

    public void updateData(){
        if(screen.children().isEmpty()) {
            CustomClient.LOGGER.info("너무 빠른 데이터 업데이트");
        }
        for(int i = 0; i < screen.children().size(); i++) {
            widgetArrayList.get(i).widget = (AbstractWidget) screen.children().get(i);
            widgetArrayList.get(i).dataUpdate();
        }
    }

    public void save(){
        try {
            widgetObject.add("widgetButton", GSON.toJsonTree(widgetArrayList));
            widgetObject.add("widgetImage", GSON.toJsonTree(widgetImageList));
            Files.writeString(filePath, GSON.toJson(widgetObject));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void makeFiles(){
        Path customClient = Path.of("./customclient");
        try {
            if(!Files.exists(customClient))
                Files.createDirectories(customClient);
            if(!Files.exists(filePath))
                Files.writeString(filePath, GSON.toJson(widgetArrayList));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void  loadWidgetList(){
        String json = null;
        try {
            json = new String(Files.readAllBytes(filePath));
            if(json.equals("[]"))
                return;
            JsonObject jsonObject = GSON.fromJson(json, JsonObject.class);
            widgetArrayList = GSON.fromJson(jsonObject.get("widgetButton"), new TypeToken<ArrayList<WidgetData>>(){}.getType());
            widgetImageList = GSON.fromJson(jsonObject.get("widgetImage"), new TypeToken<ArrayList<WidgetImage>>(){}.getType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<WidgetImage> getWidgetImageList() {
        return widgetImageList;
    }
    public class WidgetImage{
        private transient ResourceLocation resourceLocation;
        private String resource;
        private int id, x, y, width, height;
        private boolean isVisible = true;
        private float alpha = 1;
        WidgetImage(){

        }
        public void render(GuiGraphics pGuiGraphcis){
            if(isVisible)
                ScreenNewTitle.renderTexture(pGuiGraphcis, getResource(), x, y, width, height, alpha);
        }
        public ResourceLocation getResource() {
            return resourceLocation == null ? resourceLocation = new ResourceLocation(resource) : resourceLocation;
        }
    }
    public class WidgetData{

        private transient AbstractWidget widget;
        private int id, x, y, width, height;
        private boolean isVisible , active;
        private float alpha = 1;
        private String message;
        WidgetData(AbstractWidget widget){
            this.widget = widget;
            dataUpdate();
        }

        public void dataUpdate(){
            x = widget.getX();
            y = widget.getY();
            width = widget.getWidth();
            height = widget.getHeight();
            isVisible = widget.visible;
            active = widget.active;
            message = widget.getMessage().getString();
        }

        public void widgetUpdate(){
            widget.setX(x);
            widget.setY(y);
            widget.setWidth(width);
            widget.setHeight(height);
            widget.visible = isVisible;
            widget.active = active;
            widget.setMessage(Component.literal(message));
        }


    }

}
