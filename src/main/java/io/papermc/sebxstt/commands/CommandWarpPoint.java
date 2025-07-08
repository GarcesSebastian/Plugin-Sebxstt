package io.papermc.sebxstt.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.sebxstt.functions.commands.FunctionGroup;
import io.papermc.sebxstt.functions.utils.Suggest;

public class CommandWarpPoint {
    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("gwarp")
                .then(Commands.literal("create")
                        .then(Commands.argument("nombre", StringArgumentType.word())
                                .executes(ctx -> {
                                    String name = ctx.getArgument("nombre", String.class);
                                    FunctionGroup.SetWarp(ctx, name);
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("delete")
                        .then(Commands.argument("warpPoint", StringArgumentType.word())
                                .suggests(Suggest.WarpPointSuggestions())
                                .executes(ctx -> {
                                    String warpPoint = ctx.getArgument("warpPoint", String.class);
                                    FunctionGroup.DeleteWarp(ctx, warpPoint);
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("list")
                        .executes(ctx -> {
                            FunctionGroup.ListWarps(ctx);
                            return 1;
                        })
                )
                .then(Commands.literal("tp")
                        .then(Commands.argument("warpPoint", StringArgumentType.word())
                                .suggests(Suggest.WarpPointSuggestions())
                                .executes(ctx -> {
                                    String warpPoint = ctx.getArgument("warpPoint", String.class);
                                    FunctionGroup.WarpMember(ctx, warpPoint);
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("all")
                        .then(Commands.argument("warpPoint", StringArgumentType.word())
                                .suggests(Suggest.WarpPointSuggestions())
                                .executes(ctx -> {
                                    String warpPoint = ctx.getArgument("warpPoint", String.class);
                                    FunctionGroup.WarpAll(ctx, warpPoint);
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("post")
                        .then(Commands.argument("post", StringArgumentType.word())
                                .suggests(Suggest.PlayersTypeSuggestions())
                                .then(Commands.argument("warpPoint", StringArgumentType.word())
                                        .suggests(Suggest.WarpPointSuggestions())
                                        .executes(ctx -> {
                                            String post = ctx.getArgument("post", String.class);
                                            String warpPoint = ctx.getArgument("warpPoint", String.class);
                                            FunctionGroup.WarpPost(ctx, post, warpPoint);
                                            return 1;
                                        })
                                )
                        )
                ).build();
    }
}