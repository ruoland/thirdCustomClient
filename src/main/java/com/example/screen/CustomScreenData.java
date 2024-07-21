package com.example.screen;

import com.example.wrapper.widget.ButtonWrapper;
import com.example.wrapper.widget.ImageWrapper;
import com.example.wrapper.widget.WidgetWrapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

//스크린 데이터를 저장하고 관리하는 클래스
public class CustomScreenData {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final ScreenFlow screenFlow;
    private JsonObject widgetObject = new JsonObject();
    private JsonObject customObject = new JsonObject();
    protected String background = "customclient:textures/screenshot.png", dynamicBackground;
    private Path screenDataPath;
    private static final Logger logger = LoggerFactory.getLogger(CustomScreenData.class);

    public CustomScreenData(ScreenFlow screenFlow, String screenName){
        this.screenFlow = screenFlow;
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
        logger.info("화면 데이터 저장 중 - 파일: {}", screenDataPath);
        try {
            widgetObject.addProperty("background", background);
            if(widgetObject.has("titleWidgetButton"))
                logger.warn("경고, 저장하려는데 기본 위젯 버튼이 이미 존재합니다.");
            else {

                widgetObject.add("titleWidgetButton", GSON.toJsonTree(screenFlow.getWidget().getDefaultButtons()));
            }
            widgetObject.add("widgetButton", GSON.toJsonTree(screenFlow.getWidget().getButtons()));
            widgetObject.add("widgetImage", GSON.toJsonTree(screenFlow.getWidget().getImageList()));
            widgetObject.add("customObject", customObject);
            Files.writeString(screenDataPath, GSON.toJson(widgetObject));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setBackground(String background) {
        this.background = background;
    }

    public void loadCustomWidgets(){
        logger.info("1.커스텀 위젯 로딩 중 - 파일: {}", screenDataPath);
        WidgetHandler widgetHandler = screenFlow.getWidget();
        try {
            String json = new String(Files.readAllBytes(screenDataPath));
            if(json.equals("[]") || json.equals("{}"))
                return;
            JsonObject jsonObject = GSON.fromJson(json, JsonObject.class);
            setBackground(jsonObject.get("background").getAsString());
            if(jsonObject.has("titleWidgetButton")) {
                widgetHandler.getDefaultButtons().addAll(GSON.fromJson(jsonObject.get("titleWidgetButton"), new TypeToken<ArrayList<ButtonWrapper>>() {
                }.getType()));
            }
            ArrayList<ButtonWrapper> arrayList = GSON.fromJson(jsonObject.get("widgetButton"), new TypeToken<ArrayList<ButtonWrapper>>(){}.getType());
            ArrayList<ButtonWrapper> buttonWrappers = arrayList;
            logger.info("커스텀 버튼의 개수 {}",buttonWrappers.size());

            for(int i = 0; i < arrayList.size(); i++){
                ButtonWrapper buttonWrapper = arrayList.get(i);
                if(!buttonWrapper.isVisible()) {
                    buttonWrappers.remove(i);
                    logger.info("버튼 제거 됨{}", buttonWrappers.size());
                }
            }
            widgetHandler.getButtons().addAll(buttonWrappers);

            if(!jsonObject.get("widgetImage").getAsJsonArray().isEmpty()){
                widgetHandler.getImageList().addAll(GSON.fromJson(jsonObject.get("widgetImage"), new TypeToken<ArrayList<ImageWrapper>>(){}.getType()));
            }

            if(jsonObject.has("customObject")) {
                customObject = jsonObject.get("customObject").getAsJsonObject();
                System.out.println(customObject+" : 커스텀 데이터 불러옴");
            }else{
                jsonObject.add("customObject", customObject);
                System.out.println(customObject+" : 생성됨");
            }
            logger.info("3. 이미지 개수 {}", widgetHandler.getImageList().size());
        } catch (IOException e) {
            logger.error("커스텀 위젯 로딩 중 오류 발생", e);
            throw new RuntimeException(e);

        }

    }

}
