package io.github.kilobytz.sa.gui;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WarpEditor extends GUICreator{

    HashMap<Integer,String> warps;

    public WarpEditor(HashMap<Integer,String> warps){
        super(54, "Warp Editor");
        this.warps = warps;

        /*for(int i = 0)

        setItem(0, makeItem(Material.DIAMOND, "Right", "lol idk"),(player) -> {
            player.sendMessage("poggers");
            player.teleport(player.getWorld().getSpawnLocation());
        });*/
        
        //make this once WarpEditManager sends data properly to this class, iterate over contents of hashmap/list and add to respective slots
    }

    public boolean isNumberSide(int num){
        if(num == 0){
            return true;
        }
        if(num % 9 == 0 || num % 9 == 8){
            return true;
        }
        return false;
    }
    
}
