package me.jjm_223.smartgiants;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LangManager {
    private static Logger logger;
    private static FileConfiguration config;
    private static FileConfiguration fallback;

    private LangManager() {
        // Hide the public constructor
    }

    static void loadMessages(final SmartGiants plugin) {
        logger = JavaPlugin.getPlugin(SmartGiants.class).getLogger();

        final File configFile = new File(plugin.getDataFolder(), Constants.LANG_FILENAME);

        fallback = initFallback();
        config = initConfig(configFile);

        updateConfigWithFallback(config, fallback, configFile);
    }

    private static FileConfiguration initFallback() {
        final YamlConfiguration conf = new YamlConfiguration();

        try (final InputStream stream = LangManager.class.getClassLoader().getResourceAsStream(Constants.LANG_FILENAME)) {
            if (stream != null) {
                try (final InputStreamReader streamReader = new InputStreamReader(stream)) {
                    conf.load(streamReader);
                }
            }
        } catch (IOException | InvalidConfigurationException e) {
            logger.log(Level.SEVERE, "Failed to read bundled lang file!", e);
        }

        return conf;
    }

    private static FileConfiguration initConfig(final File configFile) {
        final YamlConfiguration conf = new YamlConfiguration();
        try {
            conf.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            logger.log(Level.SEVERE, "Failed to read current language file.", e);
        }

        return conf;
    }

    private static void updateConfigWithFallback(
            final FileConfiguration config,
            final FileConfiguration fallback, File configFile) {
        try {
            for (final String key : fallback.getKeys(true)) {
                if (!config.contains(key)) {
                    config.set(key, fallback.getString(key));
                }
            }

            config.save(configFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to update current language file.", e);
        }
    }

    public static String getLang(final String section, Object... objects) {
        final String string = Optional
                .ofNullable(config.getString(section))
                .orElse(fallback.getString(section));

        return string == null ? "" : ChatColor.translateAlternateColorCodes('&', String.format(string, objects));
    }
}
