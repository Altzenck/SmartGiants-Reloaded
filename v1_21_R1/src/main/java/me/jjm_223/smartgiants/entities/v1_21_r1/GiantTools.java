package me.jjm_223.smartgiants.entities.v1_21_r1;

import me.jjm_223.smartgiants.api.entity.ISmartGiant;
import me.jjm_223.smartgiants.api.util.IGiantTools;
import me.jjm_223.smartgiants.entities.v1_21_r1.nms.SmartGiant;
import me.jjm_223.smartgiants.entities.v1_21_r1.nms.SmartGiantHostile;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntity;
import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class GiantTools implements IGiantTools {

    @Override
    public ISmartGiant spawnGiant(Location location, boolean hostile) {
        final CraftWorld craftWorld = (CraftWorld) location.getWorld();
        if (craftWorld != null) {
            final Level world = craftWorld.getHandle();
            final SmartGiant entity = hostile ? new SmartGiantHostile(Load.smartGiantHostile, world) : new SmartGiant(Load.smartGiant, world);
            entity.setPos(location.getX(), location.getY(), location.getZ());
            world.addFreshEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
            return entity;
        }
        return null;
    }

    @Override
    public boolean isSmartGiant(final org.bukkit.entity.Entity entity) {
        return ((CraftEntity) entity).getHandle() instanceof SmartGiant;
    }

    @Override
    public boolean isSimpleArrow(final org.bukkit.entity.Entity entity) {
        if (isArrow(entity) && entity instanceof Arrow arrow)
            return arrow.getBasePotionType() == null;
        return isSpectralArrow(entity);
    }

    @Override
    public boolean isTippedArrow(final org.bukkit.entity.Entity entity) {
        return isArrow(entity) && !isSimpleArrow(entity);
    }

    private boolean isArrow(final org.bukkit.entity.Entity entity) {
        return entity.getType() == org.bukkit.entity.EntityType.ARROW;
    }

    private boolean isSpectralArrow(final org.bukkit.entity.Entity entity) {
        return entity.getType() == org.bukkit.entity.EntityType.SPECTRAL_ARROW;
    }
}