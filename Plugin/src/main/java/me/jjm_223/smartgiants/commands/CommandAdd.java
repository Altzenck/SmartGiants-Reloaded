package me.jjm_223.smartgiants.commands;

import me.jjm_223.smartgiants.Drop;
import me.jjm_223.smartgiants.SmartGiants;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

import static me.jjm_223.smartgiants.LangManager.getLang;

public class CommandAdd extends AbstractCommand {
    private SmartGiants plugin;

    public CommandAdd(final SmartGiants plugin) {
        super("Add", "smartgiants.configure", true, Collections.emptyList());

        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings("squid:S3358")
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return Collections.singletonList(args.length == 1 ? "<min>"
                : args.length == 2 ? "<max>"
                : args.length == 3 ? "<dropPercent>"
                : "too many arguments");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        final Player player = (Player) sender;
        final ItemStack stackInHand = player.getInventory().getItemInMainHand().clone();

        if (stackInHand.getType() == Material.AIR) {
            sender.sendMessage(getLang("noItemInHand"));
        } else {
            try {
                final int min = Integer.parseInt(args[0]);
                final int max = Integer.parseInt(args[1]);
                final int percent = Integer.parseInt(args[2]);

                plugin.getDropManager().addDrop(new Drop(stackInHand, min, max, percent));

                sender.sendMessage(getLang("dropAdded"));
            } catch (NumberFormatException e) {
                sender.sendMessage(getLang("lectureAdd"));
            }
        }

        return true;
    }
}
