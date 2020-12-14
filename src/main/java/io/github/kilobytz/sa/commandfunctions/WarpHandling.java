package io.github.kilobytz.sa.commandfunctions;

import io.github.kilobytz.sa.SA;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class WarpHandling {

    SA main;

    public WarpHandling(SA SA) {
        this.main = SA;
    }

    public void setWarp(String name, Location warpLoc) {

        main.getConfig().set("warps."+name,  warpLoc);
        main.saveConfig();
    }

    public List getAllWarps() {
        List<String> warps = new LinkedList();
        try {
            for (String key : main.getConfig().getConfigurationSection("warps").getKeys(false)) {
                warps.add(key);
            }
            return warps;
        }catch(NullPointerException e) {
            return null;
        }
    }

    public int getNumOfWarps() {
        Set<String> warps = main.getConfig().getConfigurationSection("warps").getKeys(false);
        return warps.size();
        }

    public boolean checkWarp(String name) {
        List warps = new LinkedList();
        try {
            for (String key : main.getConfig().getConfigurationSection("warps").getKeys(false)) {
                warps.add(key);
            }
            if (warps.contains(name)) {
                return true;
            }
            else{
                return false;
            }
        }catch(NullPointerException e) {
            return false;
        }
    }

    public Location getLocationFromWarp(String warp) {
        try {
            Location loc = (Location) main.getConfig().get("warps." + warp);
            return loc;
        }catch(NullPointerException e ) {
            return null;
        }
    }
    public void warpPlayer(Player player, String warp) {
        Location loc = getLocationFromWarp(warp);
        player.teleport(loc);
    }
    public void delWarp(String warp) {
        main.getConfig().set("warps."+warp, null);
        main.saveConfig();
    }

}
