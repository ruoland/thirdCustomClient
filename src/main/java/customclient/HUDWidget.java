package customclient;

import com.example.wrapper.widget.ImageWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.player.inventory.Hotbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HUDWidget {
    private static final HashMap<ImageWrapper, Integer> imageMap = new HashMap<>();

    public static void addImage(String resource, int x, int y, int width, int height, int duration){
        imageMap.put(new ImageWrapper(resource, x, y, width, height, 1F), duration);

    }

    public static boolean isEmpty(){
        return imageMap.isEmpty();
    }
    public static List<ImageWrapper> getImageList(){
        return imageMap.keySet().stream().toList();
    }

}
