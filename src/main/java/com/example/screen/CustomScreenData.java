package com.example.screen;

import com.example.wrapper.widget.ButtonWrapper;
import com.example.wrapper.widget.ImageWrapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import customclient.CustomClient;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

//스크린 데이터를 저장하고 관리하는 클래스
public class CustomScreenData {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final ScreenFlow screenFlow;
    private final ScreenHandler screenHandler;
    private JsonObject widgetObject = new JsonObject();
    private JsonObject customObject = new JsonObject();
    protected String background = "customclient:textures/screenshot.png", dynamicBackground;
    private Path screenDataPath;

    public CustomScreenData(ScreenFlow screenFlow, String screenName){
        this.screenFlow = screenFlow;
        this.screenHandler = screenFlow.getWidget();
        screenDataPath = Path.of("./customclient/").resolve(screenName+".json");
    }


    public JsonObject getCustomObject() {
        return customObject;
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
                Files.writeString(screenDataPath, GSON.toJson(widgetObject));

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void save(){
        try {
            widgetObject.addProperty("background", background);
            widgetObject.add("titleWidgetButton", GSON.toJsonTree(screenFlow.getWidget().getDefaultButtons()));
            widgetObject.add("widgetButton", GSON.toJsonTree(screenFlow.getWidget().getButtons()));
            widgetObject.add("widgetImage", GSON.toJsonTree(screenFlow.getWidget().getImageList()));
            widgetObject.add("customObject", customObject);
            System.out.println(customObject+" : 커스텀 데이터 저장됨");
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
            if(json.equals("[]") || json.equals("{}"))
                return;
            JsonObject jsonObject = GSON.fromJson(json, JsonObject.class);
            setBackground(jsonObject.get("background").getAsString());

            if(jsonObject.has("titleWidgetButton"))
                screenHandler.getDefaultButtons().addAll(GSON.fromJson(jsonObject.get("titleWidgetButton"), new TypeToken<ArrayList<ButtonWrapper>>(){}.getType()));

            screenHandler.getButtons().addAll(GSON.fromJson(jsonObject.get("widgetButton"), new TypeToken<ArrayList<ButtonWrapper>>(){}.getType()));

            if(!jsonObject.get("widgetImage").getAsJsonArray().isEmpty()){
                screenHandler.getImageList().addAll(GSON.fromJson(jsonObject.get("widgetImage"), new TypeToken<ArrayList<ImageWrapper>>(){}.getType()));

            }
            if(jsonObject.has("customObject")) {
                customObject = jsonObject.get("customObject").getAsJsonObject();
                System.out.println(customObject+" : 커스텀 데이터 불러옴");
            }else{
                jsonObject.add("customObject", customObject);
                System.out.println(customObject+" : 생성됨");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
