package io.github.kilobytz.sa.gui;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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
    TreeMap<Integer,WarpEditor> warpEditorPages = new TreeMap<>();
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
    }

    public void openFirstEditorPage(Player p){
        warpEditorPages.get(0).open(p);
    }

    public void loadEditor(){
        for (int i = 0; i < warpEditorWarps.size(); i+=42) {
            warpEditorPages.put(i/42, new WarpEditor(warpEditorWarps.get(i/42),warpEditorMaterials.get(i/42), this,i/42));
        }
        if(warpEditorWarps.size() == 0){
            warpEditorPages.put(0, new WarpEditor(null,null,this,0));
        }
        for(int num : warpEditorPages.keySet()){
            if(warpEditorPages.size() > 1){
                if(num == 0){
                    warpEditorPages.get(num).setItem(53, warpEditorPages.get(num).makeItem(Material.NETHER_STAR, "Next", "Click me to go forward a page."), (player,object) -> {
                        ((TreeMap<Integer,WarpEditor>)object).get(num+1).open(player);
                    }); //only next
                    warpEditorPages.get(num).setActionObject(53, warpEditorPages);
                }
                if(num < (warpEditorPages.size()-1) && num != 0){
                    warpEditorPages.get(num).setItem(53, warpEditorPages.get(num).makeItem(Material.NETHER_STAR, "Next", "Click me to go forward a page."), (player,object) -> {
                        ((TreeMap<Integer,WarpEditor>)object).get(num+1).open(player);
                    }); //next
                    warpEditorPages.get(num).setActionObject(53, warpEditorPages);
                    warpEditorPages.get(num).setItem(45, warpEditorPages.get(num).makeItem(Material.NETHER_STAR, "Previous", "Click me to go back a page."), (player,object) -> {
                        ((TreeMap<Integer,WarpEditor>)object).get(num-1).open(player);
                    }); //previous
                    warpEditorPages.get(num).setActionObject(45, warpEditorPages);
                }
                if(num == (warpEditorPages.size()-1)){
                    warpEditorPages.get(num).setItem(45, warpEditorPages.get(num).makeItem(Material.NETHER_STAR, "Previous", "Click me to go back a page."), (player,object) -> {
                        ((TreeMap<Integer,WarpEditor>)object).get(num-1).open(player);
                    }); //only previous
                    warpEditorPages.get(num).setActionObject(45, warpEditorPages);
                }
            }
        }
    }

    public void openSelectPage(int slot,int pageNum,Player p){
        TreeMap<Integer,WarpSelectPage> select = new TreeMap<>();
        List<String> warps = wH.getAllWarpNames();
        for (int i = 0; i < warps.size(); i+= 42){
            int index = Math.min(i+42, warps.size());
            select.put(select.size(), new WarpSelectPage(warps.subList(i, index), this, slot, pageNum));
        }
        selectPages.put(p.getUniqueId(),select);
        for(int num : selectPages.get(p.getUniqueId()).keySet()){
            if(selectPages.get(p.getUniqueId()).size() > 1){
                if(num == 0){
                    selectPages.get(p.getUniqueId()).get(num).setItem(53, selectPages.get(p.getUniqueId()).get(num).makeItem(Material.NETHER_STAR, "Next", "Click me to go forward a page."), (player,object) -> {
                        ((HashMap<UUID,TreeMap<Integer,WarpSelectPage>>)object).get(player.getUniqueId()).get(num+1).open(player);
                    }); //only next
                    selectPages.get(p.getUniqueId()).get(num).setActionObject(53, selectPages);
                }
                if(num < (selectPages.get(p.getUniqueId()).size()-1) && num != 0){
                    selectPages.get(p.getUniqueId()).get(num).setItem(53, selectPages.get(p.getUniqueId()).get(num).makeItem(Material.NETHER_STAR, "Next", "Click me to go forward a page."), (player,object) -> {
                        ((HashMap<UUID,TreeMap<Integer,WarpSelectPage>>)object).get(player.getUniqueId()).get(num+1).open(player);
                    }); //only next
                    selectPages.get(p.getUniqueId()).get(num).setActionObject(53, selectPages); //next
                    selectPages.get(p.getUniqueId()).get(num).setActionObject(53, selectPages);
                    selectPages.get(p.getUniqueId()).get(num).setItem(45, selectPages.get(p.getUniqueId()).get(num).makeItem(Material.NETHER_STAR, "Previous", "Click me to go back a page."), (player,object) -> {
                        ((HashMap<UUID,TreeMap<Integer,WarpSelectPage>>)object).get(player.getUniqueId()).get(num-1).open(player);
                    }); //previous
                    selectPages.get(p.getUniqueId()).get(num).setActionObject(45, selectPages);
                }
                if(num == (selectPages.get(p.getUniqueId()).size()-1)){
                    selectPages.get(p.getUniqueId()).get(num).setItem(45, selectPages.get(p.getUniqueId()).get(num).makeItem(Material.NETHER_STAR, "Previous", "Click me to go back a page."), (player,object) -> {
                        ((HashMap<UUID,TreeMap<Integer,WarpSelectPage>>)object).get(player.getUniqueId()).get(num-1).open(player);
                    }); //only previous
                    selectPages.get(p.getUniqueId()).get(num).setActionObject(45, selectPages);
                }
            }
        }
        selectPages.get(p.getUniqueId()).get(0).open(p);
    }

    public void deleteWarpEditorItem(int slot, int pageNum,Player p){
        warpEditorPages.get(pageNum).setEmptyWarp(slot);
        warpEditorWarps.get(pageNum).remove(slot);
        warpEditorMaterials.get(pageNum).remove(slot);
        selectPages.remove(p.getUniqueId());
        warpEditorPages.get(pageNum).open(p);
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
        warpEditorWarps.get(pageNum).put(slot, warp);
        warpEditorMaterials.get(pageNum).put(slot, mat);
        selectPages.remove(p.getUniqueId());
        warpEditorPages.get(pageNum).open(p);
    }

    public void loadGuiWarps(){
        try {
            main.openConnection();
            Statement statement = SA.connection.createStatement();
            int pageCount = 0;
            ResultSet rs = statement.executeQuery("SELECT * FROM gui_warps;");{
            while(rs.next()) {
                if(rs.getInt(1)/42 > pageCount){
                    ++pageCount;
                    warpEditorWarps.put(pageCount, new TreeMap<Integer, String>());
                    warpEditorMaterials.put(pageCount, new TreeMap<Integer, Material>());
                }
                warpEditorWarps.get(pageCount).put(rs.getInt(1), rs.getString(2));
                if(rs.getString(3) != null){
                    warpEditorMaterials.get(pageCount).put(rs.getInt(1), Material.getMaterial(rs.getString(3)));
                }
                else{
                    warpEditorMaterials.get(pageCount).put(rs.getInt(1), Material.SNOW_BALL);
                }
            }
        }
        statement.close();
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

    public void saveGuiWarps(){
        try {
            main.openConnection();
            String insertString = "INSERT INTO gui_warps (slot, warps_name) VALUES (?,?) ON DUPLICATE KEY UPDATE warps_name = ?";
            String deleteString = "DELETE FROM gui_warps WHERE warps_name = ?";
            for(int page : warpEditorWarps.keySet()) {
                for(int slot : warpEditorWarps.get(page).keySet()){
                    PreparedStatement preppedInsert = SA.connection.prepareStatement(insertString);
                    preppedInsert.setInt(1, slot);
                    preppedInsert.setString(2, warpEditorWarps.get(page).get(slot));
                    preppedInsert.setString(3, warpEditorWarps.get(page).get(slot));
                    preppedInsert.executeUpdate();
                }
            }
            for(String warpName : wH.getWarpsToDelete()) {
                PreparedStatement preppedDelete = SA.connection.prepareStatement(deleteString);
                preppedDelete.setString(1, warpName);
                preppedDelete.executeUpdate();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
      
            e.printStackTrace();
        } catch (NumberFormatException e) {
            main.getLogger().info("No Database found, warp saving failed.");
        }
    }
}
