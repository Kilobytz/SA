package io.github.kilobytz.sa.entities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class EntityManager implements Listener {

    @EventHandler
    public void playerDeath(EntityDeathEvent event) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("reeeeeeeeeeeeeeeeee");
        }
    }
}
