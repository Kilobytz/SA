package io.github.warping;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import io.github.kilobytz.sa.SA;

public class WarpHandling {

    SA main;

    Map<String,Location> warps = new HashMap<String,Location>();
    List<String> warpsToDelete = new LinkedList<String>();

    public WarpHandling(SA SA) {
        this.main = SA;
    }

    public void setWarp(String name, Location warpLoc) {
        warps.put(name, warpLoc);
    }

    public List<String> getAllWarpNames() {
        List<String> warpNames = new LinkedList<String>();
        warpNames.addAll(warps.keySet());
        return warpNames;
    }

    public int getNumOfWarps() {
        return warps.size();
        }

    public boolean checkWarp(String name) {
        if(warps.keySet().contains(name)) {
            return true;
        }
        return false;
    }


    public void warpPlayer(Player player, String warp) {
        player.teleport(warps.get(warp));
    }
    public void delWarp(String warp) {
        warpsToDelete.add(warp);
        warps.remove(warp);
    }

    public String unpackageLocationSerialised(Location loc) {
        StringBuilder sb = new StringBuilder();
        for(Object locObj : loc.serialize().values()) {
            sb.append(locObj).append(",");
        }
         return  sb.deleteCharAt(sb.length()-1).toString();
    }

    public Location repackageLocationSerialised(String dbLocation) {
        String[] locData = dbLocation.split(",");
        Location location = new Location(Bukkit.getServer().getWorld(locData[0]), Double.parseDouble(locData[1]), Double.parseDouble(locData[2]), Double.parseDouble(locData[3]));
        location.setPitch(Float.parseFloat(locData[4]));
        location.setYaw(Float.parseFloat(locData[5]));
        return location;
    }
    public String getWorldName(String dbLoc){
        String[] locData = dbLoc.split(",");
        return locData[0];
    }

    public void loadWarps() {
        try {
            main.openConnection();
            Statement statement = SA.connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM warps;");{
            while(rs.next()) {
                if(Bukkit.getWorld(getWorldName(rs.getString(2))) == null){
                    WorldCreator wc = new WorldCreator(getWorldName(rs.getString(2)));
                    Bukkit.getServer().createWorld(wc);
                }
                warps.put(rs.getString(1), repackageLocationSerialised(rs.getString(2)));
            }
        }
        statement.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
      
            e.printStackTrace();
        } catch (NumberFormatException e) {
            main.getLogger().info("No Database found, warp loading failed.");
        }
    }

    public void saveWarps(){
        try {
            main.openConnection();
            String insertString = "INSERT INTO warps (name, location) VALUES (?,?) ON DUPLICATE KEY UPDATE location = ?";
            String deleteString = "DELETE FROM warps WHERE name = ?";
            for(String warpName : warps.keySet()) {
                PreparedStatement preppedInsert = SA.connection.prepareStatement(insertString);
                preppedInsert.setString(1, warpName);
                preppedInsert.setString(2, unpackageLocationSerialised(warps.get(warpName)));
                preppedInsert.setString(3, unpackageLocationSerialised(warps.get(warpName)));
                preppedInsert.executeUpdate();
            }
            for(String warpName : warpsToDelete) {
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
