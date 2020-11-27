package io.github.kilobytz.sa.ranks;

import java.lang.reflect.*;
import io.github.kilobytz.sa.SA;
import io.github.kilobytz.sa.command.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.*;

public class RankManager {

    SA main;
    HashMap<String, ChatColor> ranks = new HashMap<>();

    public RankManager(SA main) {
        this.main = main;
        ranks.put("Admin",ChatColor.RED);
        ranks.put("Builder",ChatColor.GREEN);
        ranks.put("Owner",ChatColor.BLUE);
    }

    public void builder(Player player) {
        player.addAttachment(main,"minecraft.command.gamemode",true);
        player.addAttachment(main,"minecraft.command.teleport",true);
        player.addAttachment(main,"sa.warp",true);

    }

    public void admin(Player player) {
        if(!player.isOp()) {
           player.setOp(true);
        }
        player.addAttachment(main, "minecraft.command.op", false);
        player.addAttachment(main, "minecraft.command.deop",false);
        player.addAttachment(main, "minecraft.command.stop",false);
        player.addAttachment(main, "sa.admin",false);
    }
    public void owner(Player player) {
        removeRanks(player);
        main.getConfig().set("users." + player.getUniqueId().toString(), "owner");
        main.saveConfig();
        player.setOp(true);
    }

    public boolean removeRanks(Player player) {
        if(doesPlayerHaveRank(player)) {
            if(player.isOp()) {
                player.setOp(false);
            }
            PermissionAttachment pa = new PermissionAttachment(main, player);
            Set perms = player.getEffectivePermissions();
            for (Object perm : perms) {
                PermissionAttachmentInfo pai = (PermissionAttachmentInfo) perm;
                if(pai.getAttachment() != null) {
                    player.removeAttachment(pai.getAttachment());
                }
            }
            removeRankIntric(player);
            player.addAttachment(main, "minecraft.command.help", true);
            player.addAttachment(main, "minecraft.command.msg", true);
            player.addAttachment(main, "minecraft.command.me", true);
            player.addAttachment(main, "bukkit.command.version", true);
            player.addAttachment(main, "bukkit.command.help", true);
            player.addAttachment(main, "bukkit.command.plugins", true);
            player.addAttachment(main, "sa.general", true);
            
            return true;
        }
        return false;
    }
    
    public void removeRankIntric(Player player) {
        resetName(player);
        main.getConfig().set("users." + player.getUniqueId().toString(), null);
        main.saveConfig();
        //player.addAttachment(main, "minecraft.command.help", true);
        //player.addAttachment(main, "minecraft.command.msg", true);
        //player.addAttachment(main, "minecraft.command.me", true);
        // player.addAttachment(main, "bukkit.command.version", true);
        //player.addAttachment(main, "bukkit.command.help", true);
        //player.addAttachment(main, "bukkit.command.plugins", true);

    }

    public boolean doesPlayerHaveRank(Player player) {
        try {
            String ID = player.getUniqueId().toString();
            for (String key : main.getConfig().getConfigurationSection("users").getKeys(false)) {
                if (key.equalsIgnoreCase(ID)) {
                    if(!(main.getConfig().get("users." + ID) == null)) {
                        return true;
                    }

                }
            }
        }catch(NullPointerException e) {
            return false;
        }
        return false;
    }

    public boolean setRank(String name, String rank) {
        UUID playerID = Bukkit.getPlayer(name).getUniqueId();

        removeRanks(Bukkit.getPlayer(playerID));
        main.getConfig().set("users." + playerID.toString(), rank);
        main.saveConfig();

        switch (rank) {
            case "builder":
                builder(Bukkit.getPlayer(playerID));
                setTitle(Bukkit.getPlayer(playerID),rank);
                return true;
            case "admin" :
                admin(Bukkit.getPlayer(playerID));
                setTitle(Bukkit.getPlayer(playerID),rank);
                return true;
            case "owner" :
                owner(Bukkit.getPlayer(playerID));
                setTitle(Bukkit.getPlayer(playerID),rank);
                return true;
            default:
                main.getConfig().set("users." + playerID.toString(), null);
                main.saveConfig();
                return true;
        }
    }


    public ArrayList<String> getRanks() {
        return new ArrayList<>(ranks.keySet());
    }

    public void setTitle(Player player,String rank) {
        for (Map.Entry<String, ChatColor> entry : ranks.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(rank)) {
                resetName(player);
                String name = player.getDisplayName();
                player.setDisplayName(ChatColor.GOLD + "[" + entry.getValue() + entry.getKey() + ChatColor.GOLD + "]" + ChatColor.WHITE + " " + name);
                player.setPlayerListName(ChatColor.GOLD + "[" + entry.getValue() + entry.getKey() + ChatColor.GOLD + "]" + ChatColor.WHITE + " " + name);
                player.setCustomName(ChatColor.GOLD + "[" + entry.getValue() + entry.getKey() + ChatColor.GOLD + "]" + ChatColor.WHITE + " " + name);
            }
        }
    }
    public void resetName(Player player) {
        player.setDisplayName(null);
        player.setPlayerListName(null);
    }

}
