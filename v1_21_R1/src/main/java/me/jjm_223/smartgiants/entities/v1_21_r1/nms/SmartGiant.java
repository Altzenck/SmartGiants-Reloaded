package me.jjm_223.smartgiants.entities.v1_21_r1.nms;

import me.jjm_223.smartgiants.api.entity.ISmartGiant;
import me.jjm_223.smartgiants.api.util.Configuration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftGiant;
import java.lang.reflect.Field;

public class SmartGiant extends Giant implements ISmartGiant {

    public static final String ID = "smartgiant",
    HOSTILE_ID = ID + "_hostile";


    public SmartGiant(@SuppressWarnings("unused") final EntityType<? extends Giant> entityTypes, final Level world) {
        super(EntityType.GIANT, world);
        setHealth((float) Configuration.getInstance().maxHealth());
        setCanPickUpLoot(Configuration.getInstance().canPickupLoot());
        initPathfinder();
    }

    @SuppressWarnings("unused")
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, Configuration.getInstance().maxHealth())
                .add(Attributes.MOVEMENT_SPEED, Configuration.getInstance().movementSpeed())
                .add(Attributes.FOLLOW_RANGE, Configuration.getInstance().followRange());
    }

    @Override
    public AttributeMap getAttributes() {
        return new AttributeMap(createAttributes().build());
    }

    @Override
    public boolean isHostile() {
        return this instanceof SmartGiantHostile;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource var0) {
        return SoundEvents.ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.ZOMBIE_STEP;
    }

    @Override
    protected void playStepSound(BlockPos blockposition, BlockState iblockdata) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    public void initPathfinder() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        //this.goalSelector.addGoal(1, new TemptGoal(this, 1.0D, Ingredient.of(Items.APPLE), false));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
    }

    // https://www.spigotmc.org/threads/persisting-a-custom-nms-villager-through-server-restarts-in-1-19-3.584683/
    @Override
    public void addAdditionalSaveData(CompoundTag nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putString("id", "minecraft:" + ID);
    }

    @Override
    public boolean isInvertedHealAndHarm() {
        return Configuration.getInstance().isUndead();
    }

    @Override
    public CraftEntity getBukkitEntity() {
        try {
            Field bukkitEntityField = Entity.class.getDeclaredField("bukkitEntity");
            bukkitEntityField.setAccessible(true);
            CraftEntity bukkitEntity = (CraftEntity) bukkitEntityField.get(this);
            if (bukkitEntity == null)
                bukkitEntityField.set(this, bukkitEntity = new CraftGiant(getServer().server, this));
            return bukkitEntity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
