package customclient;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import customclient.swing.ButtonBucket;
import customclient.swing.ButtonFunction;
import customclient.swing.SwingButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ScreenCustom extends TitleScreen {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    protected ResourceLocation BACKGROUND_IMAGE = new ResourceLocation(CustomClient.MODID, "textures/screenshot.png");

    protected ButtonFunction buttonFunction;

    protected ArrayList<DrawTexture> drawTextureList = new ArrayList<>();
    protected ArrayList<GuiButton> buttonList = new ArrayList<>();

    protected SwingButton currentSwingButton;

    protected boolean editMode = false;

    protected Path screenPath = Path.of("./");
    protected Path screenButtons = screenPath.resolve("buttons.json");
    protected Path screenDrawtexture = screenPath.resolve("drawtexture.json");

    protected Widget selectWidget, lastSelectWidget;

    @Override
    protected void init() {
        buttonFunction = new ButtonFunction(this);
        super.init();
        System.setProperty("java.awt.headless", "false");
    }

    public abstract void addButton();

    public void changeEditMode(){
        editMode = !editMode;
        if(currentSwingButton != null) {

            currentSwingButton.dispose();
            currentSwingButton = null;
        }
        selectWidget = null;
        saveWidget();
    }

    public void postInit(){
        if(buttonList.isEmpty()) {
            if (!Files.exists(screenButtons)) {
                addButton();
            } else {
                loadWidget();
                for (int i = 0; i < buttonList.size(); i++) {
                    GuiButton button = buttonList.get(i);
                    button.setAbstractWidget((AbstractWidget) children().get(i));
                    button.setID(i);
                    button.update();
                    button.buttonBucket = new ButtonBucket(button);
                }
                for(int i = 0; i < drawTextureList.size();i++){
                    DrawTexture drawTexture = drawTextureList.get(i);
                    drawTexture.setAbstractWidget(new FakeTextureWidget());
                    drawTexture.setID(i);
                    drawTexture.update();
                }
            }
            if (buttonList.isEmpty())
                throw new NullPointerException("예상 외 결과");
        }
    }

    public boolean loadWidget(){
        try {
            if(Files.exists(screenButtons)) {
                String json = new String(Files.readAllBytes(screenButtons));
                ArrayList buttons = new Gson().fromJson(json, new TypeToken<List<GuiButton>>(){}.getType());
                if(!buttons.isEmpty())
                    buttonList = buttons;
            }
            if(Files.exists(screenDrawtexture)) {
                String json = new String(Files.readAllBytes(screenDrawtexture));
                ArrayList drawTextures = new Gson().fromJson(json, new TypeToken<List<DrawTexture>>(){}.getType());
                if(!drawTextures.isEmpty())
                    drawTextureList = drawTextures;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    public void saveWidget(){
        try {
            CustomClient.LOGGER.info("저장");
            if(!Files.exists(screenPath)) {
                Files.createFile(screenPath);
            }
            Files.writeString(screenButtons, GSON.toJson(buttonList));
            Files.writeString(screenDrawtexture, GSON.toJson(drawTextureList));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        for(DrawTexture drawTexture : drawTextureList){
            renderTexture(pGuiGraphics, drawTexture.getTexture(), drawTexture.getX(), drawTexture.getY(), drawTexture.getWidth(), drawTexture.getHeight(), drawTexture.getAlpha());
        }
    }

    public void renderTexture(GuiGraphics pGuiGraphics, ResourceLocation pShaderLocation, int x, int y, int width, int height, float pAlpha) {
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

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if(editMode){
            for(GuiButton button : buttonList){
                if(button.isMouseOver(pMouseX, pMouseY)) {
                    selectWidget = button;

                    if(pButton == 0 && !button.isLock()) {
                        if (currentSwingButton != null) {
                            if (currentSwingButton.getSelComponent() == button) {
                                return false;
                            }
                            else {
                                currentSwingButton.dispose();
                                currentSwingButton = null;
                            }
                        }
                        currentSwingButton = new SwingButton(this, (GuiButton) selectWidget);
                        return true;
                    }
                    else if(pButton == 1){
                        selectWidget.setLock(false);
                    }
                }
            }
            for(DrawTexture texture : drawTextureList){
                if(pButton == 0 && texture.isMouseOver(pMouseX, pMouseY) && !texture.isLock()) {
                    selectWidget = texture;
                    return true;
                }
            }
            selectWidget = null;
            return false;
        }
        else{
            for(GuiButton button : buttonList){
                if(button.isMouseOver(pMouseX, pMouseY)) {
                    actionPerformed(button.getID(), pMouseX, pMouseY, pButton);
                    return false;
                }
            }
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    public void actionPerformed(int buttonID, double mouseX, double mouseY, int pButton){
        buttonFunction.runScript(buttonList.get(buttonID));
    }
    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        super.mouseMoved(pMouseX, pMouseY);
        if(editMode) {
            if(isSelect()){
                int mouseX = (int) pMouseX;
                int mouseY = (int) pMouseY;
                selectWidget.setPosition(mouseX, mouseY);
                currentSwingButton.update();
            }
        }
    }

    public boolean isSelect(){
        return selectWidget != null;
    }


    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {

        if(pKeyCode == 342) {
            changeEditMode();
        }
        if(editMode) {
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
    public void onClose() {
        saveWidget();
        super.onClose();
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
                drawTextureList.add(new DrawTexture(fileName, resourceLocation));
            }
        }
    }

    public ResourceLocation getTexture(Path dropFile){
        Path toPath = Paths.get("D:\\Projects\\thirdCustomClient\\src\\main\\resources\\assets\\customclient/"+dropFile.getFileName().toString().replace(" ","_"));
        try {
            if(!Files.exists(toPath))
                Files.copy(dropFile, toPath);

            NativeImage nativeImage = NativeImage.read(new FileInputStream(toPath.toString()));
            DynamicTexture dynamicTexture = new DynamicTexture(nativeImage);
            return getMinecraft().getTextureManager().register("customclient", dynamicTexture);
        }
        catch (IOException e){
            JOptionPane.showMessageDialog(null, "PNG 파일만 인식합니다!");
        }
        return null;
    }

    public Widget getSelectWidget() {
        return selectWidget;
    }

    public Widget getLastSelectWidget() {
        return lastSelectWidget;
    }
}
