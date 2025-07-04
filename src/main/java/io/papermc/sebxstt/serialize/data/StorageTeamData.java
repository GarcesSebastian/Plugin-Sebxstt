package io.papermc.sebxstt.serialize.data;

import io.papermc.sebxstt.functions.utils.InPlayer;
import io.papermc.sebxstt.functions.utils.Lib;
import io.papermc.sebxstt.instances.PlayersGroup;
import io.papermc.sebxstt.instances.StorageTeam;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.UUID;

public class StorageTeamData {
    public UUID group;
    public int size;
    public String encodedContents;

    public StorageTeamData() {}

    public StorageTeamData(StorageTeam storageTeam) {
        if (storageTeam == null || storageTeam.group == null) return;

        this.group = storageTeam.group;
        Inventory inv = storageTeam.instance;

        if (inv != null) {
            this.size = inv.getSize();

            try {
                this.encodedContents = Lib.itemStackArrayToBase64(inv.getContents());
            } catch (IOException e) {
                System.out.println("[StorageTeamData] Error al serializar contenido: " + e.getMessage());
                this.encodedContents = null;
            }
        } else {
            this.size = 9;
            this.encodedContents = null;
        }
    }

    public static StorageTeamData create(StorageTeam storageTeam) {
        return new StorageTeamData(storageTeam);
    }

    public StorageTeam resolve(PlayersGroup pg) {
        if (group == null) return null;

        if (pg == null) {
            System.out.println("[StorageTeamData] Grupo no encontrado para UUID: " + group);
            return null;
        }

        int resolvedSize = (size > 0 && size % 9 == 0) ? size : 9;
        StorageTeam storageTeam = new StorageTeam(resolvedSize, pg);

        if (encodedContents != null) {
            try {
                ItemStack[] contents = Lib.itemStackArrayFromBase64(encodedContents);

                if (contents.length == resolvedSize) {
                    storageTeam.instance.setContents(contents);
                } else {
                    System.out.println("[StorageTeamData] Cantidad de items no coincide con el tamaño del inventario.");
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("[StorageTeamData] Error al deserializar contenido: " + e.getMessage());
            }
        }

        return storageTeam;
    }
}