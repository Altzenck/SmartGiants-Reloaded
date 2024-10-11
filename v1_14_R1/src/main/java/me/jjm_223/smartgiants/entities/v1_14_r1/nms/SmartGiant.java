package me.jjm_223.smartgiants.entities.v1_14_r1.nms;

import me.jjm_223.smartgiants.api.entity.ISmartGiant;
import me.jjm_223.smartgiants.api.util.Configuration;
import net.minecraft.server.v1_14_R1.*;

public class SmartGiant extends EntityGiantZombie implements ISmartGiant {
    public SmartGiant(@SuppressWarnings("unused") final EntityTypes<? extends EntityGiantZombie> entityTypes, final World world) {
        super(EntityTypes.GIANT, world);

        setCanPickupLoot(Configuration.getInstance().canPickupLoot());
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();

        final double health = Configuration.getInstance().maxHealth();

        this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(health);
        setHealth((float) health);

        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(Configuration.getInstance().movementSpeed());
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(Configuration.getInstance().followRange());
    }

    @Override
    public boolean isHostile() {
        return this instanceof SmartGiantHostile;
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_ZOMBIE_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_ZOMBIE_DEATH;
    }

    @Override
    protected boolean playStepSound() {
        return true;
    }

    @Override
    protected void initPathfinder() {
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalTempt(this, 1.0D, RecipeItemStack.a(Items.APPLE), false));
        this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 16.0F));
        this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
    }
}
