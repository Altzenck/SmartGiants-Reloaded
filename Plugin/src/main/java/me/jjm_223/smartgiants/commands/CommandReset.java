package me.jjm_223.smartgiants.commands;

import me.jjm_223.smartgiants.SmartGiants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

import static me.jjm_223.smartgiants.LangManager.getLang;

public class CommandReset extends AbstractCommand {
    private SmartGiants plugin;

    public CommandReset(final SmartGiants plugin) {
        super("Reset", "smartgiants.configure", false, Collections.emptyList());

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        plugin.getDropManager().resetDrops();

        sender.sendMessage(getLang("allItemsRemoved"));

        return true;
    }
}
