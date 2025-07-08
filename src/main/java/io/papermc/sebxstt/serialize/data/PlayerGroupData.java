package io.papermc.sebxstt.serialize.data;

import io.papermc.sebxstt.instances.PlayerConfig;
import io.papermc.sebxstt.instances.PlayersGroup;
import io.papermc.sebxstt.instances.WarpPoint;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerGroupData {
    public UUID id;
    public String name;
    public UUID owner;
    public List<UUID> members = new ArrayList<>();
    public int level;
    public String color;
    public StorageTeamData storage;

    public List<PlayerGroupData> enemies = new ArrayList<>();
    public List<PlayerGroupData> allies = new ArrayList<>();
    public List<WarpPointData> warpPoints = new ArrayList<>();
    public List<PlayerConfigData> pending = new ArrayList<>();

    public PlayerGroupData() {}

    private PlayerGroupData(PlayersGroup group) {
        if (group == null) {
            System.out.println("[PlayerGroupData] Grupo recibido es null.");
            return;
        }

        this.id = group.getId();
        this.name = group.getName() != null ? group.getName() : "SinNombre";
        this.owner = group.getOwner();
        this.level = group.level;
        this.color = (group.getColor() != null) ? group.getColor().name() : ChatColor.WHITE.name();

        if (group.getStorage() != null) {
            this.storage = StorageTeamData.create(group.getStorage());
        }

        if (group.getMembers() != null) {
            this.members = new ArrayList<>(group.getMembers());
        }

        if (group.getWarpPoints() != null) {
            for (WarpPoint wp : group.getWarpPoints()) {
                if (wp != null) {
                    this.warpPoints.add(WarpPointData.create(wp));
                }
            }
        }

        if (group.allies != null) {
            for (PlayersGroup ally : group.allies) {
                if (ally != null) {
                    this.allies.add(new PlayerGroupData(ally));
                }
            }
        }

        if (group.enemies != null) {
            for (PlayersGroup enemy : group.enemies) {
                if (enemy != null) {
                    this.enemies.add(new PlayerGroupData(enemy));
                }
            }
        }

        if (group.pending != null) {
            for (PlayerConfig pc : group.pending) {
                if (pc != null) {
                    this.pending.add(PlayerConfigData.create(pc));
                }
            }
        }
    }

    public static PlayerGroupData create(PlayersGroup group) {
        return new PlayerGroupData(group);
    }

    public PlayersGroup resolve() {
        if (this.name == null || this.owner == null) {
            System.out.println("[PlayerGroupData] No se puede resolver: 'name' o 'owner' es null.");
            return null;
        }

        ChatColor parsedColor;
        try {
            parsedColor = ChatColor.valueOf(this.color.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("[PlayerGroupData] Color inválido: " + this.color + ", se usará blanco.");
            parsedColor = ChatColor.WHITE;
        }

        PlayersGroup group = new PlayersGroup(this.name, owner, parsedColor);
        group.setId(id);
        group.level = this.level;

        if (this.members != null) {
            group.members.addAll(this.members);
        }

        if (storage != null) {
            group.setStorage(storage.resolve(group));
        }

        if (warpPoints != null) {
            for (WarpPointData wpd : warpPoints) {
                if (wpd != null) {
                    group.getWarpPoints().add(wpd.resolve());
                }
            }
        }

        if (this.enemies != null) {
            for (PlayerGroupData enemy : enemies) {
                PlayersGroup resolved = enemy != null ? enemy.resolve() : null;
                if (resolved != null) {
                    group.enemies.add(resolved);
                }
            }
        }

        if (this.allies != null) {
            for (PlayerGroupData ally : allies) {
                PlayersGroup resolved = ally != null ? ally.resolve() : null;
                if (resolved != null) {
                    group.allies.add(resolved);
                }
            }
        }

        if (this.pending != null) {
            for (PlayerConfigData pcd : this.pending) {
                PlayerConfig pc = pcd.resolve();
                if (pc != null) {
                    group.pending.add(pc);
                }
            }
        }

        return group;
    }
}