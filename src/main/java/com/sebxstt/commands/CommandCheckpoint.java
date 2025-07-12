package com.sebxstt.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import com.sebxstt.functions.commands.FunctionCheckPoint;
import com.sebxstt.functions.utils.Suggest;

public class CommandCheckpoint {
    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("checkpoint")
                .then(Commands.literal("save")
                        .executes(ctx -> {
                            FunctionCheckPoint.SaveCheckpoint(ctx, null);
                            return 1;
                        })
                        .then(Commands.argument("name", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    String name = ctx.getArgument("name", String.class);
                                    FunctionCheckPoint.SaveCheckpoint(ctx, name);
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("delete")
                        .then(Commands.argument("name", StringArgumentType.greedyString())
                                .suggests(Suggest.CheckPointsSuggestions())
                                .executes(ctx -> {
                                    String name = ctx.getArgument("name", String.class);
                                    FunctionCheckPoint.DeleteCheckPoint(ctx, name);
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("teleport")
                        .then(Commands.argument("name", StringArgumentType.greedyString())
                                .suggests(Suggest.CheckPointsSuggestions())
                                .executes(ctx -> {
                                    String name = ctx.getArgument("name", String.class);
                                    FunctionCheckPoint.CheckPoint(ctx, name);
                                    return 1;
                                })
                        )
                ).build();
    }
}