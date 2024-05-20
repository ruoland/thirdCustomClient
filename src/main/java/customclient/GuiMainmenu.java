package customclient;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.references.Blocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.PumpkinBlock;
import net.neoforged.neoforge.client.gui.LoadingErrorScreen;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.List;

public class GuiMainmenu  extends ScreenCustom {
    private static ResourceLocation BACKGROUND_IMAGE = new ResourceLocation(CustomClient.MODID, "textures/screenshot.png");
    GuiButton buttonSingleplayer ;
    GuiButton buttonMultiplayer;
    GuiButton buttonRelams;
    GuiButton buttonMods;
    GuiButton buttonLanguage;
    GuiButton buttonOptions;
    GuiButton buttonQuitGame;
    GuiButton buttonAccesssibility;
    private static ResourceLocation resourceLocation;
    public GuiMainmenu(){

    }
    @Override
    protected void init() {
        super.init();
        try {

            NativeImage nativeImage = NativeImage.read(GuiMainmenu.class.getResourceAsStream("/assets/customclient/textures/screenshot.png"));
            DynamicTexture dynamicTexture = new DynamicTexture(nativeImage);
            resourceLocation = getMinecraft().getTextureManager().register("test.png", dynamicTexture);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }


    @Override
    public void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.pose().pushPose();;

        renderTexture(pGuiGraphics, resourceLocation, 1);

        pGuiGraphics.pose().popPose();
    }
    @Override
    protected void renderPanorama(GuiGraphics pGuiGraphics, float pPartialTick) {
        //super.renderPanorama(pGuiGraphics, pPartialTick);
    }


    @Override
    public void onFilesDrop(List<Path> pPacks) {
        super.onFilesDrop(pPacks);
        CustomClient.LOGGER.info(pPacks+"");
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
    }

    @Override
    public void actionPerformed(int buttonID, double mouseX, double mouseY, int pButton) {

    }
}
