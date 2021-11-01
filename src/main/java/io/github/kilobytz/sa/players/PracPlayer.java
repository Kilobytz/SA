package io.github.kilobytz.sa.players;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import io.github.kilobytz.sa.SA;
import io.github.kilobytz.sa.misc.Leaderboard;
import io.github.kilobytz.sa.players.ranks.Rank;
import io.github.kilobytz.sa.players.ranks.RankManager;
import io.netty.channel.VoidChannelPromise;

public class PracPlayer {
    
    private final UUID playerID;
    private Rank rank;
    private String courseName;
    private Instant courseTime;
    private HashMap<Integer,Boolean> courseCP;

    public PracPlayer(UUID playerID, String rank) {
        this.playerID = playerID;
        setRank(rank);
    }

    public UUID getID() {
        return playerID;
    }

    public void setRankInitial(SA main,String rank) {
        rank = rank.substring(0,1).toUpperCase() + rank.substring(1).toLowerCase();
        setRank(rank);
        if(this.rank != null) {
            this.rank.setPerms(main,Bukkit.getPlayer(playerID));
        }
    } 

    public void setRank(String rank) {
        this.rank = RankManager.getRank(rank);
    }

    public Rank getRank() {
        return rank;
    }

    public void removeRank(){
        rank = null;
    }

    public String getRankName(){
        if(rank != null) {
            return rank.getName();
        }
        return "NULL";
    }

    public boolean equals(Player player) {
        if(player.getUniqueId() == playerID) {
            return true;
        }
        return false;
    }
    public boolean equals(UUID id) {
        if(id == playerID) {
            return true;
        }
        return false;
    }
    
    public boolean hasRank() {
        if(rank == null) {
            return false;
        }
        return true;
    }

    public void setCourse(String courseName,int num){
        if(Bukkit.getPlayer(playerID).getGameMode() != GameMode.ADVENTURE){
            Bukkit.getPlayer(playerID).sendMessage("You cannot start in this gamemode!");
            return;
        }
        Bukkit.getPlayer(playerID).sendMessage("Course " + courseName + " started");
        courseTime = Instant.now();
        this.courseName = courseName;
        setCheckpoints(num);
    }

    public void setCheckpoints(int cpNum){
        if(cpNum > 0){
            courseCP = new HashMap<>();
            for(int i = 1; i <= cpNum; ++i){
                courseCP.put(i, false);
            }
        }
    }

    public boolean getCpStatus(int cp){
        return courseCP.get(cp);
    }

    public void tickCheckpoint(int num){
        courseCP.put(num, true);
    }

    public void cancelCourse(){
        courseName = null;
        courseTime = null;
        courseCP = null;
    }
    public void stopCourse(SA main,Leaderboard leaderboard){
        if(courseCP != null){
            if(courseCP.values().contains(false)){
                return;
            }
        }
        double time = (double)Duration.between(courseTime, Instant.now()).toMillis()/1000;
        saveTime(time, main,leaderboard);
        cancelCourse();
        Bukkit.getPlayer(this.getID()).sendMessage("Your time was "+ time + " seconds.");
    }

    public boolean isPlayerInCourse(){
        if(courseName != null){
            return true;
        }
        return false;
    }

    public void saveTime(double time,SA main,Leaderboard leaderboard){
        try {
            main.openConnection();
            Statement statement = SA.connection.createStatement();
            ResultSet rs1 = statement.executeQuery("SELECT EXISTS(SELECT coursetime FROM coursetimes WHERE ign =  '"+Bukkit.getPlayer(this.getID()).getDisplayName()+"' AND coursename = '"+this.courseName+"');");
            if(rs1.next()) {
                if(rs1.getInt(1) == 1) {
                    ResultSet rs2 = statement.executeQuery("SELECT coursetime FROM coursetimes WHERE ign =  '"+Bukkit.getPlayer(this.getID()).getDisplayName()+"' AND coursename = '"+this.courseName+"'");
                    rs2.next();
                    if(rs2.getDouble(1) < time){
                        return;
                    }
                    statement.executeUpdate("UPDATE coursetimes SET coursetime = '"+time+"' WHERE coursename = '"+this.courseName+"' AND ign = '" +Bukkit.getPlayer(this.getID()).getDisplayName()+ "';");
                
                }else{
                    statement.executeUpdate("INSERT INTO coursetimes (coursename, ign, coursetime) VALUES ('"+ this.courseName +"','"+Bukkit.getPlayer(this.getID()).getDisplayName()+"', '"+ time +"');");
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
