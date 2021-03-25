package io.github.kilobytz.sa.misc;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KittyDrugs implements Listener{

    @EventHandler
    public void rightClickShrooms(PlayerInteractEvent event) {
        ItemStack redShroom = new ItemStack(Material.RED_MUSHROOM);
        if((event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        && event.getItem() != null){
            ItemStack playerItem = event.getItem();
            if(playerItem.getType().equals(redShroom.getType())&& CraftItemStack.asNMSCopy(playerItem).hasTag()) {
                if(CraftItemStack.asNMSCopy(playerItem).getTag().hasKey("redshroomdrugs")) {
                    event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP,100,2));
                    event.setCancelled(true);

                    if(playerItem.getAmount() > 1) {
                        event.getPlayer().getInventory().getItemInMainHand().setAmount(playerItem.getAmount()-1);
                    }
                    else{
                        event.getPlayer().getInventory().remove(playerItem);
                    }
                }
            }
        }
    }
}