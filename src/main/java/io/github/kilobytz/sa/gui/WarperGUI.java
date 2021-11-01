package io.github.kilobytz.sa.gui;

import java.util.TreeMap;
import org.bukkit.Material;

import io.github.kilobytz.sa.warping.WarpHandling;

public class WarperGUI extends GUICreator{

    WarpEditManager WEM;
    int pageNum;
    WarpHandling wH;

    public WarperGUI(TreeMap<Integer,String> warps, TreeMap<Integer,Material> warpMats, TreeMap<Integer,Short> warpMatData, int pageNum, WarpHandling wH){
        super(54, "Warps");
        this.wH = wH;

        this.pageNum = pageNum;
        for(int i = 0; i < 54; ++i){
            int e = i;
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

                            setItem(i, makeItem(warpMats.get(i),warpMatData.get(i), warps.get(i), "Click me to teleport"),(player,object) -> {
                                wH.warpPlayer(player, warps.get(e).replace(" ", "_"));
                            });
                        }
                        else{
                            setItem(i, makeItem(warpMats.get(i), warps.get(i), "Click me to teleport"),(player,object) -> {
                                wH.warpPlayer(player, warps.get(e).replace(" ", "_"));
                            });
                        }
                        continue;
                    }
                }
                setItem(i, makeItem(Material.STAINED_GLASS_PANE,(short)8,"-", "-"),(player,object) -> {
                    return;
                });
            }
        }
    }

    public void setEmptyWarp(int slot){
        setItem(slot, makeItem(Material.STAINED_GLASS_PANE,(short)8,"-", "-"),(player,object) -> {
            return;
        });
    }

    public void setNewWarp(int slot,String warpName,Material mat){
        setItem(slot, makeItem(mat, warpName, "Click me to teleport"),(player,object) -> {
            wH.warpPlayer(player, warpName);
        });
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
