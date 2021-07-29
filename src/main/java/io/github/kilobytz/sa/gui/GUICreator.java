package io.github.kilobytz.sa.gui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class GUICreator {

    //credit for this code goes to SpigotMC user "xilixir"
    
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
//todo: check if changing playerInventory changes all invs in Map (it doesn't - delete inv instance after close)
    public void setItem(int slot, ItemStack stack, PlayerGUIAction action){
        playerInventory.setItem(slot, stack);
        if (action != null){
            actions.put(slot, action);
        }
    }
 
    public void setItem(int slot, ItemStack stack){
        setItem(slot, stack, null);
    }

    public ItemStack makeItem(Material mat, String name, String lore){
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        List<String> loreList = new LinkedList<String>();
        loreList.add(lore);
        meta.setLore(loreList);
        item.setItemMeta(meta);
        return item;
    }

    public void open(Player p){
        p.openInventory(playerInventory);
        openInventories.put(p.getUniqueId(), getUuid());
    }

    public interface PlayerGUIAction {
        void click(Player player);
    }

    public static Map<UUID, GUICreator> getInventoriesByUUID() {
        return inventoriesByUUID;
    }

    public static Map<UUID, UUID> getOpenInventories() {
        return openInventories;
    }

    public Map<Integer, PlayerGUIAction> getActions() {
        return actions;
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