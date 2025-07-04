package io.papermc.sebxstt.managers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import java.util.*;

public class GroupTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (!(sender instanceof Player)) return null;

        if (cmd.getName().equalsIgnoreCase("group")) {
            if (args.length == 1) {
                return List.of("create", "info", "leave", "invite", "kick");
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
                return Collections.singletonList("<nombre>");
            }
            if (args.length == 3 && args[0].equalsIgnoreCase("create")) {
                List<String> colores = new ArrayList<>();
                for (ChatColor c : ChatColor.values()) {
                    if (c.isColor()) colores.add(c.name().toLowerCase());
                }
                Collections.sort(colores);
                return colores;
            }
        }
        return Collections.emptyList();
    }
}
