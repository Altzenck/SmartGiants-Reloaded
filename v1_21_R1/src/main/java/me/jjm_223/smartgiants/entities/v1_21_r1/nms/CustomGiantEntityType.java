package me.jjm_223.smartgiants.entities.v1_21_r1.nms;

import com.google.common.collect.ImmutableSet;
import me.jjm_223.smartgiants.api.util.ReflectionUtils;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.lang.reflect.Field;

public class CustomGiantEntityType extends EntityType<Giant> {

    public final Builder<?> builder;

    @SuppressWarnings("unchecked")
    private CustomGiantEntityType(Builder<?> builder, EntityFactory factory, MobCategory category, boolean serialize, boolean summon, boolean fireImmune, boolean canSpawnFarFromPlayer, ImmutableSet<Block> immuneTo, EntityDimensions dimensions, float spawnDimensionScale, int clientTrackingRange, int updateInterval, FeatureFlagSet featureflagset) {
        super(factory, category, serialize, summon, fireImmune, canSpawnFarFromPlayer, immuneTo, dimensions, spawnDimensionScale, clientTrackingRange, updateInterval, featureflagset);
        this.builder = builder;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <G extends Giant> CustomGiantEntityType of(final EntityType.EntityFactory<G> function) {
        try {
            // Builder implementation from EntityType.GIANT
            final Builder<Entity> builder = EntityType.Builder.of(function, MobCategory.MONSTER).sized(3.6F, 12.0F).eyeHeight(10.44F).ridingOffset(-3.75F).clientTrackingRange(10).canSpawnFarFromPlayer();

            final Class<EntityType.Builder> clazz = EntityType.Builder.class;
            final Field factoryField = clazz.getDeclaredField("factory"),
                    immuneToField = clazz.getDeclaredField("immuneTo"),
                    dimensionsField = clazz.getDeclaredField("dimensions"),
                    attachmentsField = clazz.getDeclaredField("attachments"),
                    spawnDimensionsScaleField = clazz.getDeclaredField("spawnDimensionsScale"),
                    clientTrackingRangeField = clazz.getDeclaredField("clientTrackingRange");
            ReflectionUtils.setAccessible(factoryField, immuneToField, dimensionsField, attachmentsField, spawnDimensionsScaleField, clientTrackingRangeField);
            return new CustomGiantEntityType(
                    builder,
                    (EntityFactory) factoryField.get(builder),
                    MobCategory.MONSTER,
                    false,
                    true,
                    false,
                    true,
                    (ImmutableSet<Block>) immuneToField.get(builder),
                    ((EntityDimensions) dimensionsField.get(builder)).withAttachments((EntityAttachments.Builder) attachmentsField.get(builder)),
                    (float) spawnDimensionsScaleField.get(builder),
                    (int) clientTrackingRangeField.get(builder),
                    3,
                    FeatureFlags.VANILLA_SET
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
