package me.jjm_223.smartgiants.commands;

import me.jjm_223.smartgiants.SmartGiants;
import me.jjm_223.smartgiants.api.util.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;

import static me.jjm_223.smartgiants.LangManager.getLang;

public class CommandReloadConfig extends AbstractCommand {
    public CommandReloadConfig() {
        super("ReloadConfig", "smartgiants.configure", false, Collections.emptyList());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        try {
            Configuration.getInstance().reload();
            JavaPlugin.getPlugin(SmartGiants.class).tryLoadNaturalSpawns(true);
            sender.sendMessage(getLang("configReloadSuccess"));
        } catch (IOException | InvalidConfigurationException e) {
            Bukkit.getLogger().warning(e.getMessage());
            sender.sendMessage(getLang("configReloadFail"));
        }
        return true;
    }
}
