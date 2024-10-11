package me.jjm_223.smartgiants.entities.v1_13_r2;

import me.jjm_223.smartgiants.api.entity.ISmartGiant;
import me.jjm_223.smartgiants.api.util.IGiantTools;
import me.jjm_223.smartgiants.entities.v1_13_r2.nms.SmartGiant;
import me.jjm_223.smartgiants.entities.v1_13_r2.nms.SmartGiantHostile;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class GiantTools implements IGiantTools {
    @Override
    public ISmartGiant spawnGiant(Location location, boolean hostile) {
        final CraftWorld craftWorld = (CraftWorld) location.getWorld();
        if (craftWorld != null) {
            final World world = craftWorld.getHandle();
            final Entity entity = hostile ? new SmartGiantHostile(world) : new SmartGiant(world);
            entity.setPosition(location.getX(), location.getY(), location.getZ());
            world.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        }

        return null;
    }

    @Override
    public boolean isSmartGiant(final org.bukkit.entity.Entity entity) {
        return ((CraftEntity) entity).getHandle() instanceof SmartGiant;
    }

    @Override
    public boolean isSimpleArrow(final org.bukkit.entity.Entity entity) {
        return (entity.getType() == EntityType.ARROW
                || entity.getType() == EntityType.SPECTRAL_ARROW);
    }

    @Override
    public boolean isTippedArrow(final org.bukkit.entity.Entity entity) {
        return entity.getType() == EntityType.TIPPED_ARROW;
    }
}