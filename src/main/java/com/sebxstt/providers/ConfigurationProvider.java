package com.sebxstt.providers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigurationProvider {
    public static int CooldownLastDeathCheckPoint;
    public static int MaxCheckPoints;
    public static String fileDataSaved;
    public static int AutoSaveTime;

    public static void load(JavaPlugin plugin) {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();

        CooldownLastDeathCheckPoint = config.getInt("CooldownLastDeathCheckPoint", 1);
        MaxCheckPoints = config.getInt("MaxCheckPoints", 3);
        fileDataSaved = config.getString("fileDataSaved", "data") + ".json";
        AutoSaveTime = config.getInt("AutoSaveTime", 5);
    }
}
