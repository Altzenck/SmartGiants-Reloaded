package me.jjm_223.smartgiants.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static me.jjm_223.smartgiants.LangManager.getLang;

public class CommandBase extends AbstractCommand {
    public CommandBase(final List<AbstractCommand> subCommands) {
        super("smartgiants", "smartgiants.configure", true, subCommands);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        final String typedCommand = args.length == 0 ? "" : args[0].toLowerCase();

        return getSubCommands()
                .stream()
                .filter(c -> c.getName().equalsIgnoreCase(typedCommand))
                .findFirst()
                .map(c -> c.onTabComplete(commandSender, command, s, Arrays.copyOfRange(args, 1, args.length)))
                .orElseGet(() -> getSubCommands()
                        .stream()
                        .map(AbstractCommand::getName)
                        .map(String::toLowerCase)
                        .filter(n -> n.startsWith(typedCommand))
                        .collect(Collectors.toList()));
    }

    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String s, final String[] args) {
        final String typedCommand = args.length == 0 ? "" : args[0].toLowerCase();

        return getSubCommands()
                .stream()
                .filter(c -> c.getName().equalsIgnoreCase(typedCommand))
                .findFirst()
                .map(subCommand -> {
                    if (!(sender instanceof Player) && subCommand.getPlayerRequired()) {
                        sender.sendMessage(getLang("mustBePlayer"));
                        return true;
                    }

                    if (subCommand.getPermission() != null && !sender.hasPermission(subCommand.getPermission())) {
                        sender.sendMessage(getLang("noPermission"));
                        return true;
                    }

                    final String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
                    return subCommand.onCommand(sender, command, s, subArgs);
                }).orElseGet(() -> {
                    displayUsage(sender);
                    return true;
                });
    }

    @Override
    public void displayUsage(final CommandSender sender) {
        sender.sendMessage(getLang("lectureBar"));
        getSubCommands()
                .stream()
                .map(c -> "lecture" + c.getName())
                .forEach(m -> sender.sendMessage(getLang(m)));
        sender.sendMessage(getLang("lectureBar"));
    }
}
