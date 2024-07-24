package com.example.screen;

import com.example.ICustomBackground;
import com.example.gui.event.ImageWidgetEvent;
import com.example.swing.SwingHandler;
import com.example.wrapper.widget.ButtonWrapper;
import com.example.wrapper.widget.ImageWrapper;
import com.example.wrapper.widget.WidgetWrapper;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handler로 직접 접근 하지 않고도 여기서 처리할 수 있도록
 */
public class ScreenFlow {
    private static final Logger logger = LoggerFactory.getLogger(ScreenFlow.class);

    private Screen screen;
    private String screenName;
    private CustomScreenData data;
    int displayWidth = 0;
    int displayHeight = 0;
    private SwingHandler swingHandler = new SwingHandler();
    private WidgetHandler widgetHandler;//loadScreenData 메서드에서 초기화됨
    private SelectHandler selectHandler;

    ScreenFlow(){

    }

    public void reset(boolean dataToo){
        selectHandler = null;
        if(swingHandler != null)
            swingHandler.swingClose();
        swingHandler = new SwingHandler();
        if(dataToo)
            data = null;
    }

    /**
     * 현재 선택한 위젯이 있느냐?
     */
    public boolean hasSelectWidget(){
        return selectHandler != null;
    }

    /**
     * 해당하는 경로의 이미지 위젯이 있느냐?
     */
    public boolean hasImageWidget(String resouce){
        for(ImageWrapper imageWrapper : widgetHandler.getImageList()){
            if(imageWrapper.getResource().toString().equals(resouce)){
                return true;
            }
        }
        return false;
    }

    public void openScreen(Screen screen){
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }
    public void setScreenName(String name){
        this.screenName = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public SwingHandler getSwingHandler() {
        return swingHandler;
    }

    public void save(){
        if(data != null) {
            data.save();
        }
        else {
            logger.error("{}의 스크린에서 오류 발생", getScreenName());
            throw new NullPointerException("스크린의 데이터 없음");
        }
    }
    public JsonObject getCustomData(){
        return this.data.getCustomObject();
    }
    public void loadScreenData(){
        logger.info("화면 데이터 로딩 중: {}", screenName);
        if(displayWidth == 0)
        {
            displayWidth = Minecraft.getInstance().getWindow().getScreenWidth();
        }
        if(displayHeight == 0)
        {
            displayHeight = Minecraft.getInstance().getWindow().getScreenHeight();
        }
        if(widgetHandler != null){
            for(ButtonWrapper buttonWrapper : widgetHandler.getButtons()) {
                if (screen.renderables.contains(buttonWrapper.getWidget())) {
                    screen.renderables.remove(buttonWrapper.getWidget());
                }
            }
            widgetHandler.getImageList().clear();
        }
        widgetHandler = new WidgetHandler(screen);
        data = new CustomScreenData(this, screenName);
        data.initFiles();

        data.loadCustomWidgets();
        if(screenName.equals("ScreenNewTitle"))
            widgetHandler.loadDefaultWidgets();
        widgetHandler.makeCustomButtons(displayWidth, displayHeight);
        widgetHandler.syncWithSwing();
        if(screen instanceof ICustomBackground background)
            background.setBackground(new ResourceLocation(data.background));}

    public boolean clickWidget(double mouseX, double mouseY){
        logger.debug("위젯 클릭 - 좌표: ({}, {})", mouseX, mouseY);
        LinkedList<WidgetWrapper> allWidgets = widgetHandler.getAllWidget();

        for(WidgetWrapper clickedWidget : allWidgets) {
            if (clickedWidget.isMouseOver(mouseX, mouseY) ) {
                if(selectHandler != null && selectHandler.getWidget() == clickedWidget)
                    return true;
                this.swingHandler.openSwing(clickedWidget);
                selectHandler = new SelectHandler(clickedWidget);
                return true;
            }
        }
        return false;
    }

    @Nullable
    public SelectHandler selectWidget(){
        return selectHandler;
    }

    public WidgetHandler getWidget() {
        return widgetHandler;
    }

    public void dragWidget(double mouseX, double mouseY){
        selectHandler.setPosition((int) mouseX, (int) mouseY);
    }

    public void fileDropEvent(Path path){
        ResourceLocation resourceLocation = ScreenAPI.getDynamicTexture(path);
        System.out.println("파일 드롭 됨");
        if(resourceLocation == null) {
            return;
        }
        int select = JOptionPane.showOptionDialog(null, "어떤 걸로 설정할까요?", "이미지 불러오기", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"배경화면", "이미지", "취소"}, "취소");
        String fileName = path.getFileName().toString().toLowerCase();
        try {
            Path texturePack = Path.of("D:\\Projects\\four\\thirdCustomClient\\src\\main\\resources\\assets\\customclient\\", fileName);
            Pattern pattern = Pattern.compile("[a-zA-Z0-9/._-]+");
            Matcher matcher = pattern.matcher(fileName);
            if(!matcher.matches()){
                JOptionPane.showConfirmDialog(null, "파일 이름에 영어 소문자, 숫자, -_ 문자만 있어야 등록할 수 있습니다.");
                return;
            }
            if(!Files.exists(texturePack))
                Files.copy(path, texturePack);

            BufferedImage bi = ImageIO.read(path.toFile());


            switch (select) {

            case JOptionPane.YES_OPTION -> {
                data.dynamicBackground = resourceLocation.toString();
                data.setBackground("customclient:"+fileName );
                if(screen instanceof ICustomBackground background)//백그라운드 설정 가능한 GUI라면
                    background.setBackground(new ResourceLocation(data.background));
            }
            case JOptionPane.NO_OPTION -> {
                ImageWrapper image = new ImageWrapper(resourceLocation, fileName, 0, 0, bi.getWidth(), bi.getHeight(), 1);
                widgetHandler.addImage(image);
            }
        }
        if(select == JOptionPane.YES_OPTION)
            NeoForge.EVENT_BUS.post(new ImageWidgetEvent.Background(screen, resourceLocation, path));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void renderImageWidget(GuiGraphics pGuiGraphics){

    }

    public static boolean isKeyDown(int key){
        Minecraft mc = Minecraft.getInstance();
        long windowLong = mc.getWindow().getWindow();
        return InputConstants.isKeyDown(windowLong, key);
    }
}
