package io.github.kilobytz.sa.gui;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import io.github.kilobytz.sa.SA;
import io.github.kilobytz.sa.warping.WarpHandling;

public class WarpEditManager {


    HashMap<Integer,TreeMap<Integer,String>> warpEditorWarps = new HashMap<>();
    HashMap<Integer,TreeMap<Integer,Material>> warpEditorMaterials = new HashMap<>();
    HashMap<Integer,TreeMap<Integer,Short>> warpEditorMatData = new HashMap<>();
    TreeMap<Integer,WarpEditor> warpEditorPages = new TreeMap<>();
    TreeMap<Integer,WarperGUI> warperPages = new TreeMap<>();
    HashMap<UUID,TreeMap<Integer,WarpSelectPage>> selectPages = new HashMap<>();
    SA main;
    WarpHandling wH;

    public WarpEditManager(SA main, WarpHandling wH){
        this.main = main;
        this.wH = wH;
    }

    public void GUIWarpSetup(){
        loadGuiWarps();
        loadEditor();
        loadWarper();
    }

    public void openFirstEditorPage(Player p){
        warpEditorPages.get(0).open(p);
    }

    public void openFirstWarperPage(Player p){
        warperPages.get(0).open(p);
    }
    @SuppressWarnings("unchecked")
    public void loadEditor(){
        for (int i = 0; i < warpEditorWarps.size(); i+=54) {
            warpEditorPages.put(i/54, new WarpEditor(warpEditorWarps.get(i/54),warpEditorMaterials.get(i/54),warpEditorMatData.get(i/54), this,i/54));
        }
        if(warpEditorPages.size() <= 1){
            return;
        }
        for(int num : warpEditorPages.keySet()){
            if(num != 0){
                warpEditorPages.get(num).setItem(45, warpEditorPages.get(num).makeItem(Material.NETHER_STAR, "Previous", "Click me to go back a page."), (player,object) -> {
                    ((TreeMap<Integer,WarpEditor>)object).get(num-1).open(player);
                }); //previous
                warpEditorPages.get(num).setActionObject(45, warpEditorPages);
            }
            if(num != warpEditorPages.size()-1){
                warpEditorPages.get(num).setItem(53, warpEditorPages.get(num).makeItem(Material.NETHER_STAR, "Next", "Click me to go forward a page."), (player,object) -> {
                    ((TreeMap<Integer,WarpEditor>)object).get(num+1).open(player);
                }); //next
                warpEditorPages.get(num).setActionObject(53, warpEditorPages);
            }
        }
    }
    @SuppressWarnings("unchecked")
    public void loadWarper(){
        for (int i = 0; i < warpEditorWarps.size(); i+=54) {
            warperPages.put(i/54, new WarperGUI(warpEditorWarps.get(i/54),warpEditorMaterials.get(i/54),warpEditorMatData.get(i/54), i/54,wH));
        }
        if(warperPages.size() <= 1){
            return;
        }
        for(int num : warperPages.keySet()){
            if(num != 0){
                warperPages.get(num).setActionObject(53, warperPages);
                warperPages.get(num).setItem(45, warperPages.get(num).makeItem(Material.NETHER_STAR, "Previous", "Click me to go back a page."), (player,object) -> {
                    ((TreeMap<Integer,WarperGUI>)object).get(num-1).open(player);
                }); //previous
                warperPages.get(num).setActionObject(45, warperPages);
            }
            if(num != warperPages.size()-1){
                warperPages.get(num).setItem(53, warperPages.get(num).makeItem(Material.NETHER_STAR, "Next", "Click me to go forward a page."), (player,object) -> {
                    ((TreeMap<Integer,WarperGUI>)object).get(num+1).open(player);
                }); //next
                warperPages.get(num).setActionObject(53, warperPages);
            }
        }
    }

