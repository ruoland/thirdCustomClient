package com.example;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import customclient.CustomClient;
import customclient.FakeTextureWidget;
import customclient.Widget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class GuiData {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    protected static final ResourceLocation DEFAULT_BACKGROUND_IMAGE = new ResourceLocation(CustomClient.MODID, "textures/screenshot.png");
    private final Screen screen;
    private final JsonObject widgetObject = new JsonObject();

    private ArrayList<WidgetData> widgetArrayList = new ArrayList<>();
    private ArrayList<WidgetImage> widgetImageList = new ArrayList<>();

    protected String background = "customclient:textures/screenshot.png";
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
        ICustomBackground customBackground = (ICustomBackground) screen;
        customBackground.setBackground(new ResourceLocation(background));

        if(screen.children().isEmpty() || widgetArrayList.isEmpty())
            throw new NullPointerException(filePath.toString() + "의 커스텀 위젯과 화면의 위젯이 존재하지 않음");
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
    }
    public void addButton(WidgetData data){
        widgetArrayList.add(data);
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
    public static class WidgetImage extends Widget {
        private transient ResourceLocation resourceLocation;
        private String resource;
        private boolean isVisible = true;

        WidgetImage(ResourceLocation resourceLocation, String fileName, int x, int y, int width, int height, float alpha){
            this.resource = fileName;
            this.resourceLocation = resourceLocation;
            this.alpha = alpha;
            createFakeWidget();
        }

        public ResourceLocation getResource() {
            return resourceLocation == null ? resourceLocation = new ResourceLocation(resource) : resourceLocation;
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
    public class WidgetData extends Widget{

        private transient AbstractWidget widget;
        private String action;

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
            visible = widget.visible;
            message = widget.getMessage().getString();
        }

        public void widgetUpdate(){
            widget.setX(x);
            widget.setY(y);
            widget.setWidth(width);
            widget.setHeight(height);
            widget.visible = visible;

            widget.setMessage(Component.literal(message));
        }


        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }

}
