package io.github.kilobytz.sa.players;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import io.github.kilobytz.sa.SA;

public class NoInteracting implements Listener {

    PlayerManager rM;
    SA main;
    boolean pvp = false;

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
    
    @EventHandler
    public void checkPvP(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player && (event.getDamager() instanceof Player || event.getDamager() instanceof Arrow) && !pvp) {
            if(event.getDamager() instanceof Arrow){
                if(!(((Arrow)event.getDamager()).getShooter() instanceof Player)){
                    return;
                }
            }
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void loginAttkSpd(PlayerJoinEvent event) {
        event.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(200.0);
    }

    public void togglePvP() {
        pvp = !pvp;
    }
    public boolean getPvPStatus() {
        return pvp;
    }
}