    //todo: make buttons to add and remove warp pages
    @SuppressWarnings("unchecked")
    public void openSelectPage(int slot,int pageNum,Player p){
        TreeMap<Integer,WarpSelectPage> select = new TreeMap<>();
        List<String> warps = wH.getAllWarpNames();
        for (int i = 0; i < warps.size(); i+= 42){
            int index = Math.min(i+42, warps.size());
            select.put(select.size(), new WarpSelectPage(warps.subList(i, index), this, slot, pageNum));
        }
        selectPages.put(p.getUniqueId(),select);
        if(selectPages.get(p.getUniqueId()).size() <= 1){
            return;
        }
        for(int num : selectPages.get(p.getUniqueId()).keySet()){
            if(num != 0){
                selectPages.get(p.getUniqueId()).get(num).setItem(45, selectPages.get(p.getUniqueId()).get(num).makeItem(Material.NETHER_STAR, "Previous", "Click me to go back a page."), (player,object) -> {
                    ((HashMap<UUID,TreeMap<Integer,WarpSelectPage>>)object).get(player.getUniqueId()).get(num-1).open(player);
                }); //previous
                selectPages.get(p.getUniqueId()).get(num).setActionObject(45, selectPages);
            }
            if(num != warperPages.size()-1){
                selectPages.get(p.getUniqueId()).get(num).setItem(53, selectPages.get(p.getUniqueId()).get(num).makeItem(Material.NETHER_STAR, "Next", "Click me to go forward a page."), (player,object) -> {
                    ((HashMap<UUID,TreeMap<Integer,WarpSelectPage>>)object).get(player.getUniqueId()).get(num+1).open(player);
                });
                selectPages.get(p.getUniqueId()).get(num).setActionObject(53, selectPages);
            }
        }
        selectPages.get(p.getUniqueId()).get(0).open(p);
    }

    public void deleteWarpEditorItem(int slot, int pageNum,Player p){
        warpEditorPages.get(pageNum).setEmptyWarp(slot);
        deleteWarperItem(slot, pageNum);
        warpEditorWarps.get(pageNum).remove(slot);
        warpEditorMaterials.get(pageNum).remove(slot);
        if(warpEditorMatData.get(pageNum).get(slot) != null){
            warpEditorMatData.get(pageNum).remove(slot);
        }
        selectPages.remove(p.getUniqueId());
        warpEditorPages.get(pageNum).open(p);
    }

    public void deleteWarpEditorItem(int slot, int pageNum){
        warpEditorPages.get(pageNum).setEmptyWarp(slot);
        deleteWarperItem(slot, pageNum);
        warpEditorWarps.get(pageNum).remove(slot);
        warpEditorMaterials.get(pageNum).remove(slot);
        if(warpEditorMatData.get(pageNum).get(slot) != null){
            warpEditorMatData.get(pageNum).remove(slot);
        }
    }

    public void setWarpEditorItem(int slot, int pageNum, String warp,Material mat,Player p){
        ItemStack item = new ItemStack(mat);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(warp);
        List<String> lore = new ArrayList<String>();
        lore.add("Click me to set warp");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        warpEditorPages.get(pageNum).setNewWarp(slot, item);
        warperPages.get(pageNum).setNewWarp(slot,warp,mat);
        warpEditorWarps.get(pageNum).put(slot, warp);
        warpEditorMaterials.get(pageNum).put(slot, mat);
        warpEditorMatData.get(pageNum).put(slot, (short)0);
        selectPages.remove(p.getUniqueId());
        warpEditorPages.get(pageNum).open(p);
    }

    public void loadGuiWarps(){
        try {
            main.openConnection();
            Statement statement = SA.connection.createStatement();
            int pageCount = 0;
            warpEditorWarps.put(pageCount, new TreeMap<Integer, String>());
            warpEditorMaterials.put(pageCount, new TreeMap<Integer, Material>());
            warpEditorMatData.put(pageCount, new TreeMap<Integer,Short>());
            ResultSet rs = statement.executeQuery("SELECT * FROM gui_warps;");{
            while(rs.next()) {
                if(rs.getInt(1)/54 > pageCount){
                    ++pageCount;
                    warpEditorWarps.put(pageCount, new TreeMap<Integer, String>());
                    warpEditorMaterials.put(pageCount, new TreeMap<Integer, Material>());
                    warpEditorMatData.put(pageCount, new TreeMap<Integer,Short>());
                }
                warpEditorWarps.get(pageCount).put(rs.getInt(1), rs.getString(2));
                if(rs.getString(3) != null){
                    warpEditorMaterials.get(pageCount).put(rs.getInt(1), Material.getMaterial(rs.getString(3)));
                    warpEditorMatData.get(pageCount).put(rs.getInt(1), rs.getShort(4));
                }
                else{
                    warpEditorMaterials.get(pageCount).put(rs.getInt(1), Material.ENDER_PEARL);
                }
            }
        }
        statement.close();
        if(warpEditorWarps.size() == 0){
            warpEditorPages.put(0, new WarpEditor(null,null,null,this,0));
        }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
      
            e.printStackTrace();
        } catch (NumberFormatException e) {
            main.getLogger().info("No Database found, GUI warp loading failed.");
        }
        //gui_warps = table name
        //slot = item slot number
        //warps_name = warps name (linked to warps table)

        //make sure to sort into correct place based on item slot number
    }

