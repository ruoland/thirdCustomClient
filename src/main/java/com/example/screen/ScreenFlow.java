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
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.nio.file.Path;

/**
 * Handler로 직접 접근 하지 않고도 여기서 처리할 수 있도록
 */
public class ScreenFlow {
    private Screen screen;
    private String screenName;
    private CustomScreenData data;

    private SwingHandler swingHandler = new SwingHandler();
    private ScreenHandler screenHandler;//loadScreenData 메서드에서 초기화됨
    private SelectHandler selectHandler;

    ScreenFlow(){

    }

    public void reset(){
        selectHandler = null;
        if(swingHandler != null)
            swingHandler.swingClose();
        swingHandler = new SwingHandler();
        data = null;
    }

    public boolean hasSelectWidget(){
        return selectHandler != null;
    }

    public boolean hasImageWidget(String resouce){
        for(ImageWrapper imageWrapper : screenHandler.getImageList()){
            if(imageWrapper.getResource().toString().equals(resouce)){
                return true;
            }
        }
        return false;
    }

    public Screen getScreen() {
        return screen;
    }

    public SwingHandler getSwingHandler() {
        return swingHandler;
    }

    public void setScreenName(String name){
        this.screenName = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void openScreen(Screen screen){
        this.screen = screen;
    }

    public void save(){
        if(data != null)
            data.save();
        else
            throw new NullPointerException("스크린의 데이터 없음");
    }

    public JsonObject getCustomData(){
        return this.data.getCustomObject();
    }

    public void loadScreenData(){
        screenHandler = new ScreenHandler(screen);
        data = new CustomScreenData(this, screenName);
        data.initFiles(); //기본 파일 생성

        data.loadCustomWidgets();
        screenHandler.loadDefaultWidgets();
        screenHandler.makeCustomButtons();
        screenHandler.update();

        if(screen instanceof ICustomBackground background)//백그라운드 설정 가능한 GUI라면
            background.setBackground(new ResourceLocation(data.background));
    }

    public void clickWidget(double mouseX, double mouseY){
        for(WidgetWrapper clickedWidget : screenHandler.getAllWidget()) {
            if (clickedWidget.isMouseOver(mouseX, mouseY)) {
                this.swingHandler.openSwing(clickedWidget);
                selectHandler = new SelectHandler(clickedWidget);
                return;
            }
        }
        System.out.println("선택된 위젯을 찾지 못함: "+screenHandler.getAllWidget());

    }

    @Nullable
    public SelectHandler selectWidget(){
        return selectHandler;
    }

    public ScreenHandler getWidget() {
        return screenHandler;
    }


    public void dragWidget(double mouseX, double mouseY){
        selectHandler.setPosition((int) mouseX, (int) mouseY);
    }

    public void fileDropEvent(Path path){
        ResourceLocation resourceLocation = ScreenAPI.getDynamicTexture(path);
        if(resourceLocation == null) {
            return;
        }
        int select = JOptionPane.showOptionDialog(null, "어떤 걸로 설정할까요?", "이미지 불러오기", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"배경화면", "이미지", "취소"}, "취소");
        switch (select) {
            case JOptionPane.YES_OPTION -> {
                data.dynamicBackground = resourceLocation.toString();
                data.setBackground("customclient:"+ path.getFileName().toString());
                if(screen instanceof ICustomBackground background)//백그라운드 설정 가능한 GUI라면
                    background.setBackground(new ResourceLocation(data.background));
            }
            case JOptionPane.NO_OPTION -> {
            }
        }
        if(select == JOptionPane.YES_OPTION)
            NeoForge.EVENT_BUS.post(new ImageWidgetEvent.Background(screen, resourceLocation, path));
        else {
            ImageWrapper image = new ImageWrapper(resourceLocation, path.getFileName().toString(), 0, 0, screen.width, screen.height, 1);
            screenHandler.addImage(image);
        }
    }

    public void renderImageWidget(GuiGraphics pGuiGraphics){
        if(!screenHandler.getImageList().isEmpty()) {
            for (ImageWrapper imageWrapper : screenHandler.getImageList()) {
                imageWrapper.render(pGuiGraphics);
            }
        }
    }

    public void addButton(String name, int width, int height, int x, int y){
        ButtonWrapper button = new ButtonWrapper(new Button.Builder(Component.literal(name), pButton -> {
        }).size(width, height).pos(x, y).build());
        screenHandler.addButton(button);
    }

    public static boolean isKeyDown(int key){
        Minecraft mc = Minecraft.getInstance();
        long windowLong = mc.getWindow().getWindow();
        return InputConstants.isKeyDown(windowLong, key);
    }
}
