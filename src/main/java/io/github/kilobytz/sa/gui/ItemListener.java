package io.github.kilobytz.sa.gui;

import java.util.UUID;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class ItemListener implements Listener{

    ItemNMSRegistry itemReg;
    WarpEditManager WEM;
    
    public ItemListener(ItemNMSRegistry itemReg, WarpEditManager WEM){
        this.itemReg = itemReg;
        this.WEM = WEM;
    }

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
            if(gui.getActionObject(event.getSlot()) != null){
                if (action != null){
                    action.click(player,gui.getActionObject(event.getSlot()));
                }
            }
            else{
                if (action != null){
                    action.click(player,null);
                }
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
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getItem() != null){
                ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
                net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
                if (nmsItem.hasTag()) {
                    for(String tag : itemReg.getTags()){
                        if(nmsItem.getTag().hasKey(tag)) {
                            ItemNMSRegistry.ItemAction action = itemReg.getItems().get(tag);
    
                            if(itemReg.getActionObject(tag) != null){
                                action.action(event.getPlayer(),itemReg.getActionObject(tag));
                                event.setCancelled(true);
                            }
                            else{
                                action.action(event.getPlayer(),null);
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }
}
