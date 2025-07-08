package io.papermc.sebxstt.managers;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import io.papermc.sebxstt.commands.CommandCheckpoint;
import io.papermc.sebxstt.commands.CommandGroup;
import io.papermc.sebxstt.commands.CommandPlayer;
import io.papermc.sebxstt.commands.CommandWarpPoint;

import java.util.List;

public class CommandManager {
    public static void registerAll(ReloadableRegistrarEvent<Commands> event) {
        var cmds = event.registrar();

        CommandPlayer.build(cmds);
        CommandGroup.build(cmds);

        cmds.register(CommandWarpPoint.build(), "Gestion de Warp Points de grupo (/gwarp)", List.of("gwp"));
        cmds.register(CommandCheckpoint.build(), "Gestion de CheckPoints (/checkpoint o /cp)", List.of("cp"));
    }
}
