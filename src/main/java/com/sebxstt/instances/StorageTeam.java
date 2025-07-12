package com.sebxstt.instances;

import com.sebxstt.helpers.GroupPermissions;
import com.sebxstt.instances.enums.PlayerTypeGroup;
import com.sebxstt.functions.utils.InPlayer;
import com.sebxstt.functions.utils.Lib;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.bukkit.Material;

import java.util.UUID;

import static com.sebxstt.index.mm;

public class StorageTeam {
    public Inventory instance;
    public ChatColor color;
    public UUID group;

    public StorageTeam(int size, PlayersGroup group) {
        Component title = Component.text(group.getColor() + "Storage: " + group.getName());
        this.instance = Bukkit.createInventory(null, size, title);

        this.color = group.getColor();
        this.group = group.getId();
    }

    public Material getBanner() {
        try {
            String colorName = color.name().toUpperCase();
            return Material.valueOf(colorName + "_BANNER");
        } catch (IllegalArgumentException e) {
            System.err.println("[WARN] Color inválido para banner: " + color);
            return Material.WHITE_BANNER;
        }
    }

    public void setupContents() {
        ItemStack banner = new ItemStack(this.getBanner());
        ItemMeta meta = banner.getItemMeta();

        if (meta != null) {
            if (!(InPlayer.group(group) instanceof PlayersGroup pg)) return;

            meta.setDisplayName(pg.getColor() + "Grupo " + pg.getName());
            meta.setLore(java.util.List.of(
                    ChatColor.GRAY + "Dueño: " + InPlayer.name(pg.getOwner()),
                    ChatColor.GRAY + "Miembros: " + pg.getMembers().size()
            ));
            banner.setItemMeta(meta);
        }

        instance.setItem(0, banner);
    }

    public void open(Player player) {
        PlayerConfig pc = Lib.getPlayerConfig(player);
        PlayerTypeGroup playerTypeGroup = pc.getPlayerType();

        if (!GroupPermissions.canOpenStorage(playerTypeGroup)) {
            player.sendMessage(mm.deserialize("<red><bold>Sin permiso:</bold> No tienes el cargo suficiente para hacer esta accion.</red>"));
            return;
        }
        
        player.openInventory(this.instance);
    }

    public void close(Player player) {
        player.closeInventory();
    }
}
