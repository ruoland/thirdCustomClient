package com.example.swing.base;

import com.example.wrapper.widget.WidgetWrapper;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.Consumer;

public class SwingWidgetBase extends JFrame implements ICustomSwing, KeyListener, ActionListener, DocumentListener {
        protected JTextField nameField = new JTextField(20);
        protected JTextField actionField = new JTextField(20);
        protected JTextField xField = new JTextField(4);
        protected JTextField yField = new JTextField(4);
        protected JTextField widthField = new JTextField(4);
        protected JTextField heightField = new JTextField(4);
        protected JButton visibleButton = new JButton("버튼 표시: 켜짐");
        protected String visibleText = "버튼 표시: ";
        protected JComboBox<String> actionComboBox;
        private static final Logger logger = LoggerFactory.getLogger(SwingWidgetBase.class);
        private boolean isInit = false;
        protected WidgetWrapper widgetWrapper;
        public SwingWidgetBase(WidgetWrapper widgetWrapper, String title, boolean isNameText, boolean isAction, boolean positionField, boolean sizeField, boolean visibleButton) {
            setTitle(title);
            setSize(500, 200);
            Window window = Minecraft.getInstance().getWindow();
            setLocation(window.getX() - 400, window.getY());
            setLayout(new FlowLayout(FlowLayout.LEADING));
            this.widgetWrapper = widgetWrapper;
            if(isNameText) {
                nameField.addKeyListener(this);
                nameField.setText(widgetWrapper.getMessage());
                nameField.getDocument().addDocumentListener(this);
                add(nameField);
            }
            if(isAction)
            {
                actionField.setText(widgetWrapper.getAction());
            }
            if(positionField) {
                xField.addKeyListener(this);
                xField.setText(widgetWrapper.getX() + "");
                yField.addKeyListener(this);
                yField.setText(widgetWrapper.getY() + "");
                xField.getDocument().addDocumentListener(this);
                yField.getDocument().addDocumentListener(this);
                add(xField);
                add(yField);
            }
            if(sizeField) {
                widthField.addKeyListener(this);
                widthField.setText(widgetWrapper.getWidth() + "");
                heightField.addKeyListener(this);
                heightField.setText(widgetWrapper.getHeight() + "");
                widthField.getDocument().addDocumentListener(this);
                heightField.getDocument().addDocumentListener(this);
                add(widthField);
                add(heightField);
            }
            if(visibleButton) {
                this.visibleButton.addActionListener(this);
                add(this.visibleButton);
            }

            if(isAction) {
                this.actionComboBox = new JComboBox<String>();
                this.actionField.addKeyListener(this);
                this.actionComboBox.addActionListener(this);
                this.actionField.getDocument().addDocumentListener(this);

                add(actionComboBox);
                add(this.actionField);
                comboBoxAddItem();
            }

            setFocusableWindowState(false);
            setVisible(true);
            setFocusableWindowState(true);

        }

    @Override
    public void init() {
        logger.info("초기화 진행");

        xField.setText(""+ widgetWrapper.getX());
        yField.setText(""+ widgetWrapper.getY());
        widthField.setText(""+ widgetWrapper.getWidth());
        heightField.setText(""+ widgetWrapper.getHeight());

        if(actionComboBox != null) {
            logger.info("액션 정보 : {}, 값 정보: {} ", widgetWrapper.getAction(), widgetWrapper.getValue());
            if (widgetWrapper.getAction() != null && widgetWrapper.getAction().equals("명령어")) {
                actionComboBox.setSelectedItem("명령어");
                actionField.setText(widgetWrapper.getValue());
                logger.info("초기화 : 명령어 : {},{} ", widgetWrapper.getAction(), widgetWrapper.getValue());

            } else
                actionComboBox.setSelectedItem(widgetWrapper.getAction() + ":" + widgetWrapper.getValue());
        }
        isInit = true;
    }

    public void comboBoxAddItem() {
            actionComboBox.addItem("선택안함");
            actionComboBox.addItem("열기:맵 선택");
            actionComboBox.addItem("열기:멀티");
            actionComboBox.addItem("열기:설정");
            actionComboBox.addItem("열기:모드");
            actionComboBox.addItem("열기:렐름");
            actionComboBox.addItem("열기:언어");
            actionComboBox.addItem("열기:접근성");
            actionComboBox.addItem("접속:");
            actionComboBox.addItem("종료:종료");
            if(Minecraft.getInstance().player != null)
                actionComboBox.addItem("명령어");
        }


