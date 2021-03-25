package io.github.kilobytz.sa.ranks;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import io.github.kilobytz.sa.SA;
import io.netty.handler.codec.http.HttpHeaders.Names;

public class RankListener implements Listener {
  RankManager rM;
  
  SA main;
 
  
  public void setRanks(RankManager rM, SA main) {
    this.rM = rM;
    this.main = main;
  }

  @EventHandler
  public void joinSetTeams(PlayerJoinEvent event){
    for(String ranks : rM.getRanksList()) {
      setTeam(ranks, rM.ranks.get(ranks));
    }
        
  }

  public void setTeam(String name, ChatColor color) {
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    Scoreboard mainBoard = manager.getMainScoreboard();
            try{
              Team team = mainBoard.registerNewTeam(name.toLowerCase());
              team.setPrefix(color + "[" + name + "] " + ChatColor.WHITE);
              team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            }catch(IllegalArgumentException e) {}
  }
  
  @EventHandler
  public void joinEvent(PlayerJoinEvent event) {
    if (this.rM.doesPlayerHaveRank(event.getPlayer())) {
      String rank = (String)this.main.getConfig().get("users." + event.getPlayer().getUniqueId().toString());
      switch (rank) {
        case "builder":
            this.rM.builder(event.getPlayer());
            rM.checkRank(rank,event.getPlayer());
            return;
        case "admin" :
            this.rM.admin(event.getPlayer());
            rM.checkRank(rank,event.getPlayer());
            return;
        case "owner" :
            this.rM.owner(event.getPlayer());
            rM.checkRank(rank,event.getPlayer());
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
  public void playerChat(AsyncPlayerChatEvent event) {
    String msg = event.getMessage();
    event.setCancelled(true);
    String nameSec;
    if(rM.doesPlayerHaveRank(event.getPlayer())) {
      String rankRaw = rM.getPlayerRank(event.getPlayer());
      String rank = rankRaw.substring(0,1).toUpperCase() + rankRaw.substring(1);
      ChatColor col = rM.ranks.get(rank);
      nameSec = (col + "[" + rank + "] " + ChatColor.WHITE + event.getPlayer().getDisplayName());
    }
    else{
      nameSec = event.getPlayer().getDisplayName();
    }
    main.getServer().getConsoleSender().sendMessage(nameSec + ": " + msg);
    for(Player p : Bukkit.getOnlinePlayers()) {
      p.sendMessage(nameSec + ": " + msg);
    }
  }
    

  @EventHandler
  public void playerLogin(PlayerLoginEvent event) {
    if (this.main.getDelayLogin())
      event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Server is still starting! Please wait before reconnecting."); 
  }

  
 
  
}
