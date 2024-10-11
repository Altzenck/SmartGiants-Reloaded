package me.jjm_223.smartgiants.entities.v1_14_r1;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import me.jjm_223.smartgiants.api.util.ILoad;
import me.jjm_223.smartgiants.entities.v1_14_r1.nms.SmartGiant;
import me.jjm_223.smartgiants.entities.v1_14_r1.nms.SmartGiantHostile;
import net.minecraft.server.v1_14_R1.*;

import java.util.Map;

public class Load implements ILoad {
    static EntityTypes<SmartGiant> smartGiant;
    static EntityTypes<SmartGiantHostile> smartGiantHostile;

    public Load() {
        // Nothing to do
    }

    @Override
    @SuppressWarnings("squid:S2696")
    public void load(boolean hostile) {
        smartGiant = injectNewEntity("smartgiant", SmartGiant::new);
        smartGiantHostile = injectNewEntity("smartgiant_hostile", SmartGiantHostile::new);
    }

    @Override
    public void cleanup() {
        // Nothing to do
    }

    @SuppressWarnings("unchecked")
    private <E extends Entity> EntityTypes<E> injectNewEntity(final String name, final EntityTypes.b function) {
        final Map<String, Type<?>> types = (Map<String, Type<?>>) DataConverterRegistry.a().getSchema(DataFixUtils.makeKey(SharedConstants.a().getWorldVersion())).findChoiceType(DataConverterTypes.ENTITY).types();
        types.put("minecraft:" + name, types.get("minecraft:giant"));

        final EntityTypes.a<Entity> a = EntityTypes.a.a(function, EnumCreatureType.MONSTER);

        return (EntityTypes<E>) IRegistry.a(IRegistry.ENTITY_TYPE, name, a.a(name));
    }
}
