package io.github.kilobytz.sa.tips;

import io.github.kilobytz.sa.SA;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TipManager {

    SA main;

    List<String> tipsList = new ArrayList<String>();
    List<String> tipsToDelete = new ArrayList<String>();
    List<String> blacklistedTips = new ArrayList<String>();

    public TipManager(SA main) {
        this.main = main;
    }

    public void startTips() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {

            @Override
            public void run() {
                String tipToPrint = getTip();
                for(Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(ChatColor.DARK_PURPLE + "TIP: " + tipToPrint);
                }
                blacklistTip(tipToPrint);
            }
        },50L, 3000);
    }

    public void blacklistTip(String tip) {
        if(blacklistedTips.size() > 3) {
            blacklistedTips.remove(0);
        }
        blacklistedTips.add(tip);
    }

    public void setTip(String tip) {
        if(tipsList.contains(tip)){
            tipsList.set(tipsList.indexOf(tip), tip);
            return;
        }
        tipsList.add(tip);
    }

    public void delTip(int tipNum) {
        tipsToDelete.add(tipsList.get(tipNum));
        tipsList.remove(tipNum);
    }

    public boolean isTipNumberValid(int num){
        try{
            tipsList.get(num);
            return true;
        }
        catch(IndexOutOfBoundsException e) {
            return false;
        }
    }

    public String getTip() {
        boolean tipValid = false;
        while(!tipValid){
            if(tipsList.size() != 0){
                Random random = new Random();
                String tip = tipsList.get((random.nextInt(tipsList.size())));
                if(blacklistedTips.contains(tip)){
                    continue;
                }
                return tip;
            }
            return null;
        }
        return null;
        
    }

    public void listTips(CommandSender sender) {
        if(tipsList.size() == 0) {
            sender.sendMessage(ChatColor.RED + "There are no tips added.");
            return;
        }
        for(String tip : tipsList){
            sender.sendMessage(Integer.toString(tipsList.indexOf(tip)) + " : " + tip);
        }
    }

    public void loadTips() {
        try {
            main.openConnection();
            Statement statement = SA.connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM tips;");{
            while(rs.next()) {
                tipsList.add(rs.getString(1));
                }
            }
            statement.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
      
            e.printStackTrace();
        } catch (NumberFormatException e) {
            main.getLogger().info("No Database found, tip loading failed.");
        }
    }

    public void saveTips(){
        try {
            main.openConnection();
            String insertString = "INSERT INTO tips (message) VALUES (?) ON DUPLICATE KEY UPDATE message = ?";
            String deleteString = "DELETE FROM tips WHERE message = ?";
            for(String tip : tipsList) {
                PreparedStatement preppedInsert = SA.connection.prepareStatement(insertString);
                preppedInsert.setString(1, tip);
                preppedInsert.setString(2, tip);
                preppedInsert.executeUpdate();
            }
            for(String tip : tipsToDelete) {
                PreparedStatement preppedDelete = SA.connection.prepareStatement(deleteString);
                preppedDelete.setString(1, tip);
                preppedDelete.executeUpdate();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
      
            e.printStackTrace();
        } catch (NumberFormatException e) {
            main.getLogger().info("No Database found, tip saving failed.");
        } 
    }

}
