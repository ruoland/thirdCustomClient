package com.example.event;

import com.example.ScreenCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = "customclient", bus = EventBusSubscriber.Bus.MOD)
public class CommandRegister {

    @SubscribeEvent
    public void command(RegisterCommandsEvent event){
        ScreenCommand.register(event.getDispatcher());
    }
}
