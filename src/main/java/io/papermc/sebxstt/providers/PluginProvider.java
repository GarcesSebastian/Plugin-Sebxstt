package io.papermc.sebxstt.providers;

import io.papermc.sebxstt.serialize.DataStore;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginProvider {
    public static String[] optionsInvitations = new String[] {"aceptar", "rechazar"};

    private static JavaPlugin plugin;

    public static void init(JavaPlugin pluginInstance) {
        plugin = pluginInstance;
    }

    public static JavaPlugin get() {
        return plugin;
    }
}
