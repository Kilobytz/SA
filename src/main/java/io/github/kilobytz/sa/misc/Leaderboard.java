package io.github.kilobytz.sa.misc;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import io.github.kilobytz.sa.SA;


public class Leaderboard{
    
    SA main;
    HashMap<Player, String> leaderboards = new HashMap<>();

    public Leaderboard(SA main){
        this.main = main;
    }

    public void printLeaderboard(Player player, String courseName){
        queryLeaderboard(player, courseName);
    }

    public void closeLeaderboard(Player player){
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    public void queryLeaderboard(Player player, String course){
        try {    
            main.openConnection();
            Statement statement = SA.connection.createStatement();
            ResultSet rs1 = statement.executeQuery("SELECT * FROM coursetimes WHERE coursename = '"+course+"' ORDER BY coursetime ASC;");
            int i = 0;
            player.sendMessage("Course '"+course+"' Leaderboard");
            while(rs1.next()&& i <10) {
                player.sendMessage(i+1 + " - " + rs1.getString(3) + " - " + rs1.getDouble(4));
                i +=1;
            }
            ResultSet rs2 = statement.executeQuery("SELECT EXISTS(SELECT coursetime FROM coursetimes WHERE ign =  '"+player.getDisplayName()+"' AND coursename = '"+course+"');");
            if(rs2.next()){
                if(rs2.getInt(1)== 1){
                    ResultSet rs3 = statement.executeQuery("SELECT coursetime FROM coursetimes WHERE ign =  '"+player.getDisplayName()+"' AND coursename = '"+course+"';");
                    if(rs3.next()){
                        player.sendMessage("Your Time - " + player.getDisplayName() + " - " + rs3.getDouble(1));
                    }
                }
            }
            statement.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
