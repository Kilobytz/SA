package io.github.kilobytz.sa.misc;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import io.github.kilobytz.sa.SA;

public class Grapple implements Listener {
    
    SA main;

    public Grapple(SA main) {
        this.main = main;
    }
    
    @EventHandler
    public void onGrapple(PlayerFishEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        if (item.getType().equals(Material.FISHING_ROD) && nmsItem.hasTag()) {
            if(nmsItem.getTag().hasKey("grapple")) {
                item.setDurability(item.getDurability());
                Location locHook = event.getHook().getLocation();
                Location locBlockHook = locHook.clone();
                locBlockHook.setY(Math.floor(locBlockHook.getY()));
                locBlockHook.setY(locBlockHook.getY() - 1.0);
                Block blockUnderHook = event.getHook().getWorld().getBlockAt(locBlockHook);

                if ((event.getState() != PlayerFishEvent.State.IN_GROUND && 
                event.getState() != PlayerFishEvent.State.FAILED_ATTEMPT) || 
                blockUnderHook.getType() == Material.AIR || 
                blockUnderHook.getType() == Material.WATER || 
                blockUnderHook.getType() == Material.STATIONARY_WATER) {
                    return;
                }
        
                event.getPlayer().getWorld().playSound(event.getPlayer().getEyeLocation(), Sound.ENTITY_HORSE_STEP, 1, 3);


                //Big thanks to 111kittycat111/ACatThatCanParkourReallyWell#9900 for helping me with this math formula.
                //Also FloorIsJava for simplifying this trash

                final Vector v = locHook.toVector().subtract(player.getLocation().toVector()).multiply(0.2);


                player.setVelocity(v);
                Map<Enchantment, Integer> itemsEnch = item.getEnchantments();
                if(itemsEnch.containsKey(Enchantment.LUCK)) {
                    return;
                }
                if(itemsEnch.containsKey(Enchantment.DURABILITY)) {
                    switch(itemsEnch.get(Enchantment.DURABILITY)) {
                        case 1 :
                        item.setDurability((short)(item.getDurability() + 4));
                        return;
                        case 2 :
                        item.setDurability((short)(item.getDurability() + 2));
                        return;
                        case 3 :
                        item.setDurability((short)(item.getDurability() + 1));
                        return;
                        default :
                    }
                }
                item.setDurability((short)(item.getDurability() + 8));
            }
        }
    }
    
    /*@EventHandler
    public void projLaunch(ProjectileLaunchEvent event) {
        if(event.getEntity() instanceof FishHook) {
            if(event.getEntity().getVelocity().getY() > 0) {
                event.getEntity().setVelocity(event.getEntity().getVelocity().setY(event.getEntity().getVelocity().getY()*1.1   ));
            }
        }
    
    }
    */
    @EventHandler
    public void projHit (ProjectileHitEvent event) {
        if(event.getEntity() instanceof FishHook) {
            Location hookLoc = event.getEntity().getLocation();
            Location underHookBlock = hookLoc.clone();
            underHookBlock.setY(Math.floor(hookLoc.getY()));
            underHookBlock.setY(hookLoc.getY() - 1.0);
            Block blockUnderHook = underHookBlock.getBlock();
            Player shooter = (Player) event.getEntity().getShooter();
            if(blockUnderHook.getType() != Material.AIR || 
            blockUnderHook.getType() != Material.WATER || 
            blockUnderHook.getType() != Material.STATIONARY_WATER) {
                shooter.getWorld().playSound(shooter.getEyeLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1, 3);
            }
        }   
    }

}
