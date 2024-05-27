package com.example;

import com.example.wrapper.CustomWidgetWrapper;
import com.example.wrapper.WidgetHandler;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import customclient.CustomClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class CustomScreenData {
    protected static final ResourceLocation DEFAULT_BACKGROUND_IMAGE = new ResourceLocation(CustomClient.MODID, "textures/screenshot.png");

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Screen currentScreen;
    private final JsonObject widgetObject = new JsonObject();

    private ArrayList<CustomWidgetWrapper.WidgetButtonWrapper> widgetButtonList = new ArrayList<>();
    private ArrayList<CustomWidgetWrapper.WidgetImageWrapper> widgetImageList = new ArrayList<>();
    private WidgetHandler widgetHandler = new WidgetHandler();

    protected String background = "customclient:textures/screenshot.png";
    private Path screenDataPath;

    public WidgetHandler getWidgetHandler() {
        return widgetHandler;
    }

    public CustomScreenData(Screen screen, String screenName){
        this.currentScreen = screen;
        screenDataPath = Path.of("./customclient/").resolve(screenName+".json");
        initFiles();
        //저장된 커스텀 위젯 불러오기
        loadCustomWidgetList();
    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }

    /**
     * 데이터가 저장될 폴더를 만들고, 파일들 초기화
     */
    public void initFiles(){
        Path customClient = Path.of("./customclient");
        try {
            if(!Files.exists(customClient))
                Files.createDirectories(customClient);
            if(!Files.exists(screenDataPath))
                Files.writeString(screenDataPath, GSON.toJson(widgetObject.isEmpty() ? widgetButtonList : widgetObject));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void save(){
        try {
            widgetObject.addProperty("background", background);
            widgetObject.add("widgetButton", GSON.toJsonTree(widgetButtonList));
            widgetObject.add("widgetImage", GSON.toJsonTree(widgetImageList));
            Files.writeString(screenDataPath, GSON.toJson(widgetObject));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void  loadCustomWidgetList(){
        String json = null;
        try {
            json = new String(Files.readAllBytes(screenDataPath));
            if(json.equals("[]"))
                return;
            JsonObject jsonObject = GSON.fromJson(json, JsonObject.class);
            background = jsonObject.get("background").getAsString();
            widgetButtonList = GSON.fromJson(jsonObject.get("widgetButton"), new TypeToken<ArrayList<CustomWidgetWrapper.WidgetButtonWrapper>>(){}.getType());
            if(jsonObject.get("widgetImage").isJsonObject())
                widgetImageList = GSON.fromJson(jsonObject.get("widgetImage"), new TypeToken<ArrayList<CustomWidgetWrapper.WidgetImageWrapper>>(){}.getType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(CustomWidgetWrapper.WidgetButtonWrapper data : widgetButtonList){
            currentScreen.renderables.add(new Button.Builder(Component.literal(data.getMessage()), (Button.OnPress) pButton -> {
            }).size(data.getWidth(), data.getHeight()).pos(data.getX(), data.getY()).build());
        }
    }


    public String getBackground() {
        if (dynamicBackground != null)
            return dynamicBackground;
        else
            return background.contains(":") ? background : "customclient:"+background;
    }

    /**
     * 불러온 위젯의 정보를 동기화 하기(SwingWidget에서 값을 변경 했을 때 사용 되는 메서드)
     */
    public void update(){
        for(int i = 0; i < widgetButtonList.size(); i++){

        }
    }
    public void addTextfield(CustomWidgetWrapper.WidgetButtonWrapper data){
        data.isTextField = true;
        widgetButtonList.add(data);
        ICustomRenderable customRenderable = (ICustomRenderable) currentScreen;
        customRenderable.addRenderableWidget(data.abstractWidget);
    }
    public void addButton(CustomWidgetWrapper.WidgetButtonWrapper data){
        widgetButtonList.add(data);
        ICustomRenderable customRenderable = (ICustomRenderable) currentScreen;
        customRenderable.addRenderableWidget(data.abstractWidget);
    }

    public void addImage(CustomWidgetWrapper.WidgetImageWrapper widgetImage){
        widgetImage.texture = "customclient:"+widgetImage.texture;
        widgetImageList.add(widgetImage);
    }

    public ArrayList<CustomWidgetWrapper.WidgetButtonWrapper> getWidgetList() {
        return widgetButtonList;
    }

    public ArrayList<CustomWidgetWrapper.WidgetImageWrapper> getWidgetImageList() {
        return widgetImageList;
    }

    public void renderImage(GuiGraphics guiGraphics){
        for(CustomWidgetWrapper.WidgetImageWrapper image : widgetImageList){
            image.render(guiGraphics);
        }
    }
}
