package customclient;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class ScreenCustom extends TitleScreen {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    protected ArrayList<GuiButton> buttonList = new ArrayList<>();
    private boolean editMode = false;
    protected Path screenPath = Path.of("./mainmenu.json");;
    private int selectGui;
    @Override
    protected void init() {
        super.init();
    }

    public abstract void addButton();

    public void changeEditMode(){
        editMode = !editMode;
        selectGui = -1;
        saveWidget();
    }
    public void postInit(){
        if(!Files.exists(screenPath)) {
            addButton();
        }
        else{
            loadWidget();
            for (int i = 0; i < buttonList.size(); i++) {
                GuiButton button = buttonList.get(i);
                button.setAbstractWidget((AbstractWidget) children().get(i));
                button.update();
            }
        }
        if(buttonList.size() == 0)
            throw new NullPointerException("예상 외 결과");
    }

    public boolean loadWidget(){
        try {
            if(Files.exists(screenPath)) {
                String json = new String(Files.readAllBytes(screenPath));

                ArrayList buttons = new Gson().fromJson(json, new TypeToken<List<GuiButton>>(){}.getType());

                if(buttons.isEmpty())
                    return false;
                else
                    buttonList = buttons;
                return true;
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
            Files.writeString(screenPath, GSON.toJson(buttonList));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        if(editMode){
            pGuiGraphics.drawString(font, Component.literal("편집 모드 실행 중"),0, 0,0xFFFFFF, false);
        }
    }
    public void renderTexture(GuiGraphics pGuiGraphics, ResourceLocation pShaderLocation, float pAlpha) {
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
            CustomClient.LOGGER.info(buttonList+"="+pButton);
            for(GuiButton button : buttonList){
                CustomClient.LOGGER.info(button.buttonText + button.isMouseOver(pMouseX, pMouseY));
                if(pButton == 0 && button.isMouseOver(pMouseX, pMouseY)) {
                    selectGui = button.buttonID;
                    return true;
                }
            }
            selectGui = -1;
            return false;
        }
        else{
            for(GuiButton button : buttonList){
                if(button.isMouseOver(pMouseX, pMouseY))
                    actionPerformed(button.buttonID, pMouseX, pMouseY, pButton);
            }
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    public void actionPerformed(int buttonID, double mouseX, double mouseY, int pButton){

    }
    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        super.mouseMoved(pMouseX, pMouseY);
        if(editMode) {
            if(isSelect()){
                int mouseX = (int) pMouseX;
                int mouseY = (int) pMouseY;
                buttonList.get(selectGui).setPosition(mouseX, mouseY);
                CustomClient.LOGGER.info("aaa");
            }
        }
    }

    public boolean isSelect(){
        return selectGui != -1;
    }


    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        CustomClient.LOGGER.info(pKeyCode+"");
        if(pKeyCode == 342) {
            changeEditMode();
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public void onClose() {
        saveWidget();
        CustomClient.LOGGER.info("꺼짐");
        super.onClose();


    }
}
