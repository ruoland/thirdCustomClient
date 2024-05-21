package customclient.swing;

import customclient.GuiButton;
import customclient.ScreenCustom;
import customclient.Widget;

public abstract class SwingManager {
    protected SwingComponentBase swingComponentBase;
    private ScreenCustom screenCustom;

    public SwingManager(ScreenCustom screenCustom, SwingComponentBase componentBase){
        this.screenCustom = screenCustom;
        this.swingComponentBase = componentBase;
    }
    public void close(){
        if(swingComponentBase != null) {
            swingComponentBase.dispose();
            swingComponentBase = null;
        }
    }
    public abstract void init();
    public void update(){
        swingComponentBase.positionUpdate(screenCustom.getSelectWidget().getX(), screenCustom.getSelectWidget().getY());
        swingComponentBase.sizeUpdate(screenCustom.getSelectWidget().getWidth(), screenCustom.getSelectWidget().getHeight());
        screenCustom.getSelectWidget();
    }

    public void setSwing(SwingComponentBase swingComponentBase){
        this.swingComponentBase = swingComponentBase;
    }
    public abstract SwingComponentBase getSwing();

    public boolean canSwing(){
        return swingComponentBase != null;
    }
    public Widget getWidget(){
        return this.screenCustom.getSelectWidget();
    }

    public static class BackgroundManager extends SwingManager{


        public BackgroundManager(ScreenCustom screenCustom) {
            super(screenCustom, new SwingBackground(true));
        }

        @Override
        public void init() {
            swingComponentBase.init(this);
        }

        @Override
        public SwingBackground getSwing() {
            return (SwingBackground) swingComponentBase;
        }
    }
    public static class ButtonManager extends SwingManager{
        private ButtonBucket buttonBucket;
        private int buttonID;
        private SwingButton swingButton;
        public ButtonManager(ScreenCustom screenCustom){
            super(screenCustom, new SwingButton());
            swingButton = (SwingButton) swingComponentBase;
        }

        @Override
        public void init() {
            swingComponentBase.init(this);
        }
        @Override
        public SwingButton getSwing() {
            return (SwingButton) swingComponentBase;
        }

        public ButtonBucket getButtonBucket() {
            return buttonBucket;
        }

        @Override
        public GuiButton getWidget() {
            return (GuiButton) super.getWidget();
        }

        public ButtonBucket createBucket(){
            return buttonBucket = new ButtonBucket(getWidget().getID());
        }
    }
}
