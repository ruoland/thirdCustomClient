package com.example;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import customclient.CustomClient;
import customclient.FakeTextureWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class GuiData {
    protected static final ResourceLocation DEFAULT_BACKGROUND_IMAGE = new ResourceLocation(CustomClient.MODID, "textures/screenshot.png");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Screen screen;
    private final JsonObject widgetObject = new JsonObject();

    private ArrayList<WidgetData> widgetArrayList = new ArrayList<>();
    private ArrayList<WidgetImage> widgetImageList = new ArrayList<>();

    protected String dynamicBackground, background = "customclient:textures/screenshot.png";
    private Path filePath;
    public GuiData(Screen screen, String screenName){
        this.screen = screen;
        filePath = Path.of("./customclient/").resolve(screenName+".json");
        makeFiles();

        //Screen에 기본 위젯, 불러오기
        loadDefaultWidget();
        //위젯과 정보 동기화
        syncWithDefaultWidget();
        //저장된 커스텀 위젯 불러오기
        loadWidgetList();

    }

    public void loadDefaultWidget(){
        if(widgetArrayList == null || widgetArrayList.isEmpty()) {
            for(int i = 0; i < screen.children().size();i++) {
                widgetArrayList.add(new WidgetData((AbstractWidget) screen.children().get(i)));
            }
        }
    }

    public void syncWithDefaultWidget(){
        for(int i = 0; i < 9; i++) {
            widgetArrayList.get(i).abstractWidget = (AbstractWidget) screen.children().get(i);
            widgetArrayList.get(i).widgetUpdate();
            System.out.println(widgetArrayList.get(i).getMessage() + "에 위젯 객체 추가 및 업데이트" +((AbstractWidget) screen.children().get(i)).isActive() + ((AbstractWidget) screen.children().get(i)).visible);
        }
        ICustomBackground customBackground = (ICustomBackground) screen;
        customBackground.setBackground(new ResourceLocation(getBackground()));
    }
    public String getBackground() {
        if (dynamicBackground != null)
            return dynamicBackground;
        else
            return background.contains(":") ? background : "customclient:"+background;
    }

    public void syncWithDefault(){
        for(int i = 0; i < 9; i++) {
            widgetArrayList.get(i).abstractWidget = (AbstractWidget) screen.children().get(i);
            widgetArrayList.get(i).dataUpdate();
        }
    }

    public void save(){
        try {
            widgetObject.addProperty("background", background);
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
            background = jsonObject.get("background").getAsString();
            widgetArrayList = GSON.fromJson(jsonObject.get("widgetButton"), new TypeToken<ArrayList<WidgetData>>(){}.getType());
            if(jsonObject.get("widgetImage").isJsonObject())
                widgetImageList = GSON.fromJson(jsonObject.get("widgetImage"), new TypeToken<ArrayList<WidgetImage>>(){}.getType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(WidgetData data : widgetArrayList){
            screen.renderables.add(new Button.Builder(Component.literal(data.message), (Button.OnPress) pButton -> {
            }).size(data.width, data.height).pos(data.x, data.y).build());
        }
    }
    public void addTextfield(WidgetData data){
        data.isTextField = true;
        widgetArrayList.add(data);
        ICustomRenderable customRenderable = (ICustomRenderable) screen;
        customRenderable.addRenderableWidget(data.abstractWidget);
    }
    public void addButton(WidgetData data){
        widgetArrayList.add(data);
        ICustomRenderable customRenderable = (ICustomRenderable) screen;
        customRenderable.addRenderableWidget(data.abstractWidget);
    }

    public void addImage(WidgetImage widgetImage){
        widgetImage.resource = "customclient:"+widgetImage.resource;
        widgetImageList.add(widgetImage);
    }

    public ArrayList<WidgetData> getWidgetArrayList() {
        return widgetArrayList;
    }

    public ArrayList<WidgetImage> getWidgetImageList() {
        return widgetImageList;
    }

    public void renderImage(GuiGraphics guiGraphics){
        for(WidgetImage image : widgetImageList){
            image.render(guiGraphics);
        }
    }
    public static class WidgetImage extends NewWidget {
        private transient ResourceLocation resourceLocation;
        private String resource;
        private boolean isVisible = true;

        WidgetImage(ResourceLocation resourceLocation, String fileName, int x, int y, int width, int height, float alpha){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.resource = fileName;
            this.resourceLocation = resourceLocation;
            this.alpha = alpha;
            createFakeWidget();
        }

        public ResourceLocation getResource() {
            return resourceLocation == null ? resourceLocation = new ResourceLocation("customclient", resource) : resourceLocation;
        }


        protected void render(GuiGraphics pGuiGraphics) {
            if(isVisible)
                ScreenNewTitle.renderTexture(pGuiGraphics, getResource(), getX(), getY(), getWidth(), getHeight(), alpha);
        }

        public FakeTextureWidget createFakeWidget(){
            if(getAbstractWidget() == null) {
                FakeTextureWidget fakeTextureWidget = new FakeTextureWidget(x, y, width, height, Component.literal(resource));
                setAbstractWidget(fakeTextureWidget);
            }
            return (FakeTextureWidget) getAbstractWidget();

        }

    }
    public static class WidgetData extends NewWidget {
        protected boolean isTextField = false;

        WidgetData(AbstractWidget widget){
            super(widget);

            dataUpdate();
        }

        public void dataUpdate(){
            x = abstractWidget.getX();
            y = abstractWidget.getY();
            width = abstractWidget.getWidth();
            height = abstractWidget.getHeight();
            visible = abstractWidget.visible;
            message = abstractWidget.getMessage().getString();
        }

        public void widgetUpdate(){
            abstractWidget.setX(x);
            abstractWidget.setY(y);
            abstractWidget.setWidth(width);
            abstractWidget.setHeight(height);
            abstractWidget.visible = visible;
            abstractWidget.setMessage(Component.literal(message));
        }


        public boolean isTextField() {
            return isTextField;
        }



    }

}