    public void warpDelete(String warpName){
        for(int page : warpEditorWarps.keySet()){
            for(int slot : warpEditorWarps.get(page).keySet()){
                if(warpEditorWarps.get(page).get(slot).equals(warpName)){
                    deleteWarpEditorItem(slot, page);
                    deleteWarperItem(slot, page);
                    return;
                }
            }
        }
        
    }
    
    public void deleteWarperItem(int slot, int page){
        warperPages.get(page).setEmptyWarp(slot);
    }

    public void saveGuiWarps(){
        try {
            main.openConnection();
            String deleteAllString = "TRUNCATE gui_warps";
            String insertWarpString = "INSERT INTO gui_warps (slot, warps_name) VALUES (?,?) ON DUPLICATE KEY UPDATE warps_name = ?";
            String insertMatString = "UPDATE gui_warps SET material = (?),data = (?) WHERE slot = (?)";
            String deleteString = "DELETE FROM gui_warps WHERE warps_name = ?";
            //String deleteDifferenceString = "DELETE FROM gui_warps WHERE slot NOT IN (?)";
            PreparedStatement preppedDiffDelete = SA.connection.prepareStatement(deleteAllString);
            preppedDiffDelete.executeUpdate();
            for(String warpName : wH.getWarpsToDelete()) {
                PreparedStatement preppedDelete = SA.connection.prepareStatement(deleteString);
                preppedDelete.setString(1, warpName);
                preppedDelete.executeUpdate();
            }
            for(int page : warpEditorWarps.keySet()) {
                for(int slot : warpEditorWarps.get(page).keySet()){
                    PreparedStatement preppedInsert = SA.connection.prepareStatement(insertWarpString);
                    preppedInsert.setInt(1, slot);
                    preppedInsert.setString(2, warpEditorWarps.get(page).get(slot));
                    preppedInsert.setString(3, warpEditorWarps.get(page).get(slot));
                    preppedInsert.executeUpdate();
                }
            }
            if(warpEditorMaterials.size()>=1){
                for(int page : warpEditorMaterials.keySet()) {
                    for(int slot : warpEditorWarps.get(page).keySet()){
                        PreparedStatement preppedInsert = SA.connection.prepareStatement(insertMatString);
                        preppedInsert.setInt(3, slot);
                        preppedInsert.setString(1, warpEditorMaterials.get(page).get(slot).toString());
                        preppedInsert.setShort(2, warpEditorMatData.get(page).get(slot));
                        preppedInsert.executeUpdate();
                    }
                }
            }          
            
            //thanks for not supporting arrays mySQL, making my life pain
            /*for(int page : warpEditorWarps.keySet()){
                for(int slot : warpEditorWarps.get(page).keySet()){
                    PreparedStatement preppedDifference = SA.connection.prepareStatement(deleteDifferenceString); //error here - why?
                Object[] slotArray = warpEditorWarps.get(page).keySet().toArray();
                Array array = SA.connection.createArrayOf("INT", slotArray);
                preppedDifference.setArray(1, array);
                preppedDifference.executeUpdate();
                }
            }*/
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
      
            e.printStackTrace();
        } catch (NumberFormatException e) {
            main.getLogger().info("No Database found, warp saving failed.");
        }
        
    }
}
