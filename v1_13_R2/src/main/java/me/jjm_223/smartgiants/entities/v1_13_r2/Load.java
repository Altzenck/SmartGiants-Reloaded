package me.jjm_223.smartgiants.entities.v1_13_r2;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import me.jjm_223.smartgiants.api.util.ILoad;
import me.jjm_223.smartgiants.entities.v1_13_r2.nms.SmartGiant;
import me.jjm_223.smartgiants.entities.v1_13_r2.nms.SmartGiantHostile;
import net.minecraft.server.v1_13_R2.*;

import java.util.Map;
import java.util.function.Function;

public class Load implements ILoad {
    static EntityTypes<SmartGiant> smartGiant;
    static EntityTypes<SmartGiantHostile> smartGiantHostile;

    public Load() {
        // Nothing to do
    }

    @Override
    public void load(boolean hostile) {
        smartGiant = injectNewEntity("smartgiant", SmartGiant.class, SmartGiant::new);
        smartGiantHostile = injectNewEntity("smartgiant_hostile", SmartGiantHostile.class, SmartGiantHostile::new);
    }

    @Override
    public void cleanup() {
        // Nothing to do
    }

    @SuppressWarnings("unchecked")
    private <E extends Entity> EntityTypes<E> injectNewEntity(String name, Class<E> clazz, Function<? super World, ? extends Entity> function) {
        // get the server's datatypes (also referred to as "data fixers")
        final Map<Object, Type<?>> dataTypes = (Map<Object, Type<?>>) DataConverterRegistry.a().getSchema(DataFixUtils.makeKey(1628)).findChoiceType(DataConverterTypes.n).types();
        // inject the new custom entity (this registers the
        // name/id with the server so you can use it in things
        // like the vanilla /summon command)
        dataTypes.put("minecraft:" + name, dataTypes.get("minecraft:giant"));
        // create and return an EntityTypes for the custom entity
        // store this somewhere so you can reference it later (like for spawning)
        return (EntityTypes<E>) EntityTypes.a(name, EntityTypes.a.a(clazz, function));
    }
}
