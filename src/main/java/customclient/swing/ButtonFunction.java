package customclient.swing;

import com.mojang.realmsclient.RealmsMainScreen;

import customclient.CustomClient;
import customclient.GuiButton;
import customclient.ScreenCustom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

import net.minecraft.resources.ResourceLocation;

import net.neoforged.neoforge.client.gui.ModListScreen;

import java.awt.*;
import java.net.URI;
import java.util.regex.Pattern;

public class ButtonFunction {
    private ScreenCustom customBase;

    public ButtonFunction(ScreenCustom customTool) {
        this.customBase = customTool;
    }

    public void runScript(GuiButton button) {
        try {

            for (String script : button.getButtonBucket().getScripts()) {
                runCommand(script);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runCommand(String command) {
        Minecraft mc = Minecraft.getInstance();
        CustomClient.LOGGER.info("명령 실행: "+command);
        if (command.startsWith("종료")) {
            mc.stop();
        }
        if(command.startsWith("맵 선택:")){
            mc.setScreen(new SelectWorldScreen(customBase));
            return;
        }
        if (command.startsWith("배경 변경:")) {
            String guiName = command.replace("배경 변경:", "");
            customBase.setBackgroundImage(new ResourceLocation("customclient:", guiName));
        }
        if (command.startsWith("열기:")) {
            String guiName = command.replace("열기:", "");
            switch (guiName) {
                case "멀티": {
                    mc.setScreen(new JoinMultiplayerScreen(customBase));
                }
                    break;
                case "옵션":
                    mc.setScreen(new OptionsScreen(customBase, mc.options));
                    break;
                case "설정":
                    mc.setScreen(new OptionsScreen(customBase, mc.options));
                    break;
                case "언어":
                    mc.setScreen(new LanguageSelectScreen(customBase, mc.options, mc.getLanguageManager()));
                    break;
                case "렐름":
                    mc.setScreen(new RealmsMainScreen(customBase));
                    break;
                case "모드":
                    mc.setScreen(new ModListScreen(customBase));
                    break;
            }
        }
        if (command.startsWith("접속:")) {
            String joinKeyword = command.replace("접속:", "");
            String checkIP = "^([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])){3}$";
            if (joinKeyword.startsWith("http")) {
                try {
                    Desktop.getDesktop().browse(new URI(joinKeyword));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (Pattern.matches(checkIP, joinKeyword)) {
                mc.setScreen(null);
                if (mc.level != null) {
                    mc.clearClientLevel(null);
                }
                ConnectScreen.startConnecting(customBase, mc, ServerAddress.parseString(checkIP), new ServerData("instance", joinKeyword, ServerData.Type.OTHER), false, null);

            }
        }
    }
}