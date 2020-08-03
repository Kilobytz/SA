package io.github.kilobytz.sa.misc;

import org.bukkit.GameMode;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

public class NoInteracting implements Listener {

    @EventHandler
    public void onHangingHit(HangingBreakByEntityEvent event) {
            if(event.getRemover() instanceof Player) {
                Player dmger = (Player) event.getRemover();
                if(dmger.isOp() || dmger.getGameMode() == GameMode.CREATIVE) {
                   event.setCancelled(false);
                   return;
                }
            }
            event.setCancelled(true);
    }
}
