package io.github.kilobytz.sa.misc;

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

    public CourseHandling(SA main, PlayerManager playerManager){
        this.main = main;
        this.playerManager = playerManager;
    }

    public boolean courseExists(String course){
        if(courses.keySet().contains(course)){
            return true;
        }
        else{
            return false;
        }
    }

    public int getCheckpoints(String course){
        return courses.get(course);
    }

    public void setCheckpoint(String course, int cpNum, Player name){

    }

    public void startCourse(String course,Player name){
        PracPlayer play = playerManager.getPlayerInst(name);
        play.setCourse(course, main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
            public void run() {
                
            }
        }, 150L));
    }

    public void createCourse(String course, int cpNum){
        courses.put(course, cpNum);
        //db register
    }

    public void endCourse(Player name){
        
    }
    public boolean isPlayerInCourse(Player player){
        return false;
    }

}
