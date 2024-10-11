package me.jjm_223.smartgiants.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static me.jjm_223.smartgiants.LangManager.getLang;

public abstract class AbstractCommand implements CommandExecutor, TabCompleter {
    private List<AbstractCommand> subCommands = new ArrayList<>();
    private final String name;
    private final String permission;
    private final boolean playerOnly;

    AbstractCommand(final String name, final String permission, final boolean playerOnly, final List<AbstractCommand> subCommands) {
        this.name = name;
        this.permission = permission;
        this.playerOnly = playerOnly;
        this.subCommands.addAll(subCommands);
    }

    String getName() {
        return name;
    }

    String getPermission() {
        return permission;
    }

    boolean getPlayerRequired() {
        return playerOnly;
    }

    List<AbstractCommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return args.length == 0
                ? Collections.emptyList()
                : Collections.singletonList("too many arguments");
    }

    public void displayUsage(CommandSender sender) {
        sender.sendMessage(getLang("lectureBar"));
        sender.sendMessage(getLang("lecture" + getName()));
        sender.sendMessage(getLang("lectureBar"));
    }
}
