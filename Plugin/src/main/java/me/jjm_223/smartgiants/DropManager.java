package me.jjm_223.smartgiants;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class DropManager {
    private FileConfiguration config = new YamlConfiguration();
    private List<Drop> drops = new ArrayList<>();
    private File dropsFile;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private Random random = new Random();

    DropManager(final SmartGiants plugin) throws IOException {
        dropsFile = new File(plugin.getDataFolder(), Constants.DROP_FILENAME);

        final boolean created = dropsFile.createNewFile();
        if (!created) {
            try {
                config.load(dropsFile);
            } catch (InvalidConfigurationException e) {
                plugin.getLogger().log(Level.SEVERE, "Invalid configuration: '" + Constants.DROP_FILENAME + "'. Feel free to start fresh if you aren't sure" +
                        " how to fix this. You can reload this config with /smartgiants reloaddrops", e);
            }

            loadDrops();
        }
    }

    public List<ItemStack> getRandomDrops() {
        return drops
                .stream()
                .filter(this::filterRandomly)
                .map(this::getRandomAmount)
                .collect(Collectors.toList());
    }

    private boolean filterRandomly(final Drop drop) {
        return random.nextInt((int) Math.ceil(100 / drop.getPercentChance())) == 0;
    }

    private ItemStack getRandomAmount(final Drop drop) {
        final ItemStack stack = drop.getItem();
        stack.setAmount(random.nextInt(Math.max(drop.getMaxAmount() - drop.getMinAmount(), 1)) + drop.getMinAmount());

        return stack;
    }

    public boolean deleteDrop(final ItemStack item) {
        for (final Drop drop : drops) {
            if (drop.getItem().isSimilar(item)) {
                drops.remove(drop);
                queueFileSave();

                return true;
            }
        }

        return false;
    }

    public void addDrop(final Drop drop) {
        drops.add(drop);
        queueFileSave();
    }

    public void resetDrops() {
        drops.clear();
        queueFileSave();
    }

    public Collection<Drop> getDrops() {
        return Collections.unmodifiableCollection(drops);
    }

    private void updateConfig() {
        config = new YamlConfiguration();
        for (final Drop drop : drops) {
            final String base = "drops." + drops.indexOf(drop);
            config.set(base + ".itemStack", drop.getItem());
            config.set(base + ".minAmount", drop.getMinAmount());
            config.set(base + ".maxAmount", drop.getMaxAmount());
            config.set(base + ".chance", drop.getPercentChance());
        }
    }

    private void loadDrops() {
        Bukkit.getLogger().info("Load drops :");

        drops.clear();
        final ConfigurationSection section = config.getConfigurationSection("drops");
        if (section == null) {
            Bukkit.getLogger().warning("Drop file's root element not found");

            return;
        }

        for (final String string : section.getKeys(false)) {
            final String base = "drops." + string;
            final ItemStack stack = config.getItemStack(base + ".itemStack");
            final int minAmount = config.getInt(base + ".minAmount");
            final int maxAmount = config.getInt(base + ".maxAmount");
            final double percentChance = config.getDouble(base + ".chance");

            final Drop drop = new Drop(stack, minAmount, maxAmount, percentChance);

            Bukkit.getLogger().log(Level.INFO, "Loaded drop : {0}", drop.getItem().getType());

            drops.add(drop);
        }
    }

    private void queueFileSave() {
        updateConfig();
        executor.submit(() -> {
            try {
                config.save(dropsFile);
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Error while saving the drop file", e);
            }
        });
    }

    void shutdown() {
        queueFileSave();
        executor.shutdown();

        try {
            executor.awaitTermination(5L, TimeUnit.MINUTES);
        } catch (InterruptedException ignored) {
            // Not much we can do at this point
        }
    }
}
