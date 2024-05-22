package customclient;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import customclient.swing.ButtonBucket;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WidgetData {
    private final ScreenCustom widgetScreen;
    private final Path screenPath;
    private Path screenButtons;
    private Path screenDrawtexture;

    private ArrayList<DrawTexture> drawTextureList = new ArrayList<>();
    private ArrayList<GuiButton> buttonList = new ArrayList<>();

    private static final Gson GSON = new GsonBuilder().create();

    WidgetData(ScreenCustom screenCustom, Path screenName){
        widgetScreen = screenCustom;
        screenPath = screenName == null ? Path.of("./") : screenName;
        screenButtons = screenPath.resolve("buttons.json");
        screenDrawtexture = screenPath.resolve("drawtexture.json");
    }

    /**
     * GUI가 완전히 불러온 이후 실행됨.
     */
    public void widgetLoad(){
        try{
            CustomClient.LOGGER.info(screenPath.toString());
            if(!Files.isDirectory(screenPath)) {
                Files.createDirectories(screenPath);
                Files.createFile(screenButtons);
                Files.createFile(screenDrawtexture);
            }
            makeButtons(widgetScreen.children());
            makeDrawTexture();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<GuiButton> getButtonList() {
        return buttonList;
    }

    public ArrayList<DrawTexture> getDrawTextureList() {
        return drawTextureList;
    }

    private ArrayList<GuiButton> makeButtons(List<? extends GuiEventListener> children) throws Exception{
        ArrayList<GuiButton> buttons = new ArrayList();

        if(exists(EnumData.BUTTON)) {
            String json = new String(Files.readAllBytes(screenButtons));
            buttons = GSON.fromJson(json, new TypeToken<List<GuiButton>>(){}.getType());
            if(buttons != null) {
                buttonList = buttons;
                return buttons;
            }
        }

        for (int i = 0; i < buttonList.size(); i++) {
            GuiButton button = buttonList.get(i);
            button.setAbstractWidget((AbstractWidget) children.get(i));
            button.setID(i);
            button.buttonBucket = new ButtonBucket(button.id);
        }
        return buttons;
    }
    private ArrayList<DrawTexture> makeDrawTexture() throws Exception{
        if(Files.exists(screenDrawtexture)) {
            String json = new String(Files.readAllBytes(screenDrawtexture));
            ArrayList<DrawTexture> drawTextures = new Gson().fromJson(json, new TypeToken<List<DrawTexture>>(){}.getType());
            if(drawTextures != null)
                drawTextureList = drawTextures;
        }
        return drawTextureList;
    }
    public void saveWidget(){
        try {
            Files.writeString(screenButtons, GSON.toJson(buttonList));
            Files.writeString(screenDrawtexture, GSON.toJson(drawTextureList));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean exists(EnumData enumData){
        switch (enumData) {
            case BUTTON:
                return Files.exists(screenButtons);

            case TEXTURE:
                return Files.exists(screenDrawtexture);
        }
        return false;
    }

    public void add(int i, GuiButton guiButton) {
        buttonList.add(i, guiButton);
    }


    public enum EnumData{
        BUTTON, TEXTURE, PATH
    }

}
