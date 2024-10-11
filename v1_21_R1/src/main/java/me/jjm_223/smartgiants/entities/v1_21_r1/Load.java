package me.jjm_223.smartgiants.entities.v1_21_r1;

import me.jjm_223.smartgiants.api.util.ILoad;
import me.jjm_223.smartgiants.entities.v1_21_r1.nms.SmartGiant;
import me.jjm_223.smartgiants.entities.v1_21_r1.nms.CustomGiantEntityType;
import me.jjm_223.smartgiants.entities.v1_21_r1.nms.SmartGiantHostile;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R1.CraftServer;
import org.bukkit.event.HandlerList;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
/*
import java.util.Map;
import net.minecraft.SharedConstants;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.References;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Registry;
*/

public class Load implements ILoad {
    static EntityType<SmartGiant> smartGiant;
    static EntityType<SmartGiantHostile> smartGiantHostile;

    public Load() {
        // Nothing to do
    }

    @Override
    @SuppressWarnings("squid:S2696")
    public void load(boolean hostile) {
        smartGiant = injectNewEntity(SmartGiant.ID, SmartGiant::new);
        smartGiantHostile = injectNewEntity(SmartGiant.HOSTILE_ID, SmartGiantHostile::new);
    }

    @Override
    public void enable() {
        new SmartGiantListener();
    }

    @Override
    public void cleanup() {
        HandlerList.unregisterAll(PluginAccessor.getPlugin());
    }

    /*
    @SuppressWarnings("unchecked")
    private <E extends Entity> EntityType<E> injectNewEntity(String name, final EntityType.EntityFactory<E> function) {
        final Map<String, Type<?>> types = (Map<String, Type<?>>) DataFixers.getDataFixer().getSchema(DataFixUtils.makeKey(SharedConstants.getProtocolVersion())).findChoiceType(References.ENTITY).types();
        types.put(name = ("minecraft:" + name), types.get("minecraft:giant"));
        final EntityType.Builder<Entity> a = EntityType.Builder.of(function, MobCategory.MONSTER);
        return (EntityType<E>) Registry.register(BuiltInRegistries.ENTITY_TYPE, name, a.build(name));
    }
    */

    @SuppressWarnings("unchecked")
    private <G extends Entity> EntityType<G> injectNewEntity(final String name, final EntityType.EntityFactory<G> function) {
        // Get entity type registry
        CraftServer server = ((CraftServer) Bukkit.getServer());
        DedicatedServer dedicatedServer = server.getServer();
        WritableRegistry<EntityType<?>> entityTypeRegistry = (WritableRegistry<EntityType<?>>)
                dedicatedServer.registryAccess().registryOrThrow(Registries.ENTITY_TYPE);

        try {
            // Unfreeze registry
            // Unfreezing entity type registry (1/2)...
            // l = private boolean frozen
            Field frozen = MappedRegistry.class.getDeclaredField("frozen");
            frozen.setAccessible(true);
            frozen.set(entityTypeRegistry, false);

            // Unfreezing entity type registry (2/2)...
            // m = private Map<T, Holder.Reference<T>> unregisteredIntrusiveHolders;
            Field unregisteredHolderMap = MappedRegistry.class.getDeclaredField("unregisteredIntrusiveHolders");
            unregisteredHolderMap.setAccessible(true);
            unregisteredHolderMap.set(BuiltInRegistries.ENTITY_TYPE, new HashMap<>());

            // Get Custom Entity Type
            EntityType.EntityFactory<SmartGiant> func = (EntityType.EntityFactory<SmartGiant>) function;
            CustomGiantEntityType entity = CustomGiantEntityType.of(func);

            // Build entity
            EntityType.Builder<?> builder = entity.builder;

            // Create intrusive holder
            entityTypeRegistry.createIntrusiveHolder(entity);

            // Register custom entity
            ResourceKey<EntityType<?>> newKey = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("settlements", name));
            entityTypeRegistry.register(newKey, entity, RegistrationInfo.BUILT_IN);

            // a = private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder builder)
            Method register = EntityType.class.getDeclaredMethod("register", String.class, EntityType.Builder.class);
            register.setAccessible(true);
            register.invoke(null, name, builder);

            // Re-freeze registry
            // Re-freezing entity type registry...
            frozen.set(entityTypeRegistry, true);
            unregisteredHolderMap.set(BuiltInRegistries.ENTITY_TYPE, null);
            return (EntityType<G>) entity;
        } catch (InvocationTargetException|NoSuchFieldException|NoSuchMethodException|IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
