package customclient;

import com.google.gson.annotations.SerializedName;
import customclient.swing.ButtonBucket;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.io.Serializable;

public class GuiButton extends Widget implements Serializable {
    @SerializedName("버튼 명령")
    protected ButtonBucket buttonBucket;
    @SerializedName("버튼 이름")
    private String buttonText = "";

    @SerializedName("버튼 텍스트 표시")
    private boolean textVisible;
    @SerializedName("버튼 배경 표시")
    private boolean textureVisible;

    public GuiButton(int id, AbstractWidget widget){
        super(widget);
        setID(id);
        this.buttonText = widget.getMessage().getString();
    }

    public ButtonBucket getButtonBucket() {
        return buttonBucket;
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

    public void setTextureVisible(boolean textureVisible) {
        this.textureVisible = textureVisible;
    }

    public boolean isTextureVisible() {
        return textureVisible;
    }

    public void setTextVisible(boolean textVisible) {
        this.textVisible = textVisible;
    }


    public boolean isTextVisible() {
        return textVisible;
    }

    public boolean isMouseOver(double mouseX, double mouseY ){
        return abstractWidget.isMouseOver(mouseX, mouseY);
    }

}


