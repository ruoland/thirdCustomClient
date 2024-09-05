package com.example;

import com.example.packet.ImageData;
import com.example.packet.ScreenData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class ScreenCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("screen")
                        .then(Commands.argument("player", EntityArgument.players())

                                .then(Commands.literal("image")
                                        .then(Commands.argument("imagePath", StringArgumentType.string())
                                                .executes(context -> executeScreenImageCommand(context))
                                                .then(Commands.argument("x", IntegerArgumentType.integer())
                                                        .then(Commands.argument("y", IntegerArgumentType.integer())
                                                                .executes(context -> executeScreenImageCommand(context))
                                                                .then(Commands.argument("scale", FloatArgumentType.floatArg())
                                                                                .executes(context -> executeScreenImageCommand(context))
                                                                                .then(Commands.argument("duration", IntegerArgumentType.integer())
                                                                                        .executes(context -> executeScreenImageCommand(context))
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                )
                        ).then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.literal("open")
                                        .then(Commands.argument("imagePath", StringArgumentType.greedyString())
                                                .executes(context -> {
                                                    String name = StringArgumentType.getString(context, "imagePath");
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

    private static int executeScreenImageCommand(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        String imagePath = "customclient:textures/"+StringArgumentType.getString(context, "imagePath");

        // 모든 인자를 선택적으로 처리
        int x = getOptionalArgument(context, "x", 0);  // 기본값 0
        int y = getOptionalArgument(context, "y", 0);  // 기본값 0
        float scale = getOptionalArgument(context, "scale", 0F);  // 기본값 0

        int duration = getOptionalArgument(context, "duration", 6000);  // 기본 지속 시간 6000 (5초, 틱 기준)

        PacketDistributor.sendToPlayer(player, new ImageData(imagePath, x, y,scale, duration));

        return 1;
    }

    // 선택적 인자를 가져오는 헬퍼 메서드
    private static <T> T getOptionalArgument(CommandContext<CommandSourceStack> context, String name, T defaultValue) {
        try {
            return context.getArgument(name, (Class<T>)defaultValue.getClass());
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

}