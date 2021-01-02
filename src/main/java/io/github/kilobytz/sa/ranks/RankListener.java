package io.github.kilobytz.sa.ranks;


import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import io.github.kilobytz.sa.SA;

public class RankListener implements Listener {
  RankManager rM;
  
  SA main;
  
 
  
  public void setRanks(RankManager rM, SA main) {
    this.rM = rM;
    this.main = main;
  }

  
  @EventHandler
  public void joinEvent(PlayerJoinEvent event) {
    if (this.rM.doesPlayerHaveRank(event.getPlayer())) {
      String rank = (String)this.main.getConfig().get("users." + event.getPlayer().getUniqueId().toString());
      switch (rank) {
        case "builder":
            this.rM.builder(event.getPlayer());
            this.rM.setTitle(event.getPlayer(), "builder");
            return;
        case "admin" :
            this.rM.admin(event.getPlayer());
            this.rM.setTitle(event.getPlayer(), "admin");
            return;
        case "owner" :
            this.rM.owner(event.getPlayer());
            this.rM.setTitle(event.getPlayer(), "owner");
        return;
        default:
      }
    }
    if(event.getPlayer().isOp()) {
      event.getPlayer().setOp(false);
    }
    event.getPlayer().setGameMode(GameMode.ADVENTURE);
  }
    

  @EventHandler
  public void playerLogin(PlayerLoginEvent event) {
    if (this.main.getDelayLogin())
      event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Server is still starting! Please wait before reconnecting."); 
  }

  
 
  
}
