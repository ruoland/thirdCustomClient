package customclient.swing;


import customclient.DrawTexture;
import customclient.GuiButton;
import customclient.ScreenCustom;
import customclient.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.regex.Pattern;

public abstract class SwingComponentBase extends JFrame implements KeyListener, ActionListener {

    protected Widget widget;
    private SwingManager swingManager;
    JTextField pathField = new JTextField(10);
    JTextField xField = new JTextField(3);
    JTextField yField = new JTextField(3);
    JTextField widthField = new JTextField(3);
    JTextField heightField = new JTextField(3);
    JTextField alphaField = new JTextField(3);

    JButton lockButton = new JButton("이동 잠금 : 풀림");
    JButton fileSelectButton = new JButton("이미지 선택");
    JButton removeButton = new JButton("삭제");
    private boolean canDefaultValue = false;

    public SwingComponentBase(boolean addDefault) {
        this.canDefaultValue = addDefault;
        Minecraft mc = Minecraft.getInstance();
        setSize(400, 200);
        setLocation(mc.getWindow().getX() - 400, mc.getWindow().getY() + 300);
        setLayout(new FlowLayout(FlowLayout.LEADING));

    }

    public void init(SwingManager swingManager){

    }

    public void widgetUpdate(){
        widget = swingManager.getWidget();
        if (canDefaultValue) {
            xField.setText(String.valueOf(widget.getX()));
            yField.setText(String.valueOf(widget.getY()));
            widthField.setText(String.valueOf(widget.getWidth()));
            heightField.setText(String.valueOf(widget.getHeight()));
            if (widget instanceof DrawTexture) {
                alphaField.setText(String.valueOf((widget).getAlpha()));
                alphaField.setToolTipText("투명도(1~0 사이 값)");
            }

            pathField.addKeyListener(this);
            xField.addKeyListener(this);
            yField.addKeyListener(this);
            widthField.addKeyListener(this);
            alphaField.addKeyListener(this);
            heightField.addKeyListener(this);
            lockButton.addActionListener(this);
            fileSelectButton.addActionListener(this);
            removeButton.addActionListener(this);

            add(pathField);
            add(xField);
            add(yField);
            add(widthField);
            add(heightField);
            add(alphaField);
            add(fileSelectButton);
            add(lockButton);
            add(removeButton);
        }
    }
    public void buttonTextUpdate(JButton button, boolean onoff){
        String text = button.getText();
        String buttonStr = "";
        if(text.contains("켜짐") || text.contains("꺼짐")){
            buttonStr = onoff ? ": 켜짐" : ": 꺼짐";
            text = text.replace(": 켜짐", buttonStr).replace(": 꺼짐", buttonStr);
        }
        if(text.contains("잠금") || text.contains("풀림")){
            buttonStr = onoff ? ": 잠금" : ": 풀림";
            text = text.replace(": 잠금", buttonStr).replace(": 풀림", buttonStr);
        }
        button.setText(text);

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    protected Widget getSelComponent(){
        return widget;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == removeButton)
        {
            int i = JOptionPane.showConfirmDialog(this, "정말로 삭제하시겠습니까", "삭제하기", JOptionPane.YES_NO_OPTION);
            if(i == JOptionPane.YES_OPTION){
                getSelComponent().setVisible(false);
                getSelComponent().remove();
                dispose();
            }
        }

        if (e.getSource() == lockButton) {
            getSelComponent().setLock(!getSelComponent().isLock());
            lockButton.setText(getSelComponent().isLock() ? "이동 잠금 : 잠금" : "이동 잠금 : 풀림");
        }
        if (e.getSource() == fileSelectButton) {
            JFileChooser jFileChooser = new JFileChooser();
            int select = jFileChooser.showOpenDialog(null);
            if(select == 0) {
                swingManager.getWidget().setTexture(ScreenCustom.getTexture(jFileChooser.getSelectedFile().toPath()).toString());
            }
        }

    }
    @Override
    public void keyReleased(KeyEvent e) {
        JTextField textField = (JTextField) e.getSource();
        if (!textField.getText().isEmpty()) {
            if (textField == pathField) {
                if (swingManager.getWidget() instanceof DrawTexture drawTexture)
                    drawTexture.setTexture(new ResourceLocation(textField.getText()));
                else if(swingManager.getWidget() instanceof GuiButton guiButton)
                    guiButton.setButtonText(textField.getText());
            }
            if (textField == xField) {
                widget.setX(Integer.parseInt(textField.getText()));
            }
            if (textField == yField) {
                widget.setY(Integer.parseInt(textField.getText()));
            }
            if (textField == widthField) {
                widget.setWidth(Integer.parseInt(textField.getText()));
            }
            if (textField == heightField) {
               widget.setHeight(Integer.parseInt(textField.getText()));
            }
            if (textField == alphaField) {
                widget.setAlpha(Float.parseFloat(textField.getText()));
            }
        }
    }

    public void sizeUpdate(int width, int height){
        if(!isFocused()) {
            widthField.setText(String.valueOf(width));
            heightField.setText(String.valueOf(height));
        }
    }
    public void positionUpdate(int mouseX, int mouseY){
        if(!isFocused()) {
            xField.setText(String.valueOf(mouseX));
            yField.setText(String.valueOf(mouseY));
        }
    }
}