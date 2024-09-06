package customclient;

import com.example.wrapper.widget.ImageWrapper;
import com.example.wrapper.widget.StringWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.player.inventory.Hotbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HUDWidget {
    private static final HashMap<Integer, ImageWrapper> imageMap = new HashMap<>();
    private static final HashMap<Integer, StringWrapper> stringMap = new HashMap<>();

    public static void addString(int id, String message, String customFont, int x, int y, int width, int height, int duration){
        stringMap.put(id, new StringWrapper(message, customFont, x,y,width, height));
    }
    public static ImageWrapper addImage(int id, String resource, int x, int y, int width, int height){
        imageMap.put(id, new ImageWrapper(resource, x, y, width, height, 1F));
        return imageMap.get(id);
    }

    public static boolean isEmpty(){
        return imageMap.isEmpty();
    }
    public static List<ImageWrapper> getImageList(){
        return imageMap.values().stream().toList();
    }

}
