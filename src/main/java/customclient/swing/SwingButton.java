package customclient.swing;

import customclient.CustomClient;
import customclient.GuiButton;
import net.minecraft.client.Minecraft;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SwingButton extends SwingComponentBase implements KeyListener {
    private SwingManager.ButtonManager buttonManager;
    //private ButtonBucket buttonBucket;

    JButton resetButton = new JButton("이미지 리셋");
    JButton visibleStringButton = new JButton("이름 표시: 켜짐");
    JButton visibleTextureButton = new JButton("이미지 표시: 켜짐");
    JTextField actionField = new JTextField(10);

    JComboBox<String> actionComboBox = new JComboBox<String>();

    public SwingButton() {
        super(true);
        Minecraft mc = Minecraft.getInstance();

        setSize(380, 250);
        setLocation(mc.getWindow().getX() - 380, mc.getWindow().getY() + 150);

    }
    public void init(SwingManager.ButtonManager buttonManager){

        this.buttonManager = buttonManager;

    }

    @Override
    public void widgetUpdate() {
        super.widgetUpdate();
        //this.buttonBucket = buttonManager.createBucket();
        setTitle(buttonManager.getWidget().getButtonText());
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
        pathField.setText(buttonManager.getWidget().getButtonText());

        setFocusableWindowState(false);
        setVisible(true);
        setFocusableWindowState(true);

        buttonTextUpdate(visibleStringButton, buttonManager.getWidget().isVisible());
        buttonTextUpdate(lockButton, buttonManager.getWidget().isLock());
    }

    public void update() {

        xField.setText(""+getSelComponent().getX());
        yField.setText(""+getSelComponent().getY());
        widthField.setText(""+getSelComponent().getWidth());
        heightField.setText(""+getSelComponent().getHeight());CustomClient.LOGGER.info("업데이트");
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

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        GuiButton guiButton = buttonManager.getWidget();
        if (e.getSource() == resetButton) {
            int i = JOptionPane.showConfirmDialog(this, "정말로 이미지를 초기화 할까요?", "이미지 초기화", JOptionPane.YES_NO_OPTION);
            if (i == JOptionPane.YES_OPTION) {
                guiButton.setTexture("minecraft:textures/gui/widgets.png");
            }
        }
        if (e.getSource() == actionComboBox) {
            JComboBox comboBox = (JComboBox) e.getSource();
            String selectItem = (String) comboBox.getSelectedItem();
            if (selectItem.equalsIgnoreCase("접속:")) {
                actionField.setVisible(true);
            } else {
                //콤보 박스는 한번에 하나만 선택 가능하니 init 메서드를 사용함
            }
        }

    }

    @Override
    public void dispose() {
        super.dispose();
        StringBuffer comboBox = new StringBuffer((String) actionComboBox.getSelectedItem());

        if (comboBox.toString().equals("접속:")) {
            comboBox.append(actionField.getText());
        }

    }
}