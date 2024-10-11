package me.jjm_223.smartgiants.entities.v1_21_r1;

import me.jjm_223.smartgiants.api.util.Configuration;
import me.jjm_223.smartgiants.api.util.INaturalSpawns;
import me.jjm_223.smartgiants.api.util.ReflectionUtils;
import me.jjm_223.smartgiants.entities.v1_21_r1.nms.SmartGiant;
import me.jjm_223.smartgiants.entities.v1_21_r1.nms.SmartGiantHostile;
import me.jjm_223.smartgiants.entities.v1_21_r1.utils.Utils;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftAbstractVillager;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftIronGolem;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftWither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import java.util.function.Predicate;

public class SmartGiantListener implements Listener {

    private static final Predicate<LivingEntity> MODIFIED_WITHER_LIVING_ENTITY_SELECTOR = (e) -> {
        if (e.getType() == EntityType.GIANT) return false;
        return !e.getType().is(EntityTypeTags.WITHER_FRIENDS) && e.attackable();
    },
    MODIFIED_IRONGOLEM_ENTITY_SELECTOR = (e) ->
        e instanceof Enemy
            && !(e instanceof Creeper)
            && (!(e instanceof SmartGiant) || e instanceof SmartGiantHostile)
    ;

    SmartGiantListener() {
        Bukkit.getPluginManager().registerEvents(this, PluginAccessor.getPlugin());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        org.bukkit.entity.LivingEntity entity = e.getEntity();
        switch (entity) {
            case CraftWither cw when Configuration.getInstance().isUndead() -> {
                modifyWitherTargets(cw.getHandle());
                return;
            }
            case CraftIronGolem cig -> {
                modifyIronGolemTargets(cig.getHandle());
                return;
            }
            case CraftAbstractVillager ci -> {
                ci.getHandle().goalSelector.addGoal(1, new AvoidEntityGoal<>(ci.getHandle(), SmartGiantHostile.class, 8.0F, 0.5, 0.5));
                return;
            }
            default -> {
            }
        }
        ReflectionUtils.FieldReference<INaturalSpawns> naturalSpawns = ReflectionUtils.getDeclaredField(PluginAccessor.getPlugin(), null, "naturalSpawns");
        if(!naturalSpawns.isNull())
            ((NaturalSpawns) naturalSpawns.get()).onCreatureSpawn(e);
    }

    private static void modifyWitherTargets(WitherBoss wb) {
        ReflectionUtils.FieldReference<TargetingConditions> targetingConditions = ReflectionUtils.getDeclaredField(wb, WitherBoss.class, "TARGETING_CONDITIONS");
        targetingConditions.get().selector(MODIFIED_WITHER_LIVING_ENTITY_SELECTOR);
        NearestAttackableTargetGoal<?> nearestAttackableTargetGoal = Utils.getGoal(wb.targetSelector, NearestAttackableTargetGoal.class);
        //RangedAttackGoal rangedAttackGoal = Utils.getGoal(wb.goalSelector, RangedAttackGoal.class);
        ReflectionUtils.FieldReference<TargetingConditions> goalTargetingConditions = ReflectionUtils.getDeclaredField(nearestAttackableTargetGoal, NearestAttackableTargetGoal.class, "targetConditions");
        goalTargetingConditions.get().selector(MODIFIED_WITHER_LIVING_ENTITY_SELECTOR);
    }

    private static void modifyIronGolemTargets(IronGolem ig) {
        NearestAttackableTargetGoal<?> nearestAttackableTargetGoal = Utils.getGoal(ig.targetSelector, NearestAttackableTargetGoal.class);
        ReflectionUtils.FieldReference<TargetingConditions> goalTargetingConditions = ReflectionUtils.getDeclaredField(nearestAttackableTargetGoal, NearestAttackableTargetGoal.class, "targetConditions");
        goalTargetingConditions.get().selector(MODIFIED_IRONGOLEM_ENTITY_SELECTOR);
    }
}
