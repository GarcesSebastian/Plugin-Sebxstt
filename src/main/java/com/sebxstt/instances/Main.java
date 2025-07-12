package com.sebxstt.instances;

import java.util.ArrayList;

public class Main {
    public ArrayList<PlayersGroup> playersGroups = new ArrayList<>();
    public ArrayList<PlayerConfig> playerConfigs = new ArrayList<>();

    public Main() {
        ArrayList<PlayersGroup> playersGroups = new ArrayList<>();
        ArrayList<PlayerConfig> playerConfigs = new ArrayList<>();
        this.playersGroups = playersGroups;
        this.playerConfigs = playerConfigs;
    }

    public Main(ArrayList<PlayersGroup> playersGroups, ArrayList<PlayerConfig> playerConfigs) {
        this.playersGroups = playersGroups;
        this.playerConfigs = playerConfigs;
    }

    public ArrayList<PlayersGroup> PlayersGroups() {
        return this.playersGroups;
    }

    public ArrayList<PlayerConfig> PlayerConfigs() {
        return this.playerConfigs;
    }
}
