package customclient.swing;

import customclient.GuiButton;

import java.util.ArrayList;
import java.util.Arrays;

public class ButtonBucket {
    private ArrayList<String> scripts = new ArrayList<>();
    private int buttonID;
    public ButtonBucket(int buttonID) {
        this.buttonID = buttonID;
        updateButton();
    }

    public ArrayList<String> getScripts() {
        return scripts;
    }

    public String getType(int i){
        String script = scripts.get(i);
        String[] scriptSplit = script.split(":");
        if(scriptSplit.length == 0){
            return script;
        }
        return scriptSplit[0]+":";
    }
    public String getActionScript(int i){
        String script = scripts.get(i).replace(getType(i), "");
        return script;
    }


    public void initScript(String script) {
        this.scripts.clear();
        this.scripts.addAll(Arrays.asList(script.split("/n/")));
    }

    public void updateButton(){
        switch (buttonID) {
            case 0:
                initScript("열기:맵 선택");
                break;
            case 1:
                initScript("열기:멀티");
                break;
            case 2:
                initScript("열기:렐름");
                break;
            case 3:
                initScript("열기:모드");
                break;
            case 4:
                initScript("열기:언어");
                break;
            case 5:
                initScript("열기:설정");
                break;
            case 6:
                initScript("종료");
                break;
            case 7:
                initScript("접근성");
                break;
            default:
                initScript("내용을 입력하세요.");
                break;
        }
    }
    public void saveNBT(){
    }
}