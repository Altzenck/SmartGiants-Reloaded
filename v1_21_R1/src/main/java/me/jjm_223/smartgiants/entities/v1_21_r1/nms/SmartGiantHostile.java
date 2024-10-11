package me.jjm_223.smartgiants.entities.v1_21_r1.nms;

import me.jjm_223.smartgiants.api.util.Configuration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;


public class SmartGiantHostile extends SmartGiant {

    public SmartGiantHostile(final EntityType<? extends Giant> entityTypes, final Level world) {
        super(entityTypes, world);
    }


    @SuppressWarnings("unused")
    public static AttributeSupplier.Builder createAttributes() {
        return SmartGiant.createAttributes()
                .add(Attributes.ATTACK_DAMAGE, Configuration.getInstance().attackDamage());
    }

    @Override
    public AttributeMap getAttributes() {
        return new AttributeMap(createAttributes().build());
    }

    @Override
    public void initPathfinder() {
        super.initPathfinder();
        this.goalSelector.addGoal(2, new GiantAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(ZombifiedPiglin.class));
        if(Configuration.getInstance().canAttackVillagers()) {
            this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0, true, 4, () -> false));
            this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        }
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        if(Configuration.getInstance().canAttackTurtles()) {
            this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
            this.goalSelector.addGoal(4, new ZombieAttackTurtleEggGoal(this, 1.0, 3));
        }
    }

    // https://www.spigotmc.org/threads/persisting-a-custom-nms-villager-through-server-restarts-in-1-19-3.584683/
    @Override
    public void addAdditionalSaveData(CompoundTag nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putString("id", "minecraft:" + HOSTILE_ID);
    }

    // Goal implementation from Zombie Class
    private class ZombieAttackTurtleEggGoal extends RemoveBlockGoal {
        ZombieAttackTurtleEggGoal(final PathfinderMob entitycreature, final double d0, final int i) {
            super(Blocks.TURTLE_EGG, entitycreature, d0, i);
        }

        public void playDestroyProgressSound(LevelAccessor generatoraccess, BlockPos blockposition) {
            generatoraccess.playSound(null, blockposition, SoundEvents.ZOMBIE_DESTROY_EGG, SoundSource.HOSTILE, 0.5F, 0.9F + SmartGiantHostile.this.random.nextFloat() * 0.2F);
        }

        public void playBreakSound(Level world, BlockPos blockposition) {
            world.playSound(null, blockposition, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + world.random.nextFloat() * 0.2F);
        }

        public double acceptedDistance() {
            return 1.14;
        }
    }

    // Goal implementation from Zombie Class
    public static class GiantAttackGoal extends MeleeAttackGoal {
        private final Giant giant;
        private int raiseArmTicks;

        public GiantAttackGoal(Giant var0, double var1, boolean var3) {
            super(var0, var1, var3);
            this.giant = var0;
        }

        public void start() {
            super.start();
            this.raiseArmTicks = 0;
        }

        public void stop() {
            super.stop();
            this.giant.setAggressive(false);
        }

        public void tick() {
            super.tick();
            ++this.raiseArmTicks;
            this.giant.setAggressive(this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2);
        }
    }
}
