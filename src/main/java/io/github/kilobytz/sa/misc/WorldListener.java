package io.github.kilobytz.sa.misc;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.server.ServerCommandEvent;

import io.github.kilobytz.sa.SA;
import io.github.kilobytz.sa.warping.WarpHandling;

public class WorldListener implements Listener {
    
    WarpHandling wH;
    SA main;
    public void setInfo(WarpHandling wH, SA main) {
      this.wH = wH;
      this.main = main;
    }
    @EventHandler
    public void worldLeave(PlayerChangedWorldEvent event){
      if(event.getFrom() != main.getServer().getWorlds().get(0)){
        if(event.getFrom().getPlayers().size() == 0){
          Bukkit.unloadWorld(event.getFrom(), true);
          main.getLogger().info("world " + event.getFrom().getName() + " shutting down.");
        }
      }
    }
}

