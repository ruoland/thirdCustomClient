package customclient;

import customclient.swing.ButtonBucket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;

import java.nio.file.Paths;

public class GuiMainmenu  extends ScreenCustom {

    GuiButton buttonSingleplayer ;
    GuiButton buttonMultiplayer;
    GuiButton buttonRelams;
    GuiButton buttonMods;
    GuiButton buttonLanguage;
    GuiButton buttonOptions;
    GuiButton buttonQuitGame;
    GuiButton buttonAccesssibility;


    public GuiMainmenu(){
        super("mainmenu.json");
    }

    @Override
    public void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.pose().pushPose();
        renderTexture(pGuiGraphics, BACKGROUND_IMAGE, 0, 0, width, height, 1);
        pGuiGraphics.pose().popPose();
    }
    @Override
    protected void renderPanorama(GuiGraphics pGuiGraphics, float pPartialTick) {
        //super.renderPanorama(pGuiGraphics, pPartialTick);
    }

    public void renderTransparentBackground(GuiGraphics pGuiGraphics) {
        pGuiGraphics.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
    }
    public void addButton(){
        CustomClient.LOGGER.info("실행");
        getData().add(0, buttonSingleplayer= new GuiButton(0,(AbstractWidget) children().get(0)));
        getData().add(0, buttonSingleplayer= new GuiButton(0,(AbstractWidget) children().get(0)));
        getData().add(1, buttonMultiplayer = new GuiButton(1,(AbstractWidget) children().get(1)));
        getData().add(2, buttonRelams = new GuiButton(2,(AbstractWidget) children().get(2)));
        getData().add(3, buttonMods = new GuiButton(3,(AbstractWidget) children().get(3)));
        getData().add(4, buttonLanguage = new GuiButton(4,(AbstractWidget) children().get(4)));
        getData().add(5, buttonOptions= new GuiButton(5,(AbstractWidget) children().get(5)));
        getData().add(6, buttonQuitGame = new GuiButton(6,(AbstractWidget) children().get(6)));
        getData().add(7, buttonAccesssibility = new GuiButton(7,(AbstractWidget) children().get(7)));
        for(int i = 0; i < getData().getButtonList().size();i++){
            GuiButton button = getData().getButtonList().get(i);
            button.setID(i);
            button.setWidth(button.getWidth());
            button.setHeight(button.getHeight());
            button.setPosition(button.getX(), button.getY());
            button.setVisible(button.isVisible());
            button.setAlpha(1);
            button.buttonBucket = new ButtonBucket(button.id);
        }
    }

    @Override
    public void actionPerformed(int buttonID, double mouseX, double mouseY, int pButton) {
        super.actionPerformed(buttonID, mouseX, mouseY, buttonID);

    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }



}