        @Override
        public void keyTyped(KeyEvent e) {

        }
        
        @Override
        public void keyPressed(KeyEvent e) {

        }


        @Override
        public void keyReleased(KeyEvent e) {

        }

        public void update(){
            xField.setText(""+ widgetWrapper.getX());
            yField.setText(""+ widgetWrapper.getY());
            widthField.setText(""+ widgetWrapper.getWidth());
            heightField.setText(""+ widgetWrapper.getHeight());

            if(actionComboBox != null && widgetWrapper.getAction() != null){
                if(widgetWrapper.getAction().equals("명령어"))
                {
                    actionComboBox.setSelectedItem("명령어");
                    actionField.setText(widgetWrapper.getValue());
                }
                else
                    actionComboBox.setSelectedItem(widgetWrapper.getAction() +":"+widgetWrapper.getValue());
                logger.info("액션 콤보 박스 업데이트 됨 위젯 액션: {}, 스윙 액션: {}, 위젯 값: {}, 스윙 값: {} ", widgetWrapper.getAction(), actionComboBox.getSelectedItem(), widgetWrapper.getValue(), actionField.getText());
            }
           
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(isInit) {
                if (e.getSource() == visibleButton) {
                    widgetWrapper.setVisible(!widgetWrapper.isVisible());
                    StringBuilder sb = new StringBuilder(visibleText);
                    sb.append(widgetWrapper.isVisible() ? ": 켜짐" : ": 꺼짐");
                    visibleButton.setText(sb.toString());

                }

                if (e.getSource() == actionComboBox) {
                    logger.info("이벤트 발동됨, 현재 선택된 액션: {}, 액션 필드: {}", actionComboBox.getSelectedItem(), actionField.getText());

                    if (actionComboBox.getSelectedItem().equals("접속:")) {
                        if (actionField.getText() == null || actionField.getText().equals(""))
                            actionField.setText("127.0.0.1");
                    }

                    widgetWrapper.setAction(actionComboBox.getSelectedItem().toString());

                    if(actionComboBox.getSelectedItem().toString().contains(":") || actionComboBox.getSelectedItem().toString().equals("명령어"))
                        widgetWrapper.setValue(actionField.getText());
                }
            }
        }
        @Override
        public void dispose() {
            super.dispose();

        }
    private void setIntegerFieldIfNotEmpty(JTextField field, Consumer<Integer> setter) {
        if (!field.getText().isEmpty()) {
            setter.accept(Integer.parseInt(field.getText()));
        }
    }


    /**
     *  swing에 키보드로 뭔가 입력이 된다면 업데이트됨
     */
    public void dataUpdate(){
        // 사용 예:
        if(isInit) {
            if (isFocused()) {
                setIntegerFieldIfNotEmpty(xField, widgetWrapper::setX);
                setIntegerFieldIfNotEmpty(yField, widgetWrapper::setY);
                setIntegerFieldIfNotEmpty(widthField, widgetWrapper::setWidth);
                setIntegerFieldIfNotEmpty(heightField, widgetWrapper::setHeight);
                if (!nameField.getText().equals(""))
                    widgetWrapper.setMessage(nameField.getText());

                if (!actionField.getText().isEmpty()) {
                    if (actionComboBox.getSelectedItem().equals("접속:")) {
                        widgetWrapper.setAction(actionComboBox.getSelectedItem().toString());
                        widgetWrapper.setValue(actionField.getText());
                    }
                    if (actionComboBox.getSelectedItem().equals("명령어")) {
                        widgetWrapper.setAction(actionComboBox.getSelectedItem().toString());
                        widgetWrapper.setValue(actionField.getText());
                    }
                    logger.info("액션 업데이트 됨 액션: {}, 값: {}", actionComboBox.getSelectedItem(), actionField.getText());
                }
            }
        }
    }
    @Override
    public void insertUpdate(DocumentEvent e) {
        dataUpdate();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        dataUpdate();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

        dataUpdate();
    }
}