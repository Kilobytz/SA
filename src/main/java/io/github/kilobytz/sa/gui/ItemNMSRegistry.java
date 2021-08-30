package io.github.kilobytz.sa.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

public class ItemNMSRegistry {
    
    private Map<String,ItemAction> items = new HashMap<String,ItemAction>();
    private Map<String,Object> itemObject = new HashMap<>();

    public void registerItem(String nmsTag, ItemAction action){
        items.put(nmsTag, action);
    }

    public List<String> getTags(){
        return new ArrayList<String>(items.keySet()); 
    }
    
    public Map<String,ItemAction> getItems(){
        return items;
    }

    public Object getActionObject(String itemName){
        return itemObject.get(itemName);
    }

    public void setActionObject(String name, Object object){
        itemObject.put(name, object);
    }

    public interface ItemAction {
        void action(Player player,Object object);
    }
}
