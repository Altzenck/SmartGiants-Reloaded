package me.jjm_223.smartgiants.listeners;

import me.jjm_223.smartgiants.SmartGiants;
import me.jjm_223.smartgiants.api.util.Configuration;
import me.jjm_223.smartgiants.entities.v1_21_r1.nms.SmartGiant;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityListener implements Listener {
    private SmartGiants plugin;

    public EntityListener(SmartGiants plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(final EntityDeathEvent event) {
        if (event.getEntityType() == EntityType.GIANT && Configuration.getInstance().handleDrops()) {
            event.getDrops().clear();
            event.getDrops().addAll(plugin.getDropManager().getRandomDrops());
            event.getDrops().addAll(getEquipment(event.getEntity().getEquipment()));
        }
    }

    private Collection<ItemStack> getEquipment(final EntityEquipment equipment) {
        if (equipment == null || !Configuration.getInstance().canDropEquipment()) {
            return Collections.emptyList();
        }

        return Stream.of(equipment.getHelmet(),
                equipment.getChestplate(),
                equipment.getLeggings(),
                equipment.getBoots(),
                equipment.getItemInMainHand(),
                equipment.getItemInOffHand())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSpawn(final CreatureSpawnEvent event) {
        final SpawnReason reason = event.getSpawnReason();
        if ((reason == SpawnReason.CHUNK_GEN || reason == SpawnReason.NATURAL)
                && event.getEntityType() == EntityType.GIANT
                && !containsIgnoreCase(Configuration.getInstance().worlds(), event.getEntity().getWorld().getName())) {
            event.setCancelled(true);
        }
    }

    private boolean containsIgnoreCase(final @Nonnull List<String> sourceList, final @Nonnull String search) {
        for (final String string : sourceList) {
            if (search.equalsIgnoreCase(string)) {
                return true;
            }
        }

        return false;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPluginSpawn(final CreatureSpawnEvent event) {
        if (plugin.getGiantTools().isSmartGiant(event.getEntity()) || event.getEntityType() != EntityType.GIANT) {
            return;
        }
        event.setCancelled(true);
        plugin.getGiantTools().spawnGiant(event.getLocation(), Configuration.getInstance().isHostile());
    }

    @EventHandler(ignoreCancelled = true)
    public void onGiantDamage(final EntityDamageByEntityEvent event) {
        if (!Configuration.getInstance().damageObeyGameDifficulty()
                && plugin.getGiantTools().isSmartGiant(event.getDamager())) {
            event.setDamage(Configuration.getInstance().attackDamage());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityCombust(final EntityCombustByEntityEvent event) {
        // Disable combustion from arrows
        event.setCancelled(shouldProtectFromArrow(event.getEntity(), event.getCombuster()));
    }

    @EventHandler(ignoreCancelled = true)
    public void onArrowDamage(final EntityDamageByEntityEvent event) {
        event.setCancelled(shouldProtectFromArrow(event.getEntity(), event.getDamager()));
    }

    private boolean shouldProtectFromArrow(final Entity damagedEntity, final Entity damagerEntity) {
        final Configuration config = Configuration.getInstance();

        return plugin.getGiantTools().isSmartGiant(damagedEntity)
                && ((plugin.getGiantTools().isSimpleArrow(damagerEntity) && !config.takeArrowDamage())
                || (plugin.getGiantTools().isTippedArrow(damagerEntity) && !config.takeTippedArrowDamage()));
    }

    /*
    @EventHandler(priority = EventPriority.HIGH)
    public void onTabComplete(TabCompleteEvent e) {
        String buffer = e.getBuffer();
        plugin.getLogger().info("Buffer: " + buffer);
        String[] args = buffer.trim().split(" ");
        String cmd = args[0];
        if(args.length != 1 || !cmd.matches("(minecraft:)?summon")) return;
        List<String> completions = new ArrayList<>(e.getCompletions());
        completions.add(SmartGiant.ID);
        completions.add(SmartGiant.HOSTILE_ID);
        e.setCompletions(completions);
    }*
    /
     */
}
