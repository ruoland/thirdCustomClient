package com.example.screen;

import com.example.wrapper.widget.ButtonWrapper;
import com.example.wrapper.widget.ImageWrapper;
import com.example.wrapper.widget.StringWrapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

//스크린 데이터를 저장하고 관리하는 클래스
public class CustomScreenData {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final ScreenFlow screenFlow;
    private final JsonObject widgetObject = new JsonObject();
    private final Path screenDataPath;
    private JsonObject customObject = new JsonObject();
    protected String background = "customclient:textures/screenshot.png";

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
            widgetObject.add("widgetString", GSON.toJsonTree(screenFlow.getWidget().getStringWrappers()));

            widgetObject.addProperty("global_font", ScreenFlow.getGlobalFont().toString());
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
        logger.info("1.유저가 추가한 위젯 로딩 중 - 파일: {}", screenDataPath);
        WidgetHandler widgetHandler = screenFlow.getWidget();
        try {
            String screenJson = new String(Files.readAllBytes(screenDataPath));

            if(screenJson.equals("[]") || screenJson.equals("{}")) //위젯이 비어 있음!
                return;

            JsonObject jsonObject = GSON.fromJson(screenJson, JsonObject.class);

            //배경 설정
            setBackground(jsonObject.get("background").getAsString());

            if(jsonObject.has("titleWidgetButton"))
                initTitle(jsonObject, widgetHandler);

            removeWidget(jsonObject, widgetHandler);

            if(!jsonObject.get("widgetImage").getAsJsonArray().isEmpty())
                loadImage(jsonObject, widgetHandler);

            if(jsonObject.has("widgetString" ))
                loadString(jsonObject, widgetHandler);

            if(jsonObject.has("global_font"))
                loadGlobalFont(jsonObject, widgetHandler);

            if(jsonObject.has("customObject")) {
                customObject = jsonObject.get("customObject").getAsJsonObject();
                System.out.println(customObject+" : 커스텀 데이터 불러옴");
            }else{
                jsonObject.add("customObject", customObject);
                System.out.println(customObject+" : 생성됨");
            }



        } catch (IOException e) {
            logger.error("커스텀 위젯 로딩 중 오류 발생", e);
            throw new RuntimeException(e);

        }

    }

    public void initTitle(JsonObject jsonObject, WidgetHandler widgetHandler){
    //타이틀 스크린
        widgetHandler.getDefaultButtons().addAll(GSON.fromJson(jsonObject.get("titleWidgetButton"), new TypeToken<ArrayList<ButtonWrapper>>() {
        }.getType()));
        logger.info("기본 버튼 불러오는 중 {}", jsonObject.get("titleWidgetButton"));
    }

    public void loadImage(JsonObject jsonObject, WidgetHandler widgetHandler){

        ArrayList<ImageWrapper> imageWrappers = GSON.fromJson(jsonObject.get("widgetImage"), new TypeToken<ArrayList<ImageWrapper>>(){}.getType());
        for (ImageWrapper imageWrapper : imageWrappers) {
            imageWrapper.createFakeWidget(imageWrapper.getX(), imageWrapper.getY(), imageWrapper.getWidth(), imageWrapper.getHeight(), imageWrapper.getMessage());
        }
        widgetHandler.getImageList().addAll(imageWrappers);
        logger.info("이미지 개수 {}", widgetHandler.getImageList().size());
    }

    public void loadGlobalFont(JsonObject jsonObject, WidgetHandler widgetHandler){
            screenFlow.setGlobalFont(new ResourceLocation(jsonObject.get("global_font").getAsString()));

    }
    public void loadString(JsonObject jsonObject, WidgetHandler widgetHandler){
            ArrayList<StringWrapper> stringWidgets = GSON.fromJson(jsonObject.get("widgetString"), new TypeToken<ArrayList<StringWrapper>>(){}.getType());
            widgetHandler.getStringWrappers().addAll(stringWidgets);
            logger.info("문자열 불러옵니다 :{}", stringWidgets);

    }
    public void removeWidget(JsonObject jsonObject, WidgetHandler widgetHandler){
        ArrayList<ButtonWrapper> buttonList = GSON.fromJson(jsonObject.get("widgetButton"), new TypeToken<ArrayList<ButtonWrapper>>(){}.getType());
        logger.info("커스텀 버튼의 개수 {}", buttonList.size());

        for(int i = 0; i < buttonList.size(); i++){
            ButtonWrapper buttonWrapper = buttonList.get(i);
            if(!buttonWrapper.isVisible()) {
                buttonList.remove(i);
                logger.info("버튼 제거 됨{}", buttonList.size());
            }
        }
        widgetHandler.getButtons().addAll(buttonList);

        ArrayList<StringWrapper> stringList = GSON.fromJson(jsonObject.get("widgetString"), new TypeToken<ArrayList<StringWrapper>>(){}.getType());
        logger.info("커스텀 버튼의 개수 {}", stringList.size());

        for(int i = 0; i < stringList.size(); i++){
            StringWrapper stringWrapper = stringList.get(i);
            if(!stringWrapper.isVisible()) {
                stringList.remove(i);
                logger.info("문자열 제거 됨{}", stringList.size());
            }
        }
        widgetHandler.getStringWrappers().addAll(stringList);
    }
}
