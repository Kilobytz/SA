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
    }

    public void builder(Player player) {
        player.addAttachment(main,"minecraft.command.gamemode",true);
        player.addAttachment(main,"minecraft.command.teleport",true);
        player.addAttachment(main,"sa.warp",true);

    }
    public boolean removeRanks(Player player) {
        if(player.isOp()) {
            player.setOp(false);
            resetName(player);
            return true;
        }
        if(doesPlayerHaveRank(player)) {
            PermissionAttachment pa = new PermissionAttachment(main, player);
            Set perms = player.getEffectivePermissions();
            for (Object perm : perms) {
                PermissionAttachmentInfo pai = (PermissionAttachmentInfo) perm;
                if(pai.getAttachment() != null) {
                    player.sendMessage(pai.getPermission());
                    player.removeAttachment(pai.getAttachment());
                }
            }
            resetName(player);
            main.getConfig().set("users." + player.getUniqueId().toString(), null);
            main.saveConfig();
            player.addAttachment(main, "minecraft.command.help", true);
            return true;
        }
        return false;
    }

    public boolean doesPlayerHaveRank(Player player) {
        try {
            String ID = player.getUniqueId().toString();
            for (String key : main.getConfig().getConfigurationSection("users").getKeys(false)) {
                if (key.equalsIgnoreCase(ID)) {
                    if(main.getConfig().get("users." + ID) == null) {
                        main.saveConfig();
                        return false;
                    }
                }
            }
        }catch(NullPointerException e) {
            return false;
        }
        return true;
    }

    public boolean setRank(String name, String rank) {
        UUID playerID = Bukkit.getPlayer(name).getUniqueId();
        List users = new LinkedList();

        main.getConfig().set("users." + playerID.toString(), rank);
        main.saveConfig();

        switch (rank) {
            case "builder":
                removeRanks(Bukkit.getPlayer(playerID));
                builder(Bukkit.getPlayer(playerID));
                setTitle(Bukkit.getPlayer(playerID),rank);
                return true;
            case "admin" :
                Bukkit.getPlayer(playerID).setOp(true);
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
