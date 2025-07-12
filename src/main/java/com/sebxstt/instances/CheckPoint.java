package com.sebxstt.instances;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CheckPoint {
    public Player instance;
    public Location loc;
    public String name;

    public CheckPoint(Player instance, Location loc, String name) {
        this.instance = instance;
        this.loc = loc;
        this.name = name;
    }

    public void teleport() {
        this.instance.teleport(this.loc);
    }

    public void setInstance(Player instance) {
        this.instance = instance;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getInstance() {
        return instance;
    }

    public Location getLoc() {
        return loc;
    }

    public String getName() {
        return name;
    }
}
