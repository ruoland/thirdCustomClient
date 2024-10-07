    package com.example.swing.base;

    import com.example.wrapper.widget.WidgetWrapper;
    import com.mojang.blaze3d.platform.Window;
    import net.minecraft.client.Minecraft;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

    import javax.swing.*;
    import javax.swing.event.DocumentEvent;
    import javax.swing.event.DocumentListener;
    import javax.swing.text.NumberFormatter;
    import java.awt.*;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.awt.event.KeyEvent;
    import java.awt.event.KeyListener;
    import java.util.function.Consumer;

    public class SwingWidgetBase extends JFrame implements ICustomSwing, KeyListener, ActionListener, DocumentListener {
        protected JFormattedTextField idField;
        protected JTextField nameField = new JTextField(20);
        protected JTextField actionField = new JTextField(20);
        protected JTextField xField = new JTextField(4);
        protected JTextField yField = new JTextField(4);
        protected JTextField zField = new JTextField(4);
        protected JTextField widthField = new JTextField(4);
        protected JTextField heightField = new JTextField(4);
        protected JTextField fontField = new JTextField(15);
        protected JButton visibleButton = new JButton("버튼 표시: 켜짐");
        protected String visibleText = "버튼 표시: ";
        protected JComboBox<String> actionComboBox;

        private static final Logger logger = LoggerFactory.getLogger(SwingWidgetBase.class);
        private boolean isInit = false;
        protected WidgetWrapper widgetWrapper;

        public SwingWidgetBase(WidgetWrapper widgetWrapper, String title, boolean isNameText, boolean isAction, boolean positionField, boolean sizeField, boolean visibleButton, boolean useFont) {
            setTitle(title);
            setSize(500, 200);
            Window window = Minecraft.getInstance().getWindow();
            setLocation(Math.max(window.getX() - 400, 0), window.getY());
            setLayout(new FlowLayout(FlowLayout.LEADING));
            this.widgetWrapper = widgetWrapper;
            NumberFormatter numberFormatter = new NumberFormatter();
            numberFormatter.setValueClass(Integer.class);
            numberFormatter.setMinimum(0);
            numberFormatter.setMaximum(1000);
            idField = new JFormattedTextField(numberFormatter);
            idField.addKeyListener(this);
            idField.setText(String.valueOf(widgetWrapper.getId()));
            idField.getDocument().addDocumentListener(this);
            idField.setToolTipText("고유 ID를 입력하세요.");
            
            add(idField);
            if(isNameText) {
                nameField.addKeyListener(this);
                nameField.setText(widgetWrapper.getMessage());
                nameField.getDocument().addDocumentListener(this);
                idField.setToolTipText("표시될 내용을 입력하세요.");
                add(nameField);
            }
            if(isAction)
            {
                actionField.setText(widgetWrapper.getValue());
                idField.setToolTipText("대상의 ID나 서버 접속의 경우 서버 주소를 입력하세요.");
            }
            if(positionField) {
                xField.addKeyListener(this);
                xField.setText(widgetWrapper.getX() + "");
                yField.addKeyListener(this);
                yField.setText(widgetWrapper.getY() + "");
                zField.addKeyListener(this);
                zField.setText(widgetWrapper.getZ()+"");
                xField.getDocument().addDocumentListener(this);
                yField.getDocument().addDocumentListener(this);
                zField.getDocument().addDocumentListener(this);
                add(xField);
                add(yField);
                add(zField);
            }
            if(sizeField) {
                widthField.addKeyListener(this);
                widthField.setText(widgetWrapper.getWidth() + "");
                heightField.addKeyListener(this);
                heightField.setText(widgetWrapper.getHeight() + "");
                widthField.setToolTipText("개체의 넓이입니다.");
                heightField.setToolTipText("개체의 높낮이입니다.");
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
            if(useFont){
                fontField.setText(widgetWrapper.getCustomFont());
                this.fontField.addKeyListener(this);
                this.fontField.getDocument().addDocumentListener(this);
                this.fontField.setToolTipText("전용 폰트가 있다면, 폰트 이름을 입력하세요.");
                add(fontField);
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
        zField.setText(""+ widgetWrapper.getZ());
        widthField.setText(""+ widgetWrapper.getWidth());
        heightField.setText(""+ widgetWrapper.getHeight());

        if(fontField != null)
            fontField.setText(widgetWrapper.getCustomFont());
        if(actionComboBox != null) {
            logger.info("액션 정보 : {}, 값 정보: {} ", widgetWrapper.getAction(), widgetWrapper.getValue());
            if (widgetWrapper.getAction() != null && widgetWrapper.getAction().equals("명령어")) {
                actionComboBox.setSelectedItem("명령어");
                actionField.setText(widgetWrapper.getValue());
                logger.info("초기화 : 명령어 : {},{} ", widgetWrapper.getAction(), widgetWrapper.getValue());

            } else if(widgetWrapper.getValue() != null){
                actionComboBox.setSelectedItem(widgetWrapper.getAction());
                actionField.setText(widgetWrapper.getValue());
            }
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
            if(Minecraft.getInstance().player != null) {
                actionComboBox.addItem("명령어");
                actionComboBox.addItem("종료:화면");
            }
            else
                actionComboBox.addItem("종료:게임");

            actionComboBox.addItem("배경 변경:");
            actionComboBox.addItem("버튼 표시:");
            actionComboBox.addItem("버튼 숨기기:");
            actionComboBox.addItem("이미지 표시:");
            actionComboBox.addItem("이미지 숨기기:");
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
            zField.setText("" + widgetWrapper.getZ());
            widthField.setText(""+ widgetWrapper.getWidth());
            heightField.setText(""+ widgetWrapper.getHeight());
            if(fontField != null)
                fontField.setText(widgetWrapper.getCustomFont());

            if(actionComboBox != null && widgetWrapper.getAction() != null){
                if(widgetWrapper.getAction().equals("명령어"))
                {
                    actionComboBox.setSelectedItem("명령어");
                    actionField.setText(widgetWrapper.getValue());
                }
                else
                    actionComboBox.setSelectedItem(widgetWrapper.getAction() +":"+widgetWrapper.getValue());
            }

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(isInit) {
                String itemMessage = actionComboBox.getSelectedItem().toString();
                if (e.getSource() == visibleButton) {
                    widgetWrapper.setVisible(!widgetWrapper.isVisible());
                    StringBuilder sb = new StringBuilder(visibleText);
                    sb.append(widgetWrapper.isVisible() ? ": 켜짐" : ": 꺼짐");
                    visibleButton.setText(sb.toString());
                }

                if (e.getSource() == actionComboBox) {
                    logger.info("이벤트 발동됨, 현재 선택된 액션: {}, 액션 필드: {}", itemMessage, actionField.getText());

                    if (itemMessage.equals("접속:")) {
                        if (actionField.getText() == null || actionField.getText().equals(""))
                            actionField.setText("127.0.0.1");
                    }

                    widgetWrapper.setAction(actionComboBox.getSelectedItem().toString());

                    if(itemMessage.contains(":") || itemMessage.equals("명령어")) {
                        widgetWrapper.setValue(actionField.getText());

                    }
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
     *  swing에 키보드로 뭔가 입력이 된다면 바로바로 위젯에 업데이트됨
     */
    public void typeUpdate(){
        // 사용 예:
        if(isInit) {
            if (isFocused()) {
                setIntegerFieldIfNotEmpty(xField, widgetWrapper::setX);
                setIntegerFieldIfNotEmpty(yField, widgetWrapper::setY);
                setIntegerFieldIfNotEmpty(zField, widgetWrapper::setZ);
                setIntegerFieldIfNotEmpty(widthField, widgetWrapper::setWidth);
                setIntegerFieldIfNotEmpty(heightField, widgetWrapper::setHeight);
                if (!nameField.getText().equals(""))
                    widgetWrapper.setMessage(nameField.getText());
                if(!idField.getText().equals(""))
                    widgetWrapper.setId(Integer.parseInt(idField.getText()));
                if(!fontField.getText().isEmpty()){
                    widgetWrapper.setCustomFont(fontField.getText());
                    widgetWrapper.setMessage(nameField.getText());
                }
                if (!actionField.getText().isEmpty()) {
                    widgetWrapper.setAction(actionComboBox.getSelectedItem().toString());
                    widgetWrapper.setValue(actionField.getText());
                    logger.info("액션 업데이트 됨 액션: {}, 값: {}", actionComboBox.getSelectedItem(), actionField.getText());
                }
            }
        }
    }
    @Override
    public void insertUpdate(DocumentEvent e) {
        typeUpdate();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        typeUpdate();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

        typeUpdate();
    }
    }