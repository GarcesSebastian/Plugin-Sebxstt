package io.papermc.sebxstt.serialize.data;

import io.papermc.sebxstt.instances.Main;
import io.papermc.sebxstt.instances.PlayerConfig;
import io.papermc.sebxstt.instances.PlayersGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainData {
    public List<PlayerGroupData> playersGroupData = new ArrayList<>();
    public List<PlayerConfigData> playersConfigData = new ArrayList<>();

    public MainData() {}

    private MainData(ArrayList<PlayersGroup> playersGroup, ArrayList<PlayerConfig> playersConfig) {
        playersGroup.forEach(group -> {
            PlayerGroupData pgd = PlayerGroupData.create(group);
            playersGroupData.add(pgd);
        });

        playersConfig.forEach(config -> {
           PlayerConfigData pcd = PlayerConfigData.create(config);
           playersConfigData.add(pcd);
        });
    }

    public MainData create(ArrayList<PlayersGroup> playersGroup, ArrayList<PlayerConfig> playersConfig) {
        return new MainData(playersGroup, playersConfig);
    }

    public Main resolve() {
        if (playersGroupData == null || playersConfigData == null) return null;

        ArrayList<PlayersGroup> playersGroups = new ArrayList<>();
        ArrayList<PlayerConfig> playersConfigs = new ArrayList<>();

        playersGroupData.forEach(pgd -> {
            PlayersGroup pg = pgd.resolve();
            playersGroups.add(pg);
        });

        playersConfigData.forEach(pcd -> {
            PlayerConfig pc = pcd.resolve();
            playersConfigs.add(pc);
        });

        return new Main(playersGroups, playersConfigs);
    }
}
