package com.sebxstt.providers;

import com.sebxstt.instances.PlayerConfig;
import com.sebxstt.instances.PlayersGroup;
import com.sebxstt.serialize.DataStore;
import com.sebxstt.serialize.data.PlayerConfigData;
import com.sebxstt.serialize.data.PlayerGroupData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

import static com.sebxstt.index.mainData;
import static com.sebxstt.providers.ConfigurationProvider.AutoSaveTime;

public class DataStoreProvider {
    public static final DataStore DS = DataStore.getInstance();

    public static void init(Plugin plugin) {
        System.out.println("[DataStore] AutoSave Run");

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            ArrayList<PlayerConfig> MPC = mainData.playerConfigs;
            ArrayList<PlayersGroup> MPG = mainData.PlayersGroups();

            if (MPC == null || MPG == null) return;

            MPC.forEach(pc -> {
                DS.edit("id", pc.id.toString(), PlayerConfigData.create(pc), PlayerConfigData.class);
            });

            MPG.forEach(pg -> {
                DS.edit("id", pg.id.toString(), PlayerGroupData.create(pg), PlayerGroupData.class);
            });

            System.out.println("[DataStore] AutoSave");
        }, 0L, AutoSaveTime * 60 * 20L);
    }
}
