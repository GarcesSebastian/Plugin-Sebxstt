package io.papermc.sebxstt.functions.utils;

import io.papermc.sebxstt.instances.PlayersGroup;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

import static io.papermc.sebxstt.index.mainData;
import static io.papermc.sebxstt.index.mm;

public class InPlayer {
    public static Player instance(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getPlayer();
    }

    public static PlayersGroup group(UUID id) {
        if (mainData.playersGroups == null) return null;
        return mainData.playersGroups.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    public static String name(UUID uuid) {
        OfflinePlayer offPlayer =  Bukkit.getOfflinePlayer(uuid);

        if (offPlayer.isOnline()) {
            return offPlayer.getPlayer().getName();
        }

        return offPlayer.getName();
    }

    public static void message(UUID uuid, String message) {
        if (!(instance(uuid) instanceof Player p)) return;
        p.sendMessage(mm.deserialize(message));
    }
}
