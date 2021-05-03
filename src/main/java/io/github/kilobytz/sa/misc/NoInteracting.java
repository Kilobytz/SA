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
import io.github.kilobytz.sa.players.PlayerManager;
import io.github.kilobytz.sa.players.PracPlayer;

public class NoInteracting implements Listener {

    PlayerManager rM;
    SA main;

    public void setRanks(PlayerManager rM, SA main) {
        this.rM = rM;
        this.main = main;
      }

    @EventHandler
    public void onHangingHit(HangingBreakByEntityEvent event) {
            if(event.getRemover() instanceof Player) {
                if ((rM.getPlayerInst((Player)event.getRemover())).hasRank()) {
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
                if ((rM.getPlayerInst((Player)event.getDamager())).hasRank()) {
                    return;
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void armorStandInteract(PlayerArmorStandManipulateEvent event) {
        if((rM.getPlayerInst(event.getPlayer())).hasRank()) {
            return;
        }
        event.setCancelled(true);
    }
    
    @EventHandler
    public void hungChange(PlayerInteractEntityEvent event) {
        if(event.getRightClicked() instanceof ItemFrame || event.getRightClicked() instanceof ArmorStand) {
            if ((rM.getPlayerInst(event.getPlayer())).hasRank()) {
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
            if (!(rM.getPlayerInst(event.getPlayer())).hasRank()) {
                if (event.getClickedBlock().getType() == Material.DISPENSER || event.getClickedBlock().getType() == Material.ANVIL ||
                 event.getClickedBlock().getType() == Material.BEACON || event.getClickedBlock().getType() == Material.FURNACE ||
                 event.getClickedBlock().getType() == Material.FLOWER_POT) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
