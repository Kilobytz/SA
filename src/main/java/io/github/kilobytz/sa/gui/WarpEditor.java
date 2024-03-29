package io.github.kilobytz.sa.gui;


import java.util.TreeMap;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WarpEditor extends GUICreator{

    WarpEditManager WEM;
    int pageNum;

    public WarpEditor(TreeMap<Integer,String> warps, TreeMap<Integer,Material> warpMats, TreeMap<Integer,Short> warpMatData, WarpEditManager WEM, int pageNum){
        super(54, "Warp Editor");
        this.WEM = WEM;
        this.pageNum = pageNum;
        for(int i = 0; i < 54; ++i){
            int s = i;
            if(isNumberSide(i)){
                setItem(i, makeItem(Material.STAINED_GLASS_PANE,(short)7,"-", "-"),(player,object) -> {
                    return;
                });
                continue;
            }
            else{
                if(warps != null){
                    if(warps.keySet().contains(i)){
                        warps.put(i, warps.get(i).replace("_", " "));
                        if(warpMatData.keySet().contains(i)){
                            setItem(i, makeItem(warpMats.get(i),warpMatData.get(i), warps.get(i), "Click me to change warp : " + warps.get(i)),(player,object) -> {
                                ((WarpEditManager)object).openSelectPage(s, pageNum, player);
                            });
                        }
                        else{
                            setItem(i, makeItem(warpMats.get(i), warps.get(i), "Click me to change warp : " + warps.get(i)),(player,object) -> {
                                ((WarpEditManager)object).openSelectPage(s, pageNum, player);
                            });
                        }
                        setActionObject(i, WEM);
                        continue;
                    }
                }
                setItem(i, makeItem(Material.SNOW_BALL, "Empty Slot", "Click me to set warp"),(player,object) -> {
                    ((WarpEditManager)object).openSelectPage(s, pageNum, player);
                });
                setActionObject(i, WEM);
            }
            setActionObject(i, WEM);
        }
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

    public void setEmptyWarp(int slot){
        setItem(slot, makeItem(Material.SNOW_BALL, "Empty Slot", "Click me to set warp"),(player,object) -> {
            ((WarpEditManager)object).openSelectPage(slot, pageNum, player);
        });
        setActionObject(slot, WEM);
    }

    public void setNewWarp(int slot, ItemStack item){
        setItem(slot, item,(player,object) -> {
            ((WarpEditManager)object).openSelectPage(slot,pageNum,player);
        });
        setActionObject(slot, WEM);
    }
    
}
