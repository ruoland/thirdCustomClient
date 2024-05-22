package customclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import customclient.swing.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public abstract class ScreenCustom extends TitleScreen {
    protected ResourceLocation BACKGROUND_IMAGE = new ResourceLocation(CustomClient.MODID, "textures/screenshot.png");
    private SwingManager swingButtonManager;
    private SwingManager swingDraw;
    private SwingManager swingBackgroundManager;
    protected WidgetData widgetData;

    protected ButtonFunction buttonFunction;

    protected boolean editMode = false;
    protected Widget selectWidget;

    ScreenCustom(String screenNameFolder){
        System.setProperty("java.awt.headless", "false");
        widgetData = new WidgetData(this, Paths.get(screenNameFolder));
        swingButtonManager = new SwingManager.ButtonManager(this);
        swingBackgroundManager = new SwingManager.BackgroundManager(this);
        buttonFunction = new ButtonFunction(this);
    }

    @Override
    protected void init() {
        super.init();
        swingButtonManager.init();
        swingBackgroundManager.init();

    }

    public abstract void addButton();

    public void changeEditMode(){
        editMode = !editMode;
        selectWidget = null;
        getData().saveWidget();
    }

    public void setBackgroundImage(ResourceLocation backgroundImage) {
        BACKGROUND_IMAGE = backgroundImage;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        if(editMode){
            pGuiGraphics.drawString(font, Component.literal("편집 모드 실행 중"),0, 0,0xFFFFFF, false);
            pGuiGraphics.drawString(font, Component.literal("버튼을 이동할 때 오른쪽 클릭으로 멈출 수 있습니다..."),0, 20,0xFFFFFF, false);
        }
        for(DrawTexture drawTexture : getData().getDrawTextureList()){
            renderTexture(pGuiGraphics, drawTexture.getTexture(), drawTexture.getX(), drawTexture.getY(), drawTexture.getWidth(), drawTexture.getHeight(), drawTexture.getAlpha());
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if(editMode){
            for(GuiButton button : getData().getButtonList()){
                if(button.isMouseOver(pMouseX, pMouseY)) {
                    selectWidget = button;
                    if(pButton == 0 && !button.isLock()) {
                        if (selectWidget == button) {
                            return false;
                        }
                        else {
                            swingButtonManager.close();
                            swingButtonManager.setSwing(new SwingButton());
                        }
                        return true;
                    }
                    else if(pButton == 1){
                        selectWidget.setLock(false);
                    }
                }
            }
            for(DrawTexture texture : getData().getDrawTextureList()){
                if(pButton == 0 && texture.canSelectByMouse(pMouseX, pMouseY)) {
                    selectWidget = texture;
                    return true;
                }
            }
            selectWidget = null;
            return false;
        }
        else{
            for(GuiButton button : getData().getButtonList()){
                if(button.isMouseOver(pMouseX, pMouseY)) {
                    actionPerformed(button.getID(), pMouseX, pMouseY, pButton);
                    return false;
                }
            }
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    public void actionPerformed(int buttonID, double mouseX, double mouseY, int pButton){
        buttonFunction.runScript(getData().getButtonList().get(buttonID));
    }
    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        super.mouseMoved(pMouseX, pMouseY);
        if(editMode) {
            if(isSelect()){
                int mouseX = (int) pMouseX;
                int mouseY = (int) pMouseY;
                selectWidget.setPosition(mouseX, mouseY);
                swingButtonManager.update();
            }
        }
    }

    public boolean isSelect(){
        return selectWidget != null;
    }


    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == 342) {
            changeEditMode();
        }
        if (editMode) {
            if (isSelect()) {

                switch (pKeyCode) {
                    case GLFW.GLFW_KEY_RIGHT:
                        selectWidget.setWidth(selectWidget.getWidth() + 1);
                    case GLFW.GLFW_KEY_LEFT:
                        selectWidget.setWidth(selectWidget.getWidth() - 1);
                    case GLFW.GLFW_KEY_UP:
                        selectWidget.setHeight(selectWidget.getHeight() + 1);
                    case GLFW.GLFW_KEY_DOWN:
                        selectWidget.setHeight(selectWidget.getHeight() - 1);
                }
            }
            return false;
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public void onFilesDrop(List<Path> pPacks) {
        super.onFilesDrop(pPacks);
        String fileName = pPacks.get(0).toFile().getName();
        ResourceLocation resourceLocation = getTexture(pPacks.get(0));
        int select = JOptionPane.showOptionDialog(null, "어떤 걸로 설정할까요?" , "이미지 불러오기", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"배경화면", "이미지", "취소"}, "취소");

        switch (select){
            case JOptionPane.YES_OPTION -> {
                BACKGROUND_IMAGE = resourceLocation;
            }
            case JOptionPane.NO_OPTION -> {
                getData().getDrawTextureList().add(new DrawTexture(fileName, resourceLocation));
            }
        }
    }

    public static ResourceLocation getTexture(Path dropFile){
        Path toPath = Paths.get("D:\\Projects\\thirdCustomClient\\src\\main\\resources\\assets\\customclient/"+dropFile.getFileName().toString().replace(" ","_"));
        try {
            if(!Files.exists(toPath))
                Files.copy(dropFile, toPath);

            NativeImage nativeImage = NativeImage.read(new FileInputStream(toPath.toString()));
            DynamicTexture dynamicTexture = new DynamicTexture(nativeImage);

            return Minecraft.getInstance().getTextureManager().register("customclient", dynamicTexture);
        }
        catch (IOException e){
            JOptionPane.showMessageDialog(null, "PNG 파일만 인식합니다!");
        }
        return null;
    }

    protected void renderTexture(GuiGraphics pGuiGraphics, ResourceLocation pShaderLocation, int x, int y, int width, int height, float pAlpha) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, pAlpha);

        pGuiGraphics.blit(pShaderLocation, 0, 0, -90, 0.0F, 0.0F, pGuiGraphics.guiWidth(), pGuiGraphics.guiHeight(), pGuiGraphics.guiWidth(), pGuiGraphics.guiHeight());
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public Widget getSelectWidget() {
        return selectWidget;
    }

    protected WidgetData getData(){
        return widgetData;
    }

    public SwingManager getSwingDraw() {
        return swingDraw;
    }

    public SwingManager getSwingBackgroundManager() {
        return swingBackgroundManager;
    }

    public SwingManager getSwingButtonManager() {
        return swingButtonManager;
    }
}
