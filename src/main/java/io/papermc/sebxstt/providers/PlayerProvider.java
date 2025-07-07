package io.papermc.sebxstt.providers;

import io.papermc.sebxstt.functions.utils.Lib;
import io.papermc.sebxstt.instances.PlayersGroup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public class PlayerProvider {
    public static void setup(UUID id) {
        OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(id);
        if(!offPlayer.isOnline()) return;
        Player player = offPlayer.getPlayer();

        assert player != null;

        PlayersGroup pg = Lib.FindPlayerInGroup(player.getName());
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        if (pg == null) {
            Team team = scoreboard.getTeam("hiddenNametags");
            if (team == null) {
                team = scoreboard.registerNewTeam("hiddenNametags");
                team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
                team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            }

            team.addEntry(player.getName());
        }

        if (pg != null) {
            String teamName = pg.getName();
            Team team = scoreboard.getTeam(teamName);
            if (team == null) {
                team = scoreboard.registerNewTeam(teamName);
                team.setColor(pg.getColor());
                team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
                team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            }

            if (!team.hasEntry(player.getName())) {
                team.addEntry(player.getName());
            }
        }

        String customName;

        if (pg != null) {
            String groupColor = pg.getColor().name().toLowerCase();
            customName = ChatColor.GRAY + player.getName() + ChatColor.RESET + " [" +
                    ChatColor.valueOf(groupColor.toUpperCase()) + pg.getName() + ChatColor.RESET + "]";
        } else {
            customName = ChatColor.WHITE + player.getName() + " [Sin Equipo]";
        }

        Lib.setCustomNameTag(player, customName, null);
    }
}