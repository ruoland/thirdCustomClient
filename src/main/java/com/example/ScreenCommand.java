package com.example;

import com.example.userscreen.ScreenUserCustom;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.commands.SayCommand;
import net.minecraft.server.commands.SummonCommand;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.registration.NetworkRegistry;
import net.neoforged.neoforge.server.command.CommandUtils;

import java.awt.*;



public class ScreenCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("open")
                        .then(Commands.argument("name", StringArgumentType.greedyString())
                                .executes(context -> {
                                    CommandSourceStack source = (CommandSourceStack) context.getSource();
                                    String name = StringArgumentType.getString(context, "name");

                                    Minecraft.getInstance().setScreen(new ScreenUserCustom(Component.literal(name)));
                                    return 1;
                                })));
    }

    private static int execute(CommandContext<CommandSourceStack> command){

        return Command.SINGLE_SUCCESS;
    }
}
