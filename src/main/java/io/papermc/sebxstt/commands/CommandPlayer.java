package io.papermc.sebxstt.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.sebxstt.functions.commands.FunctionPlayer;
import io.papermc.sebxstt.functions.utils.Lib;
import io.papermc.sebxstt.helpers.ReturnCommand;
import io.papermc.sebxstt.providers.PluginProvider;

import java.util.List;

public class CommandPlayer {
    public static void build(Commands cmds) {
        cmds.register(
                ReturnCommand.create("test", ctx -> {
                    FunctionPlayer.Test(ctx);
                    return 1;
                })
        );

        cmds.register(
                ReturnCommand.create("stats", ctx -> {
                    FunctionPlayer.PreviewPlayer(ctx);
                    return 1;
                }),
                "Ver estadisticas del jugador",
                List.of("est")
        );

        cmds.register(
                ReturnCommand.create("clearteams", ctx -> {
                    FunctionPlayer.ClearTeams(ctx);
                    return 1;
                }),
                "Elimina todos los teams del scoreboard",
                List.of()
        );

        cmds.register(
                ReturnCommand.create("return", ctx -> {
                    FunctionPlayer.ReturnPlayer(ctx);
                    return 1;
                }),
                "Te devuelve al punto de muerte",
                List.of()
        );

        cmds.register(
                Commands.literal("invitations")
                        .then(Commands.argument("option", StringArgumentType.word())
                                .suggests(Lib.OptionsSuggestions(PluginProvider.optionsInvitations))
                                .then(Commands.argument("equipos", StringArgumentType.greedyString())
                                        .suggests(Lib.RequestTeamSuggestions())
                                        .executes(ctx -> {
                                            String name = ctx.getArgument("equipos", String.class);
                                            String option  = ctx.getArgument("option", String.class);
                                            FunctionPlayer.RequestPlayer(ctx, name, option);
                                            return 1;
                                        })
                                )
                        )
                        .build(),
                "Invitaciones pendientes a equipos",
                List.of("inv")
        );
    }
}