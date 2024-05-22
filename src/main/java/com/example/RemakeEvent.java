package com.example;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import customclient.CustomClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWDropCallback;
import org.lwjgl.glfw.GLFWDropCallbackI;
import org.lwjgl.system.NativeType;

import javax.annotation.Nullable;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.glfw.GLFW.nglfwSetDropCallback;
import static org.lwjgl.system.MemoryUtil.memAddressSafe;

public class RemakeEvent {
    private String[] whiteList = {"TitleScreen"};
    private boolean editMode;
    private AbstractWidget selectWidget;
    private GuiData guiData;
    public boolean check(Screen screen){
        for(String black : whiteList) {
            if (screen.getClass().getSimpleName().equals(black))
                return true;
        }
        return false;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void screeOpenEvent(ScreenEvent.Init.Post event){
        if(check(event.getScreen())) {
            guiData = new GuiData(event.getScreen(), event.getScreen().getClass().getSimpleName());
            guiData.updateWidget();
            for(Renderable abstractWidget :event.getScreen().renderables){

            }
        }
    }

    @SubscribeEvent
    public void screenButton(ScreenEvent.MouseDragged.Post opening){
        if(editMode){
            if(selectWidget != null){
                selectWidget.setX((int) (opening.getMouseX() + opening.getDragX()));
                selectWidget.setY((int) (opening.getMouseY() + opening.getDragY()));
                guiData.updateData();

            }
        }
    }

    @SubscribeEvent
    public void screenRender(ScreenEvent.Render.Post render){
        Minecraft mc = Minecraft.getInstance();
        GuiGraphics pGuiGraphics = render.getGuiGraphics();
        if(editMode) {
            pGuiGraphics.drawString(mc.font, Component.literal("편집 모드 실행 중"), 0, 0, 0xFFFFFF, false);
        }
    }

    @SubscribeEvent
    public void sizeEditEvent(ScreenEvent.KeyPressed.Post event){
        if(editMode && event.getKeyCode() != GLFW.GLFW_KEY_LEFT_ALT){
            if(event.getKeyCode() == GLFW.GLFW_KEY_LEFT) {
                selectWidget.setWidth(selectWidget.getWidth() - 1);
            }
            if(event.getKeyCode() == GLFW.GLFW_KEY_RIGHT) {
                selectWidget.setWidth(selectWidget.getWidth() + 1);
            }
            if(event.getKeyCode() == GLFW.GLFW_KEY_UP) {
                selectWidget.setHeight(selectWidget.getHeight() + 1);
            }
            if(event.getKeyCode() == GLFW.GLFW_KEY_DOWN) {
                selectWidget.setWidth(selectWidget.getHeight() - 1);
            }
            guiData.updateData();
        }
    }
    @SubscribeEvent
    public void editModeEvent(ScreenEvent.KeyPressed.Post event){
        if(check(event.getScreen()) && event.getKeyCode() == GLFW.GLFW_KEY_LEFT_ALT) {
            editMode = !editMode;
            if(!editMode){
                //에딧 모드 종료
                guiData.updateData();
                guiData.save();
            }
            else {
                guiData = new GuiData(event.getScreen(), event.getScreen().getClass().getSimpleName());
                guiData.updateWidget();
            }
        }
    }
    protected ResourceLocation BACKGROUND_IMAGE = new ResourceLocation(CustomClient.MODID, "textures/screenshot.png");

    @SubscribeEvent
    public void screenButton(ScreenEvent.Render.Post event){
        event.getGuiGraphics().pose().pushPose();
        event.getGuiGraphics().pose().translate(0,0,-1);

        //ScreenNewTitle.renderTexture(event.getGuiGraphics(), BACKGROUND_IMAGE, 0,0, event.getScreen().width, event.getScreen().height, 1);
        event.getGuiGraphics().pose().popPose();

    }

    @SubscribeEvent
    public void screenButton(ScreenEvent.MouseButtonPressed.Pre opening){
        if(editMode){
            if(opening.getButton() == 0) {
                for (GuiEventListener guiEventListener : opening.getScreen().children()) {
                    if (guiEventListener instanceof AbstractWidget && guiEventListener.isMouseOver(opening.getMouseX(), opening.getMouseY())) {
                        this.selectWidget = (AbstractWidget) guiEventListener;
                        opening.setCanceled(true);
                        return;
                    }
                }

            }
        }
    }

    public void reset(){
        editMode = false;
    }
}
