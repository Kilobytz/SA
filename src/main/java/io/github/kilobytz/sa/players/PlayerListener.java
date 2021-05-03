package io.github.kilobytz.sa.players;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.github.kilobytz.sa.SA;

public class PlayerListener implements Listener {
  PlayerManager pM;
  
  SA main;
 
  
  public void setRanks(PlayerManager pM, SA main) {
    this.pM = pM;
    this.main = main;
  }

  
  @EventHandler
  public void joinEvent(PlayerJoinEvent event) {
    pM.setConfPerms(event.getPlayer());
    event.getPlayer().setGameMode(GameMode.ADVENTURE);
  }

  @EventHandler
  public void playerChat(AsyncPlayerChatEvent event) {
    String msg = event.getMessage();
    event.setCancelled(true);
    String nameSec;
    if((pM.getPlayerInst(event.getPlayer())).hasRank()) {
      PracPlayer playPrac = pM.getPlayerInst(event.getPlayer());
      String rank = playPrac.getRank().getName().substring(0,1).toUpperCase() + playPrac.getRank().getName().substring(1);
      ChatColor col = playPrac.getRank().getColour();
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
  public void playerLoginDelay(PlayerLoginEvent event) {
    if (this.main.getDelayLogin()) {
      event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Server is still starting! Please wait before reconnecting.");
    }
  }

  @EventHandler
  public void playerLogin(PlayerLoginEvent event) {
    if(pM.getPlayerInst(event.getPlayer()).hasRank()) {
      pM.getPlayerInst(event.getPlayer()).getRank().setPerms(main, event.getPlayer());
    }
  }
  

  @EventHandler
  public void playerLeave(PlayerQuitEvent event) {
    try {
      main.openConnection();
      Statement statement = SA.connection.createStatement();
      pM.savePlayers(statement,event.getPlayer());       
  } catch (ClassNotFoundException e) {
      e.printStackTrace();
  } catch (SQLException e) {

      e.printStackTrace();
  }
  }
  

  @EventHandler
  public void playerKicked(PlayerKickEvent event) {
    try {
      main.openConnection();
      Statement statement = SA.connection.createStatement();
      pM.savePlayers(statement,event.getPlayer());       
  } catch (ClassNotFoundException e) {
      e.printStackTrace();
  } catch (SQLException e) {

      e.printStackTrace();
  }
  }

  @EventHandler
  public void playerPreLogin(AsyncPlayerPreLoginEvent event) {
    try {    
      main.openConnection();
      Statement statement = SA.connection.createStatement();
      ResultSet rs1 = statement.executeQuery("SELECT EXISTS(SELECT rank FROM players WHERE uuid =  '"+event.getUniqueId()+"');");
      if(rs1.next()) {
        if(rs1.getInt(1) == 1) { //if player exists, has entry
          ResultSet rs2 = statement.executeQuery("SELECT rank FROM players WHERE uuid =  '"+event.getUniqueId()+"';");
          if(rs2.next()) {
            if(rs2.getString(1) != null) { //if player has rank
              pM.addPlayer(new PracPlayer(event.getUniqueId(),rs2.getString(1)));
            }
            else{ //if player has no rank
            pM.addPlayer(new PracPlayer(event.getUniqueId(),"NULL"));
            }
          }
        }
        else{ //if player doesnt exist, no entry
          statement.executeUpdate("INSERT INTO players (uuid) VALUES ( '"+event.getUniqueId()+"');");
          pM.addPlayer(new PracPlayer(event.getUniqueId(),"NULL"));
        }
      }
  } catch (ClassNotFoundException e) {
      e.printStackTrace();
  } catch (SQLException e) {

      e.printStackTrace();
  }
  }
 
  
}
