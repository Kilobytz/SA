package io.github.kilobytz.sa.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class GUICreator {
    
    public static Map<UUID, GUICreator> inventoriesByUUID = new HashMap<>();
    public static Map<UUID, UUID> openInventories = new HashMap<>();
    private UUID uuid;
    private Inventory playerInventory;
    private Map<Integer, PlayerGUIAction> actions;

    public GUICreator(int invSize, String invName) {
        uuid = UUID.randomUUID();
        playerInventory = Bukkit.createInventory(null, invSize, invName);
        actions = new HashMap<>();
        inventoriesByUUID.put(getUuid(), this);
    }

    public UUID getUuid() {
        return uuid;
    }
//todo: check if changing playerInventory changes all invs in Map
    public void setItem(int slot, ItemStack stack, PlayerGUIAction action){
        playerInventory.setItem(slot, stack);
        if (action != null){
            actions.put(slot, action);
        }
    }
 
    public void setItem(int slot, ItemStack stack){
        setItem(slot, stack, null);
    }

    public void open(Player p){
        p.openInventory(playerInventory);
        openInventories.put(p.getUniqueId(), getUuid());
    }

    public interface PlayerGUIAction {
        void click(Player player);
    }

    public void delete(){
        for (Player p : Bukkit.getOnlinePlayers()){
            UUID u = openInventories.get(p.getUniqueId());
            if (u.equals(getUuid())){
                p.closeInventory();
            }
        }
        inventoriesByUUID.remove(getUuid());
    }
}