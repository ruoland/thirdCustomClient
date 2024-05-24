package customclient;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

import java.io.Serializable;

public class GuiButton extends Widget implements Serializable {

    @SerializedName("버튼 이름")
    private String buttonText = "";

    @SerializedName("버튼 표시")
    private boolean buttonVisible;
    @SerializedName("버튼 투명해도 클릭 가능")
    private boolean canInvisibleClick;

    public GuiButton(int id, AbstractWidget widget){
        super(widget);
        setID(id);

        this.buttonText = widget.getMessage().getString();
    }

    public void update(){
        super.update();
        setButtonText(buttonText);
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
        abstractWidget.setMessage(Component.literal(buttonText));
    }

    public void setCanInvisibleClick(boolean canInvisibleClick) {
        this.canInvisibleClick = canInvisibleClick;
    }

    public boolean isButtonVisible() {
        return buttonVisible;
    }

    public void setButtonVisible(boolean buttonVisible) {
        this.buttonVisible = buttonVisible;
    }

    public boolean canInvisibleClick() {
        return canInvisibleClick;
    }

    public boolean isMouseOver(double mouseX, double mouseY ){

        return abstractWidget.isMouseOver(mouseX, mouseY);
    }

    @Override
    public String toString() {
        return "GuiButton{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", texture='" + texture + '\'' +
                ", visible=" + visible +
                ", lock=" + lock +
                '}';
    }

    public void setAction(String text) {
    }
}


