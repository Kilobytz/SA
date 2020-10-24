package io.github.kilobytz.sa.ranks;


import com.mojang.authlib.GameProfile;
import io.github.kilobytz.sa.SA;
import io.github.kilobytz.sa.misc.Reflection;
import io.netty.channel.Channel;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitScheduler;
import io.github.kilobytz.sa.misc.TinyProtocol;

import java.lang.reflect.Field;

public class RankListener implements Listener {

    RankManager rM;
    SA main;
    private TinyProtocol protocol;
    private Class<?> playerInfo = Reflection.getClass("{nms}.PacketPlayOutPlayerInfo");
    private Class<Object> playerData = Reflection.getUntypedClass("{nms}.PacketPlayOutPlayerInfo.PlayerInfoData");
    private Reflection.FieldAccessor<Object> playerInfoData = Reflection.getField(playerInfo,playerData,0);

    public void setRanks(RankManager rM, SA main) {
        this.rM = rM;
        this.main = main;
        //packetListen();
    }

    @EventHandler
    public void joinEvent(PlayerJoinEvent event) {

        if(event.getPlayer().isOp()) {
            rM.setTitle(Bukkit.getPlayer(event.getPlayer().getUniqueId()),"admin");
            return;
        }
        try {
            if (rM.doesPlayerHaveRank(event.getPlayer())) {
                String rank = (String) main.getConfig().get("users." + event.getPlayer().getUniqueId().toString());
                if (rank.equalsIgnoreCase("builder")) {
                    rM.removeRanks(event.getPlayer());
                    rM.builder(event.getPlayer());
                    rM.setTitle(event.getPlayer(), "builder");
                    return;
                }
                if (rank.equalsIgnoreCase("admin")) {
                    if (!event.getPlayer().isOp()) {
                        event.getPlayer().setOp(true);
                    }
                    rM.setTitle(event.getPlayer(), "admin");
                    return;
                }
            }
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
        }catch (NullPointerException e) {
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
        }
    }
    @EventHandler
    public void playerLogin(PlayerLoginEvent event) {
        if(main.getDelayLogin()) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER,"Server is still starting! Please wait before reconnecting.");
        }
    }
/*
    public void packetListen() {
        protocol = new TinyProtocol(main) {
            @Override
            public Object onPacketOutAsync(Player receiver, Channel channel, Object packet) {
                if(playerInfo.isInstance(packet)) {
                    //PacketPlayOutPlayerInfo.PlayerInfoData data = (PacketPlayOutPlayerInfo.PlayerInfoData) playerInfoData.get(packet);
                    receiver.sendMessage(data.toString());
                }
                return super.onPacketOutAsync(receiver, channel, packet);
            }
        };
    }

/*
try {
                        if (rM.doesPlayerHaveRank(event.getPlayer())) {
                            String rank = (String) main.getConfig().get("users." + event.getPlayer().getUniqueId().toString());
                            if (rank.equalsIgnoreCase("builder")) {
                                rM.removeRanks(event.getPlayer());
                                rM.builder(event.getPlayer());
                                rM.setTitle(event.getPlayer(), "builder");
                                return;
                            }



                            if (rank.equalsIgnoreCase("admin")) {
                                if (!event.getPlayer().isOp()) {
                                    event.getPlayer().setOp(true);
                                }
                                rM.setTitle(event.getPlayer(), "admin");
                                return;
                            }
                        }
                    }catch (NullPointerException e) {
                    }
*/


}
