package io.github.kilobytz.sa.misc;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.minecraft.server.v1_12_R1.Item;

public class KittyDrugs implements Listener{

    @EventHandler
    public void rightClickShrooms(PlayerInteractEvent event) {
        ItemStack redShroom = new ItemStack(Material.RED_MUSHROOM);
        if(event.getItem() != null && (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))){
            ItemStack playerItem = event.getItem();
            if(playerItem.getType().equals(redShroom.getType())&& CraftItemStack.asNMSCopy(playerItem).hasTag())     {
                if(CraftItemStack.asNMSCopy(playerItem).getTag().hasKey("redshroomdrugs")) {
                    event.setCancelled(true);
                    giveJump(event.getPlayer(), playerItem);
                }
            }
        }
    }


    public void giveJump(Player player, ItemStack item) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,100,1));
        player.playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_BURP, 1, 0);
        if(item.getAmount() > 1) {
            player.getInventory().getItemInMainHand().setAmount(item.getAmount()-1);
        }else{
            player.getInventory().remove(item);
        }
    }
}