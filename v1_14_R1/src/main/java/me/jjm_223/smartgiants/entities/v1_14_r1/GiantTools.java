package me.jjm_223.smartgiants.entities.v1_14_r1;

import me.jjm_223.smartgiants.api.entity.ISmartGiant;
import me.jjm_223.smartgiants.api.util.IGiantTools;
import me.jjm_223.smartgiants.entities.v1_14_r1.nms.SmartGiant;
import me.jjm_223.smartgiants.entities.v1_14_r1.nms.SmartGiantHostile;
import net.minecraft.server.v1_14_R1.Entity;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.potion.PotionType;

public class GiantTools implements IGiantTools {
    @Override
    public ISmartGiant spawnGiant(Location location, boolean hostile) {
        final CraftWorld craftWorld = (CraftWorld) location.getWorld();
        if (craftWorld != null) {
            final World world = craftWorld.getHandle();
            final Entity entity = hostile ? new SmartGiantHostile(EntityTypes.GIANT, world) : new SmartGiant(EntityTypes.GIANT, world);
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
        if (isArrow(entity) && entity instanceof Arrow) {
            final Arrow arrow = (Arrow) entity;
            return arrow.getBasePotionData().getType() == PotionType.UNCRAFTABLE;
        }

        return isSpectralArrow(entity);
    }

    @Override
    public boolean isTippedArrow(final org.bukkit.entity.Entity entity) {
        return isArrow(entity) && !isSimpleArrow(entity);
    }

    private boolean isArrow(final org.bukkit.entity.Entity entity) {
        return entity.getType() == EntityType.ARROW;
    }

    private boolean isSpectralArrow(final org.bukkit.entity.Entity entity) {
        return entity.getType() == EntityType.SPECTRAL_ARROW;
    }
}