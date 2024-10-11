package me.jjm_223.smartgiants.entities.v1_21_r1.utils;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import java.security.SecureRandom;
import java.util.Random;

/**
 * A utility class related to spawning mobs.
 */
public class MobUtils {

    private static final Random random = new SecureRandom();

    public static boolean checkBox(int width, int height, Location loc) {
        for (int x = loc.getBlockX() - width / 2; x <= loc.getBlockX() + width / 2; x++) {
            for (int y = loc.getBlockY(); y < loc.getBlockY() + height; y++) {
                for (int z = loc.getBlockZ() - width / 2; z <= loc.getBlockZ() + width / 2; z++) {
                    if(!loc.getWorld().getBlockAt(x, y, z).isPassable()) return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks whether a mob can spawn or not.
     * @param type The type of a mob to spawn
     * @param loc The center location of the mob spawning.
     * @return Returns {@code true} if a mob can spawn, otherwise {@code false}
     */
    public static boolean canSpawn(EntityType type, Location loc) {
        return switch (type) {
            case PARROT -> checkBox(1, 1, loc);
            case PLAYER -> checkBox(1, 2, loc);
            case ELDER_GUARDIAN -> checkBox(2, 1, loc);
            case WITHER -> checkBox(2, 3, loc);
            case MAGMA_CUBE -> checkBox(3, 3, loc);
            case GHAST -> checkBox(4, 4, loc);
            case GIANT -> checkBox(4, 12, loc);
            case END_CRYSTAL -> checkBox(16, 8, loc);
            default -> true;
        };
    }

    /*
     * Tries to spawn a mob.
     * @param type The type of a mob to spawn
     * @param range The range where mobs can spawn
     * @param world The world to spawn
     * @return Returns {@link Entity} if a mob could spawn, otherwise {@code null}

    public static Entity tryRandomSpawn(EntityType type, Range range, World world) {
        int randomX = random.nextInt(range.getXLength()) + range.getMinPoint().getX();
        int randomY = random.nextInt(range.getYLength()) + range.getMinPoint().getY();
        int randomZ = random.nextInt(range.getZLength()) + range.getMinPoint().getZ();
        Location loc = new Location(world, randomX, randomY, randomZ);
        if (!canSpawn(type, loc)) return null;
        return world.spawnEntity(loc, type);
    }
    */

}
