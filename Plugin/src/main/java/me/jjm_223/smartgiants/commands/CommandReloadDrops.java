package me.jjm_223.smartgiants.commands;

import me.jjm_223.smartgiants.SmartGiants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

import static me.jjm_223.smartgiants.LangManager.getLang;

public class CommandReloadDrops extends AbstractCommand {
    private SmartGiants plugin;

    public CommandReloadDrops(final SmartGiants plugin) {
        super("ReloadDrops", "smartgiants.configure", false, Collections.emptyList());

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (plugin.reloadDrops()) {
            sender.sendMessage(getLang("dropsReloaded"));
        } else {
            sender.sendMessage(getLang("dropsFailedReload"));
        }

        return true;
    }
}
