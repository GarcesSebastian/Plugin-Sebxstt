package com.sebxstt.functions.utils;

import com.sebxstt.instances.PlayerConfig;
import com.sebxstt.instances.PlayersGroup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static com.sebxstt.index.*;

public class Lib {
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
        if (player == null) return null;
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

    public static void clearOrphanNameTags() {
        int removed = 0;
        for (World world : Bukkit.getWorlds()) {
            for (Entity e : world.getEntities()) {
                if (e instanceof ArmorStand as) {
                    if (as.isSmall() && as.isMarker() && as.isCustomNameVisible()) {
                        as.remove();
                        removed++;
                    }
                }
            }
        }
        Bukkit.getLogger().info("[Sebxstt] Removed " + removed + " orphan ArmorStands.");
    }

    public static void removeCustomNameTag(Player player) {
        ArmorStand as = nameTags.remove(player.getUniqueId());
        if (as != null && !as.isDead()) {
            as.remove();
        }
    }

    public static void setCustomNameTag(Player target, String name, ChatColor color) {
        if (nameTags.containsKey(target.getUniqueId())) {
            ArmorStand old = nameTags.remove(target.getUniqueId());
            if (old != null && !old.isDead()) old.remove();
        }

        ArmorStand as = target.getWorld().spawn(target.getLocation().add(0, 2.2, 0), ArmorStand.class);

        as.setVisible(false);
        as.setGravity(false);
        as.setMarker(true);
        as.setCustomNameVisible(true);
        as.setSilent(true);
        as.setBasePlate(false);
        as.setSmall(true);

        if (color != null) {
            as.setCustomName(color + name);
        } else {
            as.setCustomName(name);
        }

        nameTags.put(target.getUniqueId(), as);
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
}
