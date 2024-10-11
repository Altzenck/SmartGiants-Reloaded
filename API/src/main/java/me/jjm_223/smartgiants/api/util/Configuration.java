package me.jjm_223.smartgiants.api.util;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Configuration {
    private static Configuration instance;

    private final File file;
    private final FileConfiguration config;

    private Configuration(File file) throws IOException, InvalidConfigurationException {
        this.file = file;
        this.config = new YamlConfiguration();
        this.config.load(this.file);
    }

    public boolean isHostile() {
        verifyLoaded();
        return config.getBoolean("isHostile", false);
    }

    public boolean isUndead() {
        verifyLoaded();
        return config.getBoolean("isUndead", true);
    }

    public double attackDamage() {
        verifyLoaded();
        return config.getDouble("attackDamage", 5.0);
    }

    public boolean damageObeyGameDifficulty() {
        verifyLoaded();
        return config.getBoolean("damageObeyGameDifficulty", true);
    }

    public boolean handleDrops() {
        verifyLoaded();
        return config.getBoolean("handleDrops", true);
    }

    public boolean canDropEquipment() {
        verifyLoaded();
        return config.getBoolean("canDropEquipment", false);
    }

    public double movementSpeed() {
        verifyLoaded();
        return config.getDouble("movementSpeed", 0.3);
    }

    public double followRange() {
        verifyLoaded();
        return config.getDouble("followRange", 16.0);
    }

    public double maxHealth() {
        verifyLoaded();
        return config.getDouble("maxHealth", 100.0);
    }

    public boolean takeArrowDamage() {
        verifyLoaded();
        return config.getBoolean("takeArrowDamage", true);
    }

    public boolean takeTippedArrowDamage() {
        verifyLoaded();
        return config.getBoolean("takeTippedArrowDamage", true);
    }

    public boolean canPickupLoot() {
        verifyLoaded();
        return config.getBoolean("canPickupLoot", false);
    }

    public boolean naturalSpawns() {
        verifyLoaded();
        return config.getBoolean("naturalSpawns", false);
    }

    public float frequency() {
        verifyLoaded();
        return (float) config.getDouble("frequency", 5);
    }

    public int minGroupAmount() {
        verifyLoaded();
        return config.getInt("minGroupAmount", 1);
    }

    public int maxGroupAmount() {
        verifyLoaded();
        return config.getInt("maxGroupAmount", 1);
    }

    @SuppressWarnings("unused")
    public boolean daylight() {
        verifyLoaded();
        return config.getBoolean("daylight", false);
    }

    public boolean canAttackVillagers() {
        verifyLoaded();
        return config.getBoolean("canAttackVillagers", false);
    }

    public boolean canAttackTurtles() {
        verifyLoaded();
        return config.getBoolean("canAttackTurtles", false);
    }

    public List<String> worlds() {
        verifyLoaded();
        return config.getStringList("worlds");
    }

    private void verifyLoaded() {
        if (instance == null) {
            throw new IllegalStateException("Config hasn't been loaded yet. Report this to the mod author.");
        }
    }

    public void reload() throws IOException, InvalidConfigurationException {
        this.config.load(this.file);
    }

    @CanIgnoreReturnValue
    public static Configuration load(File file) throws IOException, InvalidConfigurationException {
        if (instance != null) {
            throw new IllegalStateException("Config has already been loaded. Report this to the mod author.");
        }

        instance = new Configuration(file);

        return instance;
    }

    public static Configuration getInstance() {
        return instance;
    }
}
