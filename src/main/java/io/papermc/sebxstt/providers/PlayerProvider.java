package io.papermc.sebxstt.providers;

import io.papermc.sebxstt.functions.utils.Lib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public class PlayerProvider {
    public static void setup(UUID id) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("hiddenNametags");
        if (team == null) {
            team = scoreboard.registerNewTeam("hiddenNametags");
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }

        OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(id);
        if(!offPlayer.isOnline()) return;
        Player player = offPlayer.getPlayer();

        assert player != null;
        team.addEntry(player.getName());

        var grp = Lib.FindPlayerInGroup(player.getName());
        String customName;

        if (grp != null) {
            String groupColor = grp.getColor().name().toLowerCase();
            customName = ChatColor.GRAY + player.getName() + ChatColor.RESET + " [" +
                    ChatColor.valueOf(groupColor.toUpperCase()) + grp.getName() + ChatColor.RESET + "]";
        } else {
            customName = ChatColor.WHITE + player.getName() + " [Sin Equipo]";
        }

        Lib.setCustomNameTag(player, customName, null);
    }

}