package com.example;

import net.minecraft.network.chat.Component;

public class ScreenCommand {

    public void execute(String screenName){
        ScreenUserCustom userCustom = new ScreenUserCustom(Component.literal(screenName));
    }
}
