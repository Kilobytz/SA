package io.github.kilobytz.sa.entities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

public class EntityManager implements Listener{

    @EventHandler
    public void mobSpawning(CreatureSpawnEvent event) {
        if(event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL || event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CHUNK_GEN) {
            event.setCancelled(true);
        }

    }
    @EventHandler
    public void fireworkDamage(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Firework) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void foodCancel(FoodLevelChangeEvent event) {
        if(event.getEntity() instanceof Player && event.getFoodLevel() < 20) {
            Player player = (Player) event.getEntity();
            player.setFoodLevel(20);
            event.setCancelled(true);
            return;
        }
        event.setCancelled(true);
    }
}
