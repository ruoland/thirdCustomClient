package com.example;

import com.example.packet.ScreenOpenData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.commands.GiveCommand;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class ScreenCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("open")
                        .then(
                                Commands.argument("targets", EntityArgument.players())
                                        .then(Commands.argument("name", StringArgumentType.greedyString())
                                .executes(context -> {
                                    String name = StringArgumentType.getString(context, "name");
                                    for(ServerPlayer player :EntityArgument.getPlayers(context, "targets")) {
                                        PacketDistributor.sendToPlayer(player, new ScreenOpenData(name, 1));
                                    }
                                    return 1;
                                }))));
    }
}