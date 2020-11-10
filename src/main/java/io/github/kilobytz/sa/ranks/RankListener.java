package io.github.kilobytz.sa.ranks;

import com.mojang.authlib.GameProfile;
import io.github.kilobytz.sa.SA;
import io.github.kilobytz.sa.misc.Reflection;
import io.github.kilobytz.sa.misc.TinyProtocol;
import io.netty.channel.Channel;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.v1_12_R1.EnumGamemode;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.Plugin;

public class RankListener implements Listener {
  RankManager rM;
  
  SA main;
  
  private TinyProtocol protocol;
  
  private Class<?> playerInfoClass = Reflection.getClass("{nms}.PacketPlayOutPlayerInfo");
  
  private Reflection.FieldAccessor<List> playerInfo = Reflection.getField(this.playerInfoClass, "b", List.class);
  
  public void setRanks(RankManager rM, SA main) {
    this.rM = rM;
    this.main = main;
    //packetListen();
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
            if(event.getPlayer().isOp()) {
              event.getPlayer().setOp(false);
            }
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
      }
    }
  }
    

  @EventHandler
  public void playerLogin(PlayerLoginEvent event) {
    if (this.main.getDelayLogin())
      event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Server is still starting! Please wait before reconnecting."); 
  }
  
  /*public void packetListen() {
    this.protocol = new TinyProtocol((Plugin)this.main) {
        public Object onPacketOutAsync(Player receiver, Channel channel, Object packet) {
          if (packet instanceof PacketPlayOutPlayerInfo) {
            List<Object> packetData = (List<Object>)RankListener.this.playerInfo.get(packet);
            List<PacketPlayOutPlayerInfo.PlayerInfoData> newInfoList = new ArrayList<>();
            for (Object data : packetData) {
              PacketPlayOutPlayerInfo.PlayerInfoData playerInfoData = (PacketPlayOutPlayerInfo.PlayerInfoData)data;
              UUID playerID = playerInfoData.a().getId();
              try {
                PacketPlayOutPlayerInfo testPack = (PacketPlayOutPlayerInfo)packet;
                if (RankListener.this.rM.doesPlayerHaveRank(Bukkit.getPlayer(playerID))) {
                  String rank = (String)RankListener.this.main.getConfig().get("users." + playerID.toString());

                }
              } catch (NullPointerException e) {
                receiver.sendMessage("something broke.");
              } 
            }
          } 
          return super.onPacketOutAsync(receiver, channel, packet);
        }
      };
  }
  */
}
