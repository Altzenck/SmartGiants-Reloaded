package me.jjm_223.smartgiants.entities.v1_21_r1;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

class PluginAccessor {

    private static Plugin plugin;

    static Plugin getPlugin() {
        if(plugin == null)
            plugin = Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("SmartGiants"));
        return plugin;
    }
}
