package io.github.kilobytz.sa.misc;

import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import io.github.kilobytz.sa.SA;
import io.github.kilobytz.sa.ranks.RankManager;

public class NoInteracting implements Listener {

    RankManager rM;
    SA main;

    public void setRanks(RankManager rM, SA main) {
        this.rM = rM;
        this.main = main;
      }

    @EventHandler
    public void onHangingHit(HangingBreakByEntityEvent event) {
            if(event.getRemover() instanceof Player) {
                if (this.rM.doesPlayerHaveRank((Player) event.getRemover())) {
                   event.setCancelled(false);
                   return;
                }
            }
            event.setCancelled(true);
    }

    @EventHandler
    public void hangDamage(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Hanging || event.getEntity() instanceof ArmorStand) {
            if(event.getDamager() instanceof Player) {
                if (this.rM.doesPlayerHaveRank((Player)event.getDamager())) {
                    return;
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void armorStandInteract(PlayerArmorStandManipulateEvent event) {
        if(this.rM.doesPlayerHaveRank(event.getPlayer())) {
            return;
        }
        event.setCancelled(true);
    }
    
    @EventHandler
    public void hungChange(PlayerInteractEntityEvent event) {
        if(event.getRightClicked() instanceof ItemFrame || event.getRightClicked() instanceof ArmorStand) {
            if (this.rM.doesPlayerHaveRank(event.getPlayer())) {
                return;
            }
            event.setCancelled(true);  
        }
    }

    @EventHandler
    public void iceMelt(BlockFadeEvent event) {
        if(event.getBlock().getType() == Material.ICE) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.hasBlock()) {
            if (!this.rM.doesPlayerHaveRank(event.getPlayer())) {
                if (event.getClickedBlock().getType() == Material.DISPENSER || event.getClickedBlock().getType() == Material.ANVIL ||
                 event.getClickedBlock().getType() == Material.BEACON || event.getClickedBlock().getType() == Material.FURNACE ||
                 event.getClickedBlock().getType() == Material.FLOWER_POT) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
