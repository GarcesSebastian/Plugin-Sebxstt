package com.sebxstt.serialize.data;

import com.sebxstt.functions.utils.InPlayer;
import com.sebxstt.instances.CheckPoint;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CheckPointData {
    public UUID instance;
    public String name;
    public String world;
    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;

    private CheckPointData(CheckPoint cp) {
        if (cp == null || cp.getLoc() == null || cp.getLoc().getWorld() == null || cp.instance == null) return;

        this.name = cp.getName();
        this.instance = cp.instance.getUniqueId();
        this.world = cp.getLoc().getWorld().getName();
        this.x = cp.getLoc().getX();
        this.y = cp.getLoc().getY();
        this.z = cp.getLoc().getZ();
        this.yaw = cp.getLoc().getYaw();
        this.pitch = cp.getLoc().getPitch();
    }

    public static CheckPointData create(CheckPoint cp) {
        return cp != null ? new CheckPointData(cp) : null;
    }

    public CheckPoint resolve() {
        if (instance == null || world == null || name == null) return null;

        Player p = InPlayer.instance(instance);
        if (p == null) return null;

        World w = Bukkit.getWorld(world);
        if (w == null) return null;

        Location loc = new Location(w, x, y, z, yaw, pitch);
        return new CheckPoint(p, loc, name);
    }
}