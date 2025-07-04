package io.papermc.sebxstt.functions.commands;

import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.sebxstt.instances.PlayerConfig;
import io.papermc.sebxstt.functions.utils.Lib;
import io.papermc.sebxstt.serialize.data.PlayerConfigData;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.papermc.sebxstt.index.mm;
import static io.papermc.sebxstt.providers.DataStoreProvider.DS;

public class FunctionCheckPoint {
    public static void SaveCheckpoint(CommandContext<CommandSourceStack> ctx, String name) {
        var senderRaw = ctx.getSource().getSender();
        if (!(senderRaw instanceof Player p)) return;

        if (name == null) {
            LocalDateTime dateNow = LocalDateTime.now();
            DateTimeFormatter dateFormatted = DateTimeFormatter.ofPattern("ddMMyyyyHHmm");
            String dateTimeFormatted = dateNow.format(dateFormatted);
            name = "autosave-" + dateTimeFormatted;
        }

        PlayerConfig pc = Lib.getPlayerConfig(p);
        if (pc == null) return;

        pc.saveCheckPoint(name);
        DS.edit("id", pc.id.toString(), PlayerConfigData.create(pc), PlayerConfigData.class);
    }

    public static void DeleteCheckPoint(CommandContext<CommandSourceStack> ctx, String name) {
        var senderRaw = ctx.getSource().getSender();
        if (!(senderRaw instanceof Player p)) return;

        PlayerConfig pc = Lib.getPlayerConfig(p);
        if (pc == null) return;

        pc.deleteCheckPoint(name);
        DS.edit("id", pc.id.toString(), PlayerConfigData.create(pc), PlayerConfigData.class);
    }

    public static void CheckPoint(CommandContext<CommandSourceStack> ctx, String name) {
        var senderRaw = ctx.getSource().getSender();
        if (!(senderRaw instanceof Player p)) return;

        if (name == null || name.isEmpty()) {
            p.sendMessage(mm.deserialize(
                    "<red>Debes proporcionar el nombre del checkpoint.</red>\n" +
                            "<gray>Uso correcto: <gold>/checkpoint teleport <nombre></gold></gray>"
            ));
            return;
        }

        PlayerConfig pc = Lib.getPlayerConfig(p);
        if (pc == null) return;

        pc.checkPoint(name);
    }
}
