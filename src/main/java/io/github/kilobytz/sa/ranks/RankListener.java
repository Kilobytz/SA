package io.github.kilobytz.sa.ranks;

import io.github.kilobytz.sa.SA;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class RankListener implements Listener {

    RankManager rM;
    SA main;

    public void setRanks(RankManager rM, SA main) {
        this.rM = rM;
        this.main = main;
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


}
