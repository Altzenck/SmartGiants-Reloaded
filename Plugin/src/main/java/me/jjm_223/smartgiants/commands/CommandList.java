package me.jjm_223.smartgiants.commands;

import me.jjm_223.smartgiants.LangManager;
import me.jjm_223.smartgiants.SmartGiants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class CommandList extends AbstractCommand {
    private SmartGiants plugin;

    public CommandList(final SmartGiants plugin) {
        super("List", "smartgiants.configure", true, Collections.emptyList());

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        plugin.getDropManager()
                .getDrops()
                .forEach(d -> sender.sendMessage(LangManager.getLang("dropDisplay", d.getItem())));

        return true;
    }
}
