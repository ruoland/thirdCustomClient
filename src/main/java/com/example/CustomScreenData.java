package com.example;

import com.example.wrapper.WidgetButtonWrapper;
import com.example.wrapper.WidgetHandler;
import com.example.wrapper.WidgetImageWrapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import customclient.CustomClient;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

//스크린 데이터를 저장하고 관리하는 클래스
public class CustomScreenData {
    protected static final ResourceLocation DEFAULT_BACKGROUND_IMAGE = new ResourceLocation(CustomClient.MODID, "textures/screenshot.png");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final ScreenFlow screenFlow;
    private final WidgetHandler widgetHandler;
    private final JsonObject widgetObject = new JsonObject();

    protected String background = "customclient:textures/screenshot.png";
    private Path screenDataPath;


    public CustomScreenData(ScreenFlow screenFlow, String screenName){

        this.screenFlow = screenFlow;
        this.widgetHandler = screenFlow.getScreenWidgets();
        screenDataPath = Path.of("./customclient/").resolve(screenName+".json");
    }

    public WidgetHandler getWidgetHandler() {
        return screenFlow.getScreenWidgets();
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
                Files.writeString(screenDataPath, GSON.toJson(widgetObject.isEmpty() ? getWidgetHandler().getWidgetButtonList() : widgetObject));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void save(){
        try {
            widgetObject.addProperty("background", background);
            widgetObject.add("widgetButton", GSON.toJsonTree(getWidgetHandler().getWidgetButtonList()));
            widgetObject.add("widgetImage", GSON.toJsonTree(getWidgetHandler().getWidgetImageList()));
            Files.writeString(screenDataPath, GSON.toJson(widgetObject));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setBackground(String background) {
        this.background = background;
    }


    public void loadCustomWidgets(){
        try {
            String json = new String(Files.readAllBytes(screenDataPath));
            if(json.equals("[]"))
                return;
            JsonObject jsonObject = GSON.fromJson(json, JsonObject.class);

            setBackground(jsonObject.get("background").getAsString());
            widgetHandler.getWidgetButtonList().addAll(GSON.fromJson(jsonObject.get("widgetButton"), new TypeToken<ArrayList<WidgetButtonWrapper>>(){}.getType()));
            widgetHandler.getWidgetImageList().add(GSON.fromJson(jsonObject.get("widgetImage"), new TypeToken<ArrayList<WidgetImageWrapper>>(){}.getType()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
