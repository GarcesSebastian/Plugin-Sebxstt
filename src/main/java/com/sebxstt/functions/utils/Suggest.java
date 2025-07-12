package com.sebxstt.functions.utils;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import com.sebxstt.instances.enums.PlayerTypeGroup;
import com.sebxstt.instances.CheckPoint;
import com.sebxstt.instances.PlayerConfig;
import com.sebxstt.instances.PlayersGroup;
import com.sebxstt.instances.RequestGroup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Suggest {
    public static SuggestionProvider<CommandSourceStack> ColorSuggestions() {
        return (ctx, builder) -> {
            Arrays.stream(ChatColor.values())
                    .filter(ChatColor::isColor)
                    .map(c -> c.name().toLowerCase())
                    .forEach(c -> {
                        builder.suggest(c);
                    });

            return builder.buildFuture();
        };
    }

    public static SuggestionProvider<CommandSourceStack> PlayersSuggestions() {
        return (ctx, builder) -> {
            String name = ctx.getSource().getSender().getName();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().equals(name)) continue;
                builder.suggest(player.getName());
            }
            return builder.buildFuture();
        };
    }

    public static SuggestionProvider<CommandSourceStack> PlayersSuggestionsTeam() {
        return (ctx, builder) -> {
            String name = ctx.getSource().getSender().getName();
            PlayersGroup team = Lib.FindPlayerInGroup(name);
            for (Player member : team.getPlayers()) {
                if (member.getName().equals(name)) continue;
                builder.suggest(member.getName());
            }
            return builder.buildFuture();
        };
    }

    public static SuggestionProvider<CommandSourceStack> PlayersTypeSuggestions() {
        return (ctx, builder) -> {
            Arrays.stream(PlayerTypeGroup.values()).filter(p -> !p.equals(PlayerTypeGroup.LEADER) && !p.equals(PlayerTypeGroup.NONE)).toList().forEach(playersGroup -> {
                builder.suggest(playersGroup.name().toLowerCase());
            });

            return builder.buildFuture();
        };
    }

    public static SuggestionProvider<CommandSourceStack> WarpPointSuggestions() {
        return (ctx, builder) -> {
            String name = ctx.getSource().getSender().getName();
            PlayersGroup team = Lib.FindPlayerInGroup(name);

            if (team != null) {
                team.getWarpPoints().forEach(wp -> {
                    builder.suggest(wp.name.toLowerCase());
                });
            }

            return builder.buildFuture();
        };
    }

    public static SuggestionProvider<CommandSourceStack> CheckPointsSuggestions() {
        return (ctx, builder) -> {
            var senderRaw = ctx.getSource().getSender();
            Player p = (Player) senderRaw;
            PlayerConfig pc = Lib.getPlayerConfig(p);

            for (CheckPoint cp : pc.getCheckPoints()) {
                builder.suggest(cp.getName());
            }
            return builder.buildFuture();
        };
    }

    public static SuggestionProvider<CommandSourceStack> RequestTeamSuggestions() {
        return (ctx, builder) -> {
            var senderRaw = ctx.getSource().getSender();
            Player p = (Player) senderRaw;
            PlayerConfig pc = Lib.getPlayerConfig(p);
            if (pc == null) {
                throw new RuntimeException("Player not found");
            }

            for (RequestGroup requestGroup : pc.getRequestGroup()) {
                builder.suggest(InPlayer.group(requestGroup.getGroup()).getName());
            }

            return builder.buildFuture();
        };
    }

    public static SuggestionProvider<CommandSourceStack> OptionsSuggestions(String[] options) {
        return (ctx, builder) -> {
            Arrays.stream(options).forEach(option -> {
                builder.suggest(option);
            });

            return builder.buildFuture();
        };
    }

}
