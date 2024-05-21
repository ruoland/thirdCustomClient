package customclient.swing;

import customclient.CustomClient;
import customclient.GuiButton;
import customclient.ScreenCustom;
import customclient.Widget;
import net.minecraft.client.Minecraft;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SwingButton extends SwingComponentBase implements KeyListener {
    private GuiButton buttonWidget;
    private ButtonBucket buttonBucket;
    public int buttonID;
    JButton resetButton = new JButton("이미지 리셋");
    JButton visibleStringButton = new JButton("이름 표시: 켜짐");
    JButton visibleTextureButton = new JButton("이미지 표시: 켜짐");
    JTextField actionField = new JTextField(10);

    JComboBox<String> actionComboBox = new JComboBox<String>();
    
    public SwingButton(ScreenCustom base, GuiButton button) {
        super(base, button);
        Minecraft mc = Minecraft.getInstance();
        this.buttonWidget = button;
        buttonID = button.getID();
        widget =buttonWidget;
        this.buttonBucket = button.getButtonBucket();
        setTitle(button.getButtonText());
        setSize(380, 250);
        setLocation(mc.getWindow().getX() - 380, mc.getWindow().getY() + 150);
        comboBoxAddItem();
        visibleStringButton.addActionListener(this);

        visibleTextureButton.addActionListener(this);
        resetButton.addActionListener(this);
        actionComboBox.addActionListener(this);
        actionField.addKeyListener(this);
        add(visibleStringButton);
        add(visibleTextureButton);
        add(resetButton);
        add(actionComboBox);
        add(actionField);
        pathField.setText(button.getButtonText());
        String type = buttonBucket.getType(0);
        String script = buttonBucket.getActionScript(0);
        if (type.equals("접속:")) {
            actionField.setVisible(true);
            actionComboBox.setSelectedItem(type);
            actionField.setText(script.replace(type, ""));
        } else
            actionComboBox.setSelectedItem(type + script);

        setFocusableWindowState(false);
        setVisible(true);
        setFocusableWindowState(true);

        buttonTextUpdate(visibleStringButton, button.isVisible());
        buttonTextUpdate(visibleTextureButton, button.isTextVisible());
        buttonTextUpdate(lockButton, button.isLock());
    }

    public void comboBoxAddItem() {
        actionComboBox.addItem("선택안함");
        actionComboBox.addItem("열기:맵 선택");
        actionComboBox.addItem("열기:멀티");
        actionComboBox.addItem("열기:설정");
        actionComboBox.addItem("열기:모드");
        actionComboBox.addItem("열기:렐름");
        actionComboBox.addItem("접속:");
        actionComboBox.addItem("종료");
    }


    @Override
    public void keyTyped(KeyEvent e) {
        super.keyTyped(e);


    }


    @Override
    public GuiButton getSelComponent() {
        return (GuiButton) super.getSelComponent();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (e.getSource() == resetButton) {
            int i = JOptionPane.showConfirmDialog(this, "정말로 이미지를 초기화 할까요?", "이미지 초기화", JOptionPane.YES_NO_OPTION);
            if (i == JOptionPane.YES_OPTION) {
                getSelComponent().setTexture("minecraft:textures/gui/widgets.png");
            }
        }
        if (e.getSource() == actionComboBox) {
            JComboBox comboBox = (JComboBox) e.getSource();
            String selectItem = (String) comboBox.getSelectedItem();
            if (selectItem.equalsIgnoreCase("접속:")) {
                actionField.setVisible(true);
                actionField.setText(getSelComponent().getButtonBucket().getActionScript(0).replace("접속:", ""));
            } else {
                //콤보 박스는 한번에 하나만 선택 가능하니 init 메서드를 사용함
                getSelComponent().getButtonBucket().initScript(selectItem);
            }
        }
        if (e.getSource() == visibleTextureButton) {
            getSelComponent().setVisible(!getSelComponent().isTextureVisible());
            visibleTextureButton.setText(getSelComponent().isTextureVisible() ? "이미지 표시: 켜짐" : "이미지 표시: 꺼짐");
        }
        if (e.getSource() == visibleStringButton) {
            ((GuiButton) customBase.getSelectWidget()).setTextVisible(!getSelComponent().isTextVisible());
            visibleStringButton.setText(((GuiButton) customBase.getSelectWidget()).isTextVisible() ? "이름 표시: 켜짐" : "이름 표시: 꺼짐");
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        StringBuffer comboBox = new StringBuffer((String) actionComboBox.getSelectedItem());

        if (comboBox.toString().equals("접속:")) {
            comboBox.append(actionField.getText());
        }

        getSelComponent().getButtonBucket().initScript(comboBox.toString());
    }
}