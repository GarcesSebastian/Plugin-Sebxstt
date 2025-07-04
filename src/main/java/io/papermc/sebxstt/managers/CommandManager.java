package io.papermc.sebxstt.managers;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import io.papermc.sebxstt.commands.CommandCheckpoint;
import io.papermc.sebxstt.commands.CommandGroup;
import io.papermc.sebxstt.commands.CommandPlayer;

import java.util.List;

public class CommandManager {
    public static void registerAll(ReloadableRegistrarEvent<Commands> event) {
        var cmds = event.registrar();

        CommandPlayer.build(cmds);

        cmds.register(CommandCheckpoint.build(), "Gestion de CheckPoints (/checkpoint o /cp)", List.of("cp"));
        cmds.register(CommandGroup.build(), "Gestión de grupos (/group o /gp)", List.of("gp"));
    }
}
