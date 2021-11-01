package io.github.kilobytz.sa.misc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.entity.Player;

import io.github.kilobytz.sa.SA;
import io.github.kilobytz.sa.players.PlayerManager;
import io.github.kilobytz.sa.players.PracPlayer;

public class CourseHandling {

    PlayerManager playerManager;
    SA main;
    private HashMap<String,Integer> courses = new HashMap<>();
    Leaderboard leaderboard;

    public CourseHandling(SA main, PlayerManager playerManager, Leaderboard leaderboard){
        this.main = main;
        this.playerManager = playerManager;
        this.leaderboard = leaderboard;
    }

    public boolean courseExists(String course){
        if(courses.containsKey(course)){
            return true;
        }
        else{
            return false;
        }
    }

    public void openLeaderboard(Player player, String course){
        leaderboard.printLeaderboard(player, course);
    }


    public boolean checkCpNum(String num,String course){
        if(Integer.parseInt(num)> courses.get(course) ){
            return true;
        }
        return false;
    }

    public boolean checkCpOrder(String cpNum,Player name){
        if(Integer.parseInt(cpNum)== 0|| Integer.parseInt(cpNum)== 1){
            return false;
        }
        PracPlayer play = playerManager.getPlayerInst(name);
        if(play.getCpStatus(Integer.parseInt(cpNum)-1)){
            return true;
        }
        return false;
    }

    public void setCheckpoint(int cpNum, Player name){
        PracPlayer play = playerManager.getPlayerInst(name);
        if(play.isPlayerInCourse()){
            play.tickCheckpoint(cpNum);
        }
    }

    public void startCourse(String course,Player name){
        PracPlayer play = playerManager.getPlayerInst(name);
        play.setCourse(course,courses.get(course));
        play.setCheckpoints(courses.get(course));
    }

    public void createCourse(String course, int cpNum){
        courses.put(course, cpNum);
        saveCourse(course, cpNum);
    }

    public void removeCourse(String course){
        courses.remove(course);
        deleteCourse(course);
    }

    public void endCourse(Player name){
        PracPlayer play = playerManager.getPlayerInst(name);
        play.stopCourse(main,leaderboard);
    }
    public boolean isPlayerInCourse(Player player){
        PracPlayer play = playerManager.getPlayerInst(player);
        return play.isPlayerInCourse();
    }
    /*public int getCheckpointNum(String cpName){
        try {    
            main.openConnection();
            Statement statement = SA.connection.createStatement();
            ResultSet rs1 = statement.executeQuery("SELECT checkpoints FROM courses WHERE coursename =  '"+cpName+"';");
            if(rs1.next()) {
                return rs1.getInt(1);
            }
            statement.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
          return 0;
        }
        return 0;      
    }*/

    public void saveCourse(String course, int cpNum){
        try {    
            main.openConnection();
            Statement statement = SA.connection.createStatement();
            statement.executeUpdate("INSERT INTO courses (coursename, checkpoints) VALUES ('"+ course +"', '"+ cpNum +"') ON DUPLICATE KEY UPDATE checkpoints = '"+ cpNum +"';");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCourse(String course){
        try {    
            main.openConnection();
            Statement statement = SA.connection.createStatement();
            statement.executeUpdate("DELETE FROM courses WHERE coursename = '"+ course +"';");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadCourses(){
        try {    
            main.openConnection();
            Statement statement = SA.connection.createStatement();
            ResultSet rs1 = statement.executeQuery("SELECT * FROM courses;");
            while(rs1.next()) {
              courses.put(rs1.getString(1), rs1.getInt(2));
            }
            statement.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
