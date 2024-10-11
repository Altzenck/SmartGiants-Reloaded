package me.jjm_223.smartgiants.entities.v1_21_r1;

import me.altzenck.utils.RandomElement;
import me.altzenck.utils.RandomUtils;
import me.jjm_223.smartgiants.api.util.IGiantTools;
import me.jjm_223.smartgiants.api.util.INaturalSpawns;
import me.jjm_223.smartgiants.api.util.ReflectionUtils;
import me.jjm_223.smartgiants.entities.v1_21_r1.utils.MobUtils;
import org.bukkit.*;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import java.security.SecureRandom;
import java.util.Random;

public class NaturalSpawns implements INaturalSpawns {
    private boolean hostile;
    private boolean daylight;
    private float frequency;
    private int minGroupAmount;
    private int maxGroupAmount;

    private static final Random RANDOM = new SecureRandom();

    private IGiantTools giantTools;

    @Override
    public void load(boolean hostile, boolean daylight, float frequency, int minGroupAmount, int maxGroupAmount) {
        this.hostile = hostile;
        this.daylight = daylight;
        this.frequency = frequency;
        this.minGroupAmount = minGroupAmount;
        this.maxGroupAmount = maxGroupAmount;
    }

    IGiantTools getGiantTools() {
        return (giantTools == null)? giantTools = (IGiantTools) ReflectionUtils.getDeclaredField(PluginAccessor.getPlugin(), null, "giantTools").get() : giantTools;
    }

    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Location loc = event.getLocation();
        World world = loc.getWorld();
        if(world.getEnvironment() != World.Environment.NORMAL || world.getDifficulty() == Difficulty.PEACEFUL) return;
        Entity entity = event.getEntity();
        switch (entity.getType()) {
            case ZOMBIE:
            case ZOMBIE_VILLAGER:
            case HUSK:
            case DROWNED:
                break;
            default:
                if(daylight && isDay(world) && entity instanceof Animals)
                    break;
                return;
        }
        if(event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) return;
        float rn = RANDOM.nextFloat() * 100.0f;
        if(rn < frequency) {
            if(!MobUtils.canSpawn(EntityType.GIANT, loc)) return;
            event.setCancelled(true);
            int amount = 0;
            if (minGroupAmount <= maxGroupAmount) {
                int nxt = minGroupAmount + 1, rdn = (minGroupAmount == maxGroupAmount || nxt == maxGroupAmount)? maxGroupAmount : RANDOM.nextInt(nxt, maxGroupAmount);
                amount = (minGroupAmount == rdn)? rdn : RandomUtils.randomPercentaje(RandomElement.of(minGroupAmount, 50), RandomElement.of(rdn, 50));
            }
            for(;amount > 0; amount--)
                getGiantTools().spawnGiant(loc, hostile);
        }
    }

    private static boolean isDay(World world) {
        long time = world.getTime();
        return time >= 0 && time < 12300;
    }

    @Override
    public void cleanup() {
    }
}
