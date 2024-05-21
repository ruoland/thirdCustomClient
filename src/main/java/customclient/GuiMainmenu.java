package customclient;

import customclient.swing.ButtonBucket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;

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
        super();

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
        buttonList.add(0, buttonSingleplayer= new GuiButton(0,(AbstractWidget) children().get(0)));
        buttonList.add(1, buttonMultiplayer = new GuiButton(1,(AbstractWidget) children().get(1)));
        buttonList.add(2, buttonRelams = new GuiButton(2,(AbstractWidget) children().get(2)));
        buttonList.add(3, buttonMods = new GuiButton(3,(AbstractWidget) children().get(3)));
        buttonList.add(4, buttonLanguage = new GuiButton(4,(AbstractWidget) children().get(4)));
        buttonList.add(5, buttonOptions= new GuiButton(5,(AbstractWidget) children().get(5)));
        buttonList.add(6, buttonQuitGame = new GuiButton(6,(AbstractWidget) children().get(6)));
        buttonList.add(7, buttonAccesssibility = new GuiButton(7,(AbstractWidget) children().get(7)));
        for(int i = 0; i < buttonList.size();i++){
            GuiButton button = buttonList.get(i);
            button.setID(i);
            button.setWidth(button.getWidth());
            button.setHeight(button.getHeight());
            button.setPosition(button.getX(), button.getY());
            button.setVisible(button.isVisible());
            button.setAlpha(1);
            button.buttonBucket = new ButtonBucket(button);
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
