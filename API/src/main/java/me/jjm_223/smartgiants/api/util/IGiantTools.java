package me.jjm_223.smartgiants.api.util;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import me.jjm_223.smartgiants.api.entity.ISmartGiant;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface IGiantTools {

    @CanIgnoreReturnValue
    ISmartGiant spawnGiant(Location location, boolean hostile);

    boolean isSmartGiant(Entity entity);

    boolean isSimpleArrow(Entity entity);

    boolean isTippedArrow(Entity entity);
}
