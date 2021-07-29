package io.github.kilobytz.sa.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

public class GUIListener implements Listener{


    
    @EventHandler
    public void onClick(InventoryClickEvent event){
        if (!(event.getWhoClicked() instanceof Player)){
            return;
        }
        Player player = (Player) event.getWhoClicked();
        UUID playerUUID = player.getUniqueId();

        UUID inventoryUUID = GUICreator.openInventories.get(playerUUID);
        if (inventoryUUID != null && event.getClickedInventory() != event.getWhoClicked().getInventory()){
            event.setCancelled(true);
            GUICreator gui = GUICreator.getInventoriesByUUID().get(inventoryUUID);
            GUICreator.PlayerGUIAction action = gui.getActions().get(event.getSlot());
         
            if (action != null){
                action.click(player);
            }
        }
    }
    @EventHandler
    public void onClose(InventoryCloseEvent e){
        Player player = (Player) e.getPlayer();
        UUID playerUUID = player.getUniqueId();
     
        GUICreator.openInventories.remove(playerUUID);
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();

        GUICreator.openInventories.remove(playerUUID);
    }


    //remake this later
    @EventHandler
    public void interactItem(PlayerInteractEvent event) {
        if(event.getItem() != null){
            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
            net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
            if (nmsItem.hasTag()) {
                if(nmsItem.getTag().hasKey("warpedit")) {
                    //test method, replace 'if key' with handler class for storing lambda method with nbt tag and code for said nbt tag
                    event.setCancelled(true);
                }
            }
        }
    }
}
