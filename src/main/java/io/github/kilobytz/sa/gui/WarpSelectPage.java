package io.github.kilobytz.sa.gui;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WarpSelectPage extends GUICreator{


    private int slot;
    private int warpEditPageNum = 1;
    WarpEditManager WEM;
    Material mat = Material.SNOW_BALL;


    public WarpSelectPage(List<String> warps, WarpEditManager WEM,int slot,int pageNum){
        super(54, "Warp Select");
        this.WEM = WEM;
        this.slot = slot;
        warpEditPageNum = pageNum;
        addWarps(warps);
    }
    
    public boolean addWarps(List<String> warps){
        int warpNum = 0;
        for(int i = 0; i <= 53; ++i){
            if(!isNumberSide(i)){
                if(warpNum >= warps.size()){
                    setItem(i, makeItem(Material.STAINED_GLASS_PANE,(short)8,"", ""),(player,object) -> {
                        return;
                    });
                    continue;
                }
                else{
                    int e = warpNum;
                    setItem(i, makeItem(Material.EYE_OF_ENDER, warps.get(warpNum), "Click to add this warp to the slot"),(player,object) -> {
                        ((WarpEditManager)object).setWarpEditorItem(slot, warpEditPageNum,warps.get(e),mat,player);
                        this.delete();
                    });
                    setActionObject(i, WEM);
                    ++warpNum;
                    continue;
                }
            }
            setItem(i, makeItem(Material.STAINED_GLASS_PANE,(short)7,"", ""),(player,object) -> {
                return;
            });

            
        }
        setItem(35, makeItem(Material.STAINED_GLASS_PANE,(short)14, "Delete Warp", "Click to delete the warp"),(player,object) -> {
            ((WarpEditManager)object).deleteWarpEditorItem(slot, warpEditPageNum, player);
            this.delete();
        });
        setActionObject(35, WEM);
        if(warps.size() == 0){
            return false;
        }
        return true;
    }

    public void setWarpEditPageNum(int num){warpEditPageNum = num;}

    public int getWarpEditPageNum(){return warpEditPageNum;}

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