package com.sebxstt.providers;

import com.sebxstt.serialize.DataStore;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginProvider {
    public static String[] optionsInvitations = new String[] {"aceptar", "rechazar"};
    public static String[] optionsStates = new String[] {"ON", "OFF"};

    private static JavaPlugin plugin;

    public static void init(JavaPlugin pluginInstance) {
        plugin = pluginInstance;
    }

    public static JavaPlugin get() {
        return plugin;
    }
}
