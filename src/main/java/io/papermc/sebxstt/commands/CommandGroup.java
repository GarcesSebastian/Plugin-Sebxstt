package io.papermc.sebxstt.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.sebxstt.functions.commands.FunctionGroup;
import io.papermc.sebxstt.functions.utils.Lib;

public class CommandGroup {
    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("group")
                .then(Commands.literal("create")
                        .then(Commands.argument("color", StringArgumentType.word())
                                .suggests(Lib.ColorSuggestions())
                                .then(Commands.argument("nombre", StringArgumentType.greedyString())
                                        .executes(ctx -> {
                                            String name = ctx.getArgument("nombre", String.class);
                                            String colorInput = ctx.getArgument("color", String.class);
                                            FunctionGroup.CreateGroup(ctx, name, colorInput);
                                            return 1;
                                        })
                                )
                        )
                )
                .then(Commands.literal("storage")
                        .executes(ctx -> {
                            FunctionGroup.ShowStorage(ctx);
                            return 1;
                        })
                )
                .then(Commands.literal("info")
                        .executes(ctx -> {
                            FunctionGroup.PreviewGroup(ctx);
                            return 1;
                        })
                )
                .then(Commands.literal("post")
                        .then(Commands.argument("cargo", StringArgumentType.word())
                                .suggests(Lib.PlayersTypeSuggestions())
                                .then(Commands.argument("jugador", StringArgumentType.greedyString())
                                        .suggests(Lib.PlayersSuggestionsTeam())
                                        .executes(ctx -> {
                                            String target = ctx.getArgument("jugador", String.class);
                                            String cargo = ctx.getArgument("cargo", String.class);
                                            FunctionGroup.ChangePostGroup(ctx, target, cargo);
                                            return 1;
                                        })
                                )
                        )
                )
                .then(Commands.literal("leave")
                        .executes(ctx -> {
                            FunctionGroup.LeaveGroup(ctx);
                            return 1;
                        })
                )
                .then(Commands.literal("invite")
                        .then(Commands.argument("cargo", StringArgumentType.word())
                                .suggests(Lib.PlayersTypeSuggestions())
                                .then(Commands.argument("jugador", StringArgumentType.greedyString())
                                        .suggests(Lib.PlayersSuggestions())
                                        .executes(ctx -> {
                                            String target = ctx.getArgument("jugador", String.class);
                                            String cargo = ctx.getArgument("cargo", String.class);
                                            FunctionGroup.InviteGroup(ctx, target, cargo);
                                            return 1;
                                        })
                                )
                        )
                )
                .then(Commands.literal("kick")
                        .then(Commands.argument("jugador", StringArgumentType.greedyString())
                                .suggests(Lib.PlayersSuggestionsTeam())
                                .executes(ctx -> {
                                    String target = ctx.getArgument("jugador", String.class);
                                    FunctionGroup.KickGroup(ctx, target);
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("disband")
                        .executes(ctx -> {
                            FunctionGroup.DisbandGroup(ctx);
                            return 1;
                        })
                )
                .build();
    }
}