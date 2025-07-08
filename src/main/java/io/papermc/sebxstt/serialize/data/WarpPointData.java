package io.papermc.sebxstt.serialize.data;

import io.papermc.sebxstt.instances.WarpPoint;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.time.LocalDateTime;
import java.util.UUID;

public class WarpPointData {
    public UUID id;
    public String name;
    public String world;
    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    public UUID createdBy;
    public LocalDateTime createdAt;
    public UUID group;

    public WarpPointData() {}

    private WarpPointData(WarpPoint wp) {
        this.id = wp.id;
        this.name = wp.name;
        this.world = wp.location.getWorld().getName();
        this.x = wp.location.getX();
        this.y = wp.location.getY();
        this.z = wp.location.getZ();
        this.yaw = wp.location.getYaw();
        this.createdBy = wp.createdBy;
        this.createdAt = wp.createdAt;
        this.group = wp.group;
    }

    public static WarpPointData create(WarpPoint wp) {
        return new WarpPointData(wp);
    }

    public WarpPoint resolve() {
        if (createdBy == null || world == null
                || name == null || createdAt == null
                || group == null
        ) return null;

        World w = Bukkit.getServer().getWorld(world);
        if (w == null) return null;

        Location loc = new Location(w, x, y, z, yaw, pitch);
        WarpPoint wp = new WarpPoint(name, createdBy, group, loc);

        wp.setId(id);
        wp.setCreatedAt(createdAt);
        return wp;
    }
}
