package com.example;

import com.example.packet.ScreenData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class ScreenCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("screen")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.literal("open")
                                        .then(Commands.argument("name", StringArgumentType.greedyString())
                                                .executes(context -> {
                                                    String name = StringArgumentType.getString(context, "name");
                                                    for(ServerPlayer player :EntityArgument.getPlayers(context, "targets")) {
                                                        PacketDistributor.sendToPlayer(player, new ScreenData(name, 0));
                                                    }
                                                    return 1;
                                                })
                                        )
                                )
                                .then(Commands.literal("close")
                                        .executes(context -> {
                                            for(ServerPlayer player :EntityArgument.getPlayers(context, "targets")) {
                                                PacketDistributor.sendToPlayer(player, new ScreenData("", 1));
                                            }
                                            return 1;
                                        })
                                )
                        )
        );
       
    }
}