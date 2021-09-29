package io.github.kilobytz.sa.items;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.github.kilobytz.sa.SA;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class LegendaryHandler implements Listener{

    SA main;
    HashMap<UUID,Location> sealLocTracking = new HashMap<>();
    HashMap<UUID,Integer> sealTimeTracking = new HashMap<>();

    public LegendaryHandler(SA main){
        this.main = main;
    }

    public void sealStart(Player player){
        if(sealLocTracking.containsKey(player.getUniqueId())){
            return;
        }
        sealLocTracking.put(player.getUniqueId(), player.getLocation());
        sealTimeTracking.put(player.getUniqueId(), 0);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§aSeal of Time activated!"));
        sealTimeTracking.put(player.getUniqueId(), main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
            public void run() {
                player.teleport(sealLocTracking.get(player.getUniqueId()));
                sealLocTracking.remove(player.getUniqueId());
                sealTimeTracking.remove(player.getUniqueId());
            }
        }, 150L));
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent event){
        if(sealTimeTracking.containsKey(event.getPlayer().getUniqueId())){
            main.getServer().getScheduler().cancelTask(sealTimeTracking.get(event.getPlayer().getUniqueId()));
            sealLocTracking.remove(event.getPlayer().getUniqueId());
            sealTimeTracking.remove(event.getPlayer().getUniqueId());
        }
    }
    @EventHandler
    public void playerLeave(PlayerKickEvent event){
        if(sealTimeTracking.containsKey(event.getPlayer().getUniqueId())){
            main.getServer().getScheduler().cancelTask(sealTimeTracking.get(event.getPlayer().getUniqueId()));
            sealLocTracking.remove(event.getPlayer().getUniqueId());
            sealTimeTracking.remove(event.getPlayer().getUniqueId());
        }
    }
    
}
