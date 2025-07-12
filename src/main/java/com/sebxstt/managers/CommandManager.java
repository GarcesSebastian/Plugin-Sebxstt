package com.sebxstt.managers;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import com.sebxstt.commands.CommandCheckpoint;
import com.sebxstt.commands.CommandGroup;
import com.sebxstt.commands.CommandPlayer;
import com.sebxstt.commands.CommandWarpPoint;

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
