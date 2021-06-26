package io.github.kilobytz.sa.misc;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import io.github.kilobytz.sa.SA;
import io.github.kilobytz.sa.players.PlayerManager;
import io.github.warping.WarpHandling;

public class WorldListener implements Listener {
    
    WarpHandling wH;
    SA main;

    public void setInfo(WarpHandling wH, SA main) {
        this.wH = wH;
        this.main = main;
      }
      
    @EventHandler
    public void unloadWorld(WorldUnloadEvent event) {
        event.setCancelled(true);
    }
}

