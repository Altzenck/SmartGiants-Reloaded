package me.jjm_223.smartgiants.entities.v1_21_r1.utils;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;

import java.util.NoSuchElementException;

public class Utils {

    @SuppressWarnings("unchecked")
    public static <G extends Goal> G getGoal(GoalSelector goalSelector, Class<G> clazz) {
        for(WrappedGoal wg : goalSelector.getAvailableGoals()) {
            Goal goal = wg.getGoal();
            if(clazz.isAssignableFrom(goal.getClass()))
                return (G) goal;
        }
        throw new NoSuchElementException("Goal with class member " + clazz.getName());
    }
}
