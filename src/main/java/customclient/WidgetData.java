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
    private final Path screenPath;
    private Path screenButtons;
    private Path screenDrawtexture;

    private ArrayList<DrawTexture> drawTextureList = new ArrayList<>();
    private ArrayList<GuiButton> buttonList = new ArrayList<>();

    private static final Gson GSON = new GsonBuilder().create();

    WidgetData(Path screenName){
        screenPath = screenName == null ? Path.of("./") : screenName;
        screenButtons = screenPath.resolve("buttons.json");
        screenDrawtexture = screenPath.resolve("drawtexture.json");
    }

    public void init(ScreenCustom screenCustom){
        try{
            makeButtons(screenCustom.children());
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

    public Path getScreenButtons() {
        return screenButtons;
    }

    private ArrayList<GuiButton> makeButtons(List<? extends GuiEventListener> children) throws Exception{
        ArrayList<GuiButton> buttons = new ArrayList();

        if(exists(EnumData.BUTTON)) {
            String json = new String(Files.readAllBytes(screenButtons));
            buttons = GSON.fromJson(json, new TypeToken<List<GuiButton>>(){}.getType());
            if(!buttons.isEmpty()) {
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
            if(!drawTextures.isEmpty())
                drawTextureList = drawTextures;
        }
        return drawTextureList;
    }
    public void saveWidget(){
        try {
            if(!Files.exists(screenPath)) {
                Files.createFile(screenPath);
            }
            Files.writeString(screenButtons, GSON.toJson(buttonList));
            Files.writeString(screenDrawtexture, GSON.toJson(drawTextureList));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean exists(EnumData enumData){
        switch (enumData) {
            case BUTTON -> Files.exists(screenButtons);
            case TEXTURE -> Files.exists(screenDrawtexture);
        }
        throw new NullPointerException();
    }

    public void add(int i, GuiButton guiButton) {
        buttonList.add(i, guiButton);
    }


    public enum EnumData{
        BUTTON, TEXTURE, PATH
    }

}
