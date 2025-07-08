package io.papermc.sebxstt.instances;

import io.papermc.sebxstt.functions.utils.InPlayer;
import io.papermc.sebxstt.functions.utils.Lib;
import io.papermc.sebxstt.instances.enums.PlayerTypeGroup;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.UUID;

public class WarpPoint {
    public UUID id;
    public String name;                // Nombre único del warp dentro del grupo
    public Location location;          // Coordenadas exactas del warp
    public UUID createdBy;            // Jugador que lo creó
    public LocalDateTime createdAt;   // Fecha de creación
    public UUID group;                // Grupo asociado

    public WarpPoint(String name, UUID createdBy, UUID group, Location location) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();

        Player player = InPlayer.instance(createdBy);
        if (player == null) {
            throw new IllegalStateException("No se pudo crear WarpPoint: jugador.");
        }

        this.location = location;
        this.group = group;
    }

    public void teleport(UUID target) {
        Player player = InPlayer.instance(target);
        if (player == null) {
            System.out.println("[WarpPoint] No se encontro el jugador con el id " + target);
            return;
        }

        player.teleport(location);
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setGroup(UUID group) {
        this.group = group;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
