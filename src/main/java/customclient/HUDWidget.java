package customclient;

import com.example.wrapper.widget.ImageWrapper;
import net.minecraft.client.gui.components.ImageWidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HUDWidget {
    private static final HashMap<ImageWrapper, Integer> imageMap = new HashMap<>();
    private static final HashMap<ImageWrapper, Boolean> timeMap = new HashMap<>();

    private static final ArrayList<ImageWrapper> deleteWrapper = new ArrayList<>();
    public static void addImage(String resource, int x, int y, int width, int height, int duration){
        imageMap.put(new ImageWrapper(resource, x, y, width, height, 0.001F), duration);
    }

    public static boolean isEmpty(){
        return imageMap.isEmpty();
    }
    public static List<ImageWrapper> getImageList(){
        return imageMap.keySet().stream().toList();
    }

    public static void cooldown(){
        if(!imageMap.isEmpty()) {
            for (ImageWrapper wrapper : getImageList()) {
                if(!timeMap.containsKey(wrapper)) {
                    timeMap.put(wrapper, false);

                }
                if(!timeMap.get(wrapper)) {
                    if (wrapper.getAlpha() <= 1)
                        wrapper.setAlpha(wrapper.getAlpha() + 0.05F);
                    else if (wrapper.getAlpha() >= 1)
                        timeMap.put(wrapper, true);
                }

                imageMap.put(wrapper, imageMap.get(wrapper) - 1);
                int time = imageMap.get(wrapper);
                if (time <= 60)
                    wrapper.setAlpha(1 - (2F / time));
                if (time <= 0)
                    deleteWrapper.add(wrapper);
            }
            for (ImageWrapper deleteWrapper : deleteWrapper)
                imageMap.remove(deleteWrapper);

        }
    }
    public static int getDuration(ImageWrapper wrapper){
        return imageMap.get(wrapper);
    }
}
