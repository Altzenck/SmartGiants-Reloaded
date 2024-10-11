package me.jjm_223.smartgiants;

import org.bukkit.inventory.ItemStack;

public class Drop {
    private final ItemStack item;
    private final int minAmount;
    private final int maxAmount;
    private final double percentChance;

    public Drop(ItemStack item, int minAmount, int maxAmount, double percentChance) {
        this.item = item;
        this.minAmount = minAmount > 0 ? minAmount : 1;
        this.maxAmount = maxAmount > 0 ? maxAmount : 1;
        this.percentChance = (percentChance > 0 && percentChance <= 100) ? percentChance : 100;
    }

    int getMinAmount() {
        return minAmount;
    }

    int getMaxAmount() {
        return maxAmount;
    }

    double getPercentChance() {
        return percentChance;
    }

    public ItemStack getItem() {
        return item.clone();
    }
}