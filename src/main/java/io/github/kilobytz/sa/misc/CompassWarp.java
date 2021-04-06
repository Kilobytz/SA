package io.github.kilobytz.sa.misc;

import java.util.LinkedList;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.kilobytz.sa.SA;
import io.github.kilobytz.sa.commandfunctions.WarpHandling;


public class CompassWarp implements Listener{ 

    SA main;
    WarpHandling warpHandling;

    public CompassWarp(SA main, WarpHandling warpHandling) {
        this.main = main;
        this.warpHandling = warpHandling;        
    }



    @EventHandler
    public void onCompassInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if(item != null){
            net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
            if(item.getType().equals(Material.COMPASS) || nmsItem.hasTag()) {
                final Player player = event.getPlayer();
                if(nmsItem.hasTag()) {
                    if(nmsItem.getTag().hasKey("warper") && nmsItem.getTag().getInt("warper") == 1) {
                        player.openInventory(warper());
                        return;
                    }
                    if(nmsItem.getTag().hasKey("warper") && nmsItem.getTag().getInt("warper") == 2) {
                        player.openInventory(warpSetter());
                        return;
                    }
                }
            }
        }
    }



    @EventHandler
    public void itemClick (InventoryClickEvent event) {
        if(event.getInventory().getTitle().equalsIgnoreCase("warps")
        || event.getInventory().getTitle().equalsIgnoreCase("warp editor")
        || event.getInventory().getTitle().equalsIgnoreCase("warp add")) {
            if(event.getInventory().getTitle().equalsIgnoreCase("warp editor")) {
                int lines = (int) main.getConfig().get("compass." + "lines");
                if(event.getCurrentItem().getType().equals(Material.INK_SACK) && event.getCurrentItem().getDurability() == 10) {
                    if(lines == 5) {
                        event.setCancelled(true);
                        return;
                    }
                    ++lines;
                    main.getConfig().set("compass."+"lines", lines);
                    main.saveConfig();
                    Inventory warpInv = warpSetter();
                    event.getWhoClicked().openInventory(warpInv);
                    return;   
                }
                if(event.getCurrentItem().getType().equals(Material.INK_SACK) && event.getCurrentItem().getDurability() == 1) {
                    if(lines == 0) {
                        event.setCancelled(true);
                        return;
                    }
                    --lines;
                    main.getConfig().set("compass."+"lines", lines);
                    main.saveConfig();
                    Inventory warpInv = warpSetter();
                    event.getWhoClicked().openInventory(warpInv);
                    return;   
                }
                if(event.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE) || event.getCurrentItem().getType().equals(Material.ENDER_PEARL)) {
                    Inventory addInv = addWarps(event.getRawSlot(),0);
                    event.getWhoClicked().openInventory(addInv);
                    return;   
                }
            }
            if(event.getInventory().getTitle().equalsIgnoreCase("warp add")) {
                if(event.getCurrentItem().getType().equals(Material.INK_SACK) && event.getCurrentItem().getDurability() == 9) {
                    Inventory addInv = addWarps(event.getInventory().getItem(8).getAmount(),
                    event.getInventory().getItem(0).getAmount());
                    event.getWhoClicked().openInventory(addInv);
                    return; 
                }
                if(event.getCurrentItem().getType().equals(Material.INK_SACK) && event.getCurrentItem().getDurability() == 13) {
                    Inventory addInv = addWarps(event.getInventory().getItem(8).getAmount(),
                    event.getInventory().getItem(0).getAmount()-2);
                    event.getWhoClicked().openInventory(addInv);
                    return;   
                }
                if(event.getCurrentItem().getType().equals(Material.BARRIER)) {
                    Inventory warpInv = warpSetter();
                    event.getWhoClicked().openInventory(warpInv);
                    return; 
                }
                if(event.getCurrentItem().getType().equals(Material.EYE_OF_ENDER)) {
                    main.getConfig().set("compass."+ event.getInventory().getItem(8).getAmount(),
                      event.getCurrentItem().getItemMeta().getDisplayName());
                    main.saveConfig();
                    Inventory warpInv = warpSetter();
                    event.getWhoClicked().openInventory(warpInv);
                    return;
                }
                if(event.getCurrentItem().getType().equals(Material.FIREBALL)) {
                    int id = event.getInventory().getItem(8).getAmount(); 
                    main.getConfig().set("compass."+ id,  null);
                    main.saveConfig();
                    Inventory warpInv = warpSetter();
                    event.getWhoClicked().openInventory(warpInv);
                    return; 
                }

            }
            if(event.getInventory().getTitle().equalsIgnoreCase("warps")) {
                if(event.getCurrentItem().getType().equals(Material.SKULL_ITEM) && event.getCurrentItem().getDurability() == 0) {
                    event.getWhoClicked().setHealth(0);
                    event.setCancelled(true);
                    event.getWhoClicked().closeInventory();
                    return;
                }
                if(event.getCurrentItem().getType().equals(Material.SKULL_ITEM) && event.getCurrentItem().getDurability() == 3) {
                    event.getWhoClicked().teleport(event.getWhoClicked().getWorld().getSpawnLocation());
                    event.setCancelled(true);
                    event.getWhoClicked().closeInventory();
                    return;
                }
                if(event.getCurrentItem().getType().equals(Material.ENDER_PEARL)) {
                    String warpName = event.getCurrentItem().getItemMeta().getDisplayName();
                    warpHandling.warpPlayer((Player)event.getWhoClicked(), warpName);
                    event.setCancelled(true);
                    event.getWhoClicked().closeInventory();
                    return;
                }
            }
            event.setCancelled(true);
        }
    }

    public Inventory warper() {
        int lines = (int) main.getConfig().get("compass." + "lines");

        Inventory playerWarpInv = Bukkit.createInventory(null, (9 +(lines*9)),"Warps");

        ItemStack die = new ItemStack(Material.SKULL_ITEM, 1, (short) 0);
        ItemMeta dieInfo = die.getItemMeta();
        dieInfo.setDisplayName("Suicide");
        die.setItemMeta(dieInfo);
        playerWarpInv.setItem(0, die);

        ItemStack spawn = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta spawnInfo = spawn.getItemMeta();
        spawnInfo.setDisplayName("Spawn");
        spawn.setItemMeta(spawnInfo);
        playerWarpInv.setItem(4, spawn);
        
        Set<String> compassData = main.getConfig().getConfigurationSection("compass.").getKeys(false);


        if(lines >= 1) {
            for(int i = (9*1); i <= ((lines+1)*9)-1; ++i) {
                if(compassData.contains(Integer.toString(i))) {
                    ItemStack warpPearl = new ItemStack(Material.ENDER_PEARL, 1);
                    ItemMeta warp = warpPearl.getItemMeta();
                    warp.setDisplayName((String) main.getConfig().get("compass." + i));
                    warpPearl.setItemMeta(warp);
                    playerWarpInv.setItem(i, warpPearl);
                }
                
            }
        }
        return playerWarpInv;

    }

    public Inventory warpSetter() {
        int lines = (int) main.getConfig().get("compass." + "lines");
        Inventory warpInv = Bukkit.createInventory(null, (9 +(lines*9)),"Warp Editor");

        ItemStack addLine = new ItemStack(Material.INK_SACK, 1, (short) 10);
        ItemMeta addLineInfo = addLine.getItemMeta();

        if(lines == 5) {
            addLineInfo.setDisplayName("AT MAX LINES");
            addLine.setItemMeta(addLineInfo);
            warpInv.setItem(3, addLine);
        }
        else {
            addLineInfo.setDisplayName("ADD NEW LINE");
            addLine.setItemMeta(addLineInfo);
            warpInv.setItem(3, addLine);
        }

        ItemStack removeLine = new ItemStack(Material.INK_SACK, 1, (short) 1);
        ItemMeta removeLineInfo = removeLine.getItemMeta();

        if(lines == 0) {
            removeLineInfo.setDisplayName("NO LINES TO REMOVE");
            removeLine.setItemMeta(removeLineInfo);
            warpInv.setItem(5, removeLine);
        }
        else {
            removeLineInfo.setDisplayName("REMOVE LINE");
            removeLine.setItemMeta(removeLineInfo);
            warpInv.setItem(5, removeLine);
        }
        
        ItemStack emptyPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemMeta paneMeta = emptyPane.getItemMeta();
        paneMeta.setDisplayName("CLICK TO ADD WARP");
        emptyPane.setItemMeta(paneMeta);



        Set<String> compassData = main.getConfig().getConfigurationSection("compass.").getKeys(false);


        if(lines >= 1) {
            for(int i = (9*1); i <= ((lines+1)*9)-1; ++i) {
                if(compassData.contains(Integer.toString(i))) {
                    ItemStack warpPearl = new ItemStack(Material.ENDER_PEARL, 1);
                    ItemMeta warp = warpPearl.getItemMeta();
                    warp.setDisplayName((String) main.getConfig().get("compass." + i));
                    warpPearl.setItemMeta(warp);
                    warpInv.setItem(i, warpPearl);
                }else{
                    warpInv.setItem(i, emptyPane);
                }
                
            }
        }

        return warpInv;
    }

    public Inventory addWarps(int invSlot, int page) {
        Inventory warpAdd = Bukkit.createInventory(null, 54,"Warp Add");

        ItemStack reverse = new ItemStack(Material.BARRIER, 1);
        ItemMeta reverseInfo = reverse.getItemMeta();
        reverseInfo.setDisplayName("RETURN");
        reverse.setItemMeta(reverseInfo);
        warpAdd.setItem(4, reverse);

        ItemStack pageNum = new ItemStack(Material.BOOK, page+1);
        ItemMeta pageNumInfo = pageNum.getItemMeta();
        pageNumInfo.setDisplayName("PAGE NO.");
        pageNum.setItemMeta(pageNumInfo);
        warpAdd.setItem(0, pageNum);

        ItemStack forward = new ItemStack(Material.INK_SACK, 1, (short) 9);
        ItemMeta forwardInfo = forward.getItemMeta();
        forwardInfo.setDisplayName("PAGE FORWARD");
        forward.setItemMeta(forwardInfo);
        warpAdd.setItem(5, forward);

        ItemStack slot = new ItemStack(Material.BOWL, invSlot);
        ItemMeta slotInfo = slot.getItemMeta();
        slotInfo.setDisplayName("WARP SLOT");
        slot.setItemMeta(slotInfo);
        warpAdd.setItem(8, slot);

        ItemStack clear = new ItemStack(Material.FIREBALL, 1);
        ItemMeta clearInfo = clear.getItemMeta();
        clearInfo.setDisplayName("CLEAR SLOT");
        clear.setItemMeta(clearInfo);
        warpAdd.setItem(2, clear);

        if(page != 0) {

            ItemStack back = new ItemStack(Material.INK_SACK, 1, (short) 13);
            ItemMeta backInfo = back.getItemMeta();
            backInfo.setDisplayName("PAGE BACK");
            back.setItemMeta(backInfo);
            warpAdd.setItem(3, back);

        }
        LinkedList<String> warps = new LinkedList<>();
        try {
        for (String key : main.getConfig().getConfigurationSection("warps").getKeys(false)) {
            warps.add(key);

            }
        }catch(NullPointerException | IndexOutOfBoundsException e) {
        }
        int j = 0;


        for(int i = 45*page; i <= ((page+1)*45); ++i,++j) {
            try{
                ItemStack warpName = new ItemStack(Material.EYE_OF_ENDER, 1);
                ItemMeta warp = warpName.getItemMeta();
                warp.setDisplayName(warps.get(i));
                warpName.setItemMeta(warp);
                warpAdd.setItem(9+j, warpName);
            }catch(NullPointerException | IndexOutOfBoundsException e) {
                }
            }


        return warpAdd;
    }
    
}
