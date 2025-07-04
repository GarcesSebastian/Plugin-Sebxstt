package io.papermc.sebxstt.functions.utils;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import io.papermc.sebxstt.enums.PlayerTypeGroup;
import io.papermc.sebxstt.instances.CheckPoint;
import io.papermc.sebxstt.instances.PlayerConfig;
import io.papermc.sebxstt.instances.PlayersGroup;
import io.papermc.sebxstt.instances.RequestGroup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static io.papermc.sebxstt.index.*;

public class Lib {
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
            Arrays.stream(PlayerTypeGroup.values()).toList().forEach(playersGroup -> {
                builder.suggest(playersGroup.name().toLowerCase());
            });

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

    public static void ChooseGroupColor(Player p) {
        StringBuilder sb = new StringBuilder("Elige un color: ");
        for (ChatColor c : ChatColor.values()) {
            if (!c.isColor()) continue;
            sb.append(c).append(c.name().toLowerCase()).append("§r, ");
        }
        p.sendMessage(sb.substring(0, sb.length() - 2));
        p.sendMessage("Escribe /group create <color> <nombre>");
    }

    public static String itemStackArrayToBase64(ItemStack[] items) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

        dataOutput.writeInt(items.length);
        for (ItemStack item : items) {
            dataOutput.writeObject(item);
        }

        dataOutput.close();
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    public static ItemStack[] itemStackArrayFromBase64(String data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
        BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

        int size = dataInput.readInt();
        ItemStack[] items = new ItemStack[size];

        for (int i = 0; i < size; i++) {
            items[i] = (ItemStack) dataInput.readObject();
        }

        dataInput.close();
        return items;
    }

    public static PlayerConfig getPlayerConfig(Player player) {
        PlayerConfig playerConfig = null;
        
        for (PlayerConfig pc : mainData.PlayerConfigs()) {
            if (playerConfig != null) break;

            if(InPlayer.name(pc.player).equals(player.getName())) {
                playerConfig = pc;
                break;
            }
        }
        
        return playerConfig;
    }

    public static PlayersGroup FindOwnerInGroup(UUID owner) {
        if (mainData.playersGroups == null) return null;
        return mainData.playersGroups.stream().filter(group -> group.getOwner().equals(owner)).findFirst().orElse(null);
    }

    public static PlayersGroup FindPlayerInGroup(String name) {
        PlayersGroup found = null;

        for (PlayersGroup pg : mainData.playersGroups) {
            if(found != null) break;

            if(InPlayer.name(pg.getOwner()).equals(name)) {
                found = pg;
                break;
            }

            for (UUID member : pg.getMembers()) {
                if (!(InPlayer.instance(member) instanceof Player m)) continue;
                if(m.getName().equals(name)) {
                    found = pg;
                    break;
                }
            }
        }

        return found;
    }

    public static ArrayList<Player> ClosestMembers(PlayersGroup playersGroups, Player target, Double distance) {
        ArrayList<Player> closestMembers = new ArrayList<>();
        Location locTarget = target.getLocation();

        for (Player member : playersGroups.getPlayers()) {
            if(member.getName().equals(target.getName())) continue;

            Location locMember = member.getLocation();
            double distanceBetween = locTarget.distance(locMember);

            if (distanceBetween < distance) {
                closestMembers.add(member);
            }
        }

        return closestMembers;
    }

    public static boolean PlayerIsOwner(Player player, Collection<PlayersGroup> playersGroups) {
        for (PlayersGroup pg : playersGroups) {
            if (InPlayer.name(pg.getOwner()).equals(player.getName())) {
                return true;
            }
        }

        return false;
    }
}
