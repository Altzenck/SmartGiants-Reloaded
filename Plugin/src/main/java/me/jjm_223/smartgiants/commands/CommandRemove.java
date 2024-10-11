package me.jjm_223.smartgiants.commands;

import me.jjm_223.smartgiants.SmartGiants;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

import static me.jjm_223.smartgiants.LangManager.getLang;

public class CommandRemove extends AbstractCommand {
    private SmartGiants plugin;

    public CommandRemove(final SmartGiants plugin) {
        super("Remove", "smartgiants.configure", true, Collections.emptyList());

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        final Player player = (Player) sender;
        final ItemStack stackInHand = player.getInventory().getItemInMainHand();

        if (stackInHand.getType() == Material.AIR) {
            player.sendMessage(getLang("noItemInHand"));
        } else {
            final ItemStack itemToRemove = stackInHand.clone();

            if (!plugin.getDropManager().deleteDrop(itemToRemove)) {
                sender.sendMessage(getLang("dropNoExist"));
            } else {
                player.sendMessage(getLang("dropRemoved"));
            }
        }

        return true;
    }
}
