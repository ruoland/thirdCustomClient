package customclient.swing;

import customclient.ButtonBucket;
import customclient.GuiButton;
import customclient.OldScreenCustom;
import customclient.OldWidget;

public abstract class SwingManager {
    protected SwingComponentBase swingComponentBase;
    private OldScreenCustom oldScreenCustom;

    public SwingManager(OldScreenCustom oldScreenCustom, SwingComponentBase componentBase){
        this.oldScreenCustom = oldScreenCustom;
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
        swingComponentBase.positionUpdate(oldScreenCustom.getSelectWidget().getX(), oldScreenCustom.getSelectWidget().getY());
        swingComponentBase.sizeUpdate(oldScreenCustom.getSelectWidget().getWidth(), oldScreenCustom.getSelectWidget().getHeight());
    }

    public void setSwing(SwingComponentBase swingComponentBase){
        this.swingComponentBase = swingComponentBase;
    }
    public abstract SwingComponentBase getSwing();

    public boolean canSwing(){
        return swingComponentBase != null;
    }
    public OldWidget getWidget(){
        return this.oldScreenCustom.getSelectWidget();
    }

    public static class BackgroundManager extends SwingManager{


        public BackgroundManager(OldScreenCustom oldScreenCustom) {
            super(oldScreenCustom, new SwingBackground(true));
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
        public ButtonManager(OldScreenCustom oldScreenCustom){
            super(oldScreenCustom, new SwingButton());
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


    }
}
