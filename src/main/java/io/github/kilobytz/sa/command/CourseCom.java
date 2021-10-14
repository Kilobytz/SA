package io.github.kilobytz.sa.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import io.github.kilobytz.sa.misc.CourseHandling;

public class CourseCom implements TabExecutor {

    CourseHandling cH;
    Map<String,String> courseCommands = new HashMap<>();

    public void setup(CourseHandling cH) {this.cH = cH;}

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {

        if (command.getName().equalsIgnoreCase("course")) {
            if(args.length == 0){
                printCourseCommands(sender);
                return true;
            }
            args[0] = args[0].toLowerCase();
            switch(args[0]){
                case "start" :
                    if(args.length != 3 || Bukkit.getOnlinePlayers().size() == 0 || cH.courseExists(args[1])){
                        sender.sendMessage("Input error. /course start [course] [player]");
                        return true;
                    }
                    Player p1 = null;
                    if(args[2].equals("@p")){
                        if(sender instanceof BlockCommandSender){
                            p1 = getNearestPlayer(((BlockCommandSender)sender).getBlock().getLocation());
                        }
                        else{
                            p1 = getNearestPlayer(((Player)sender).getLocation());
                        }
                    }
                    else{
                        p1 = Bukkit.getPlayer(args[2]);
                    }
                    cH.startCourse(args[1], p1);
                    return true;
                case "end" :
                    Player p2 = null;
                    if(args[2].equals("@p")){
                        if(sender instanceof BlockCommandSender){
                            p2 = getNearestPlayer(((BlockCommandSender)sender).getBlock().getLocation());
                        }
                        else{
                            p2 = getNearestPlayer(((Player)sender).getLocation());
                        }
                    }
                    else{
                        p2 = Bukkit.getPlayer(args[2]);
                    }
                    if(!cH.isPlayerInCourse(p2)){
                        sender.sendMessage("Error player is not in a course.");
                        return true;
                    }
                    cH.endCourse(p2);
                    return true;
                case "checkpoint" :
                    if(args.length != 4 || Bukkit.getOnlinePlayers().size() == 0 || cH.courseExists(args[1])){
                        sender.sendMessage("Input error. /course checkpoint [course] [checkpoint num] [player]");
                        return true;
                    }
                    if(Integer.parseInt(args[2]) > cH.getCheckpoints(args[1])){
                        sender.sendMessage("Input error. /course checkpoint [course] [checkpoint num] [player]");
                        return true;
                    }
                    Player p3 = null;
                    if(args[3].equals("@p")){
                        if(sender instanceof BlockCommandSender){
                            p3 = getNearestPlayer(((BlockCommandSender)sender).getBlock().getLocation());
                        }
                        else{
                            p3 = getNearestPlayer(((Player)sender).getLocation());
                        }
                    }
                    else{
                        p3 = Bukkit.getPlayer(args[2]);
                    }
                    cH.setCheckpoint(args[1],Integer.parseInt(args[2]),p3);
                    return  true;
                case "create" :
                    int cpNum = 0;
                    try {
                        if(args.length != 2){
                            sender.sendMessage("Input error. /course create [course name] [number of checkpoints]");
                        }
                        cpNum = Integer.parseInt(args[2]);
                    } catch (NumberFormatException nfe) {
                        sender.sendMessage("Input error. /course create [course name] [number of checkpoints]");
                        return true;
                    }
                    cH.createCourse(args[1], cpNum);
                    return true;
                default :
                printCourseCommands(sender);
                return true;
            }
        }
        return false;
    }

    public void populateCourseCommands(){
        courseCommands.put("start", "/course start [course] [player] : Starts a course timer for the specified course and player.");
        courseCommands.put("end", "/course end [player] : Ends a course timer for the specified player.");
        courseCommands.put("checkpoint", "/course checkpoint [course] [checkpoint num] [player] : Sets the checkpoint for a course, checkpoint number and player.");
        courseCommands.put("create", "/course create [course name] [number of checkpoints] : Creates a course under the specified name. If you want no checkpoints, specify 0.");
        //courseCommands.put("leaderboard", "/course leaderboard [course] : Displays the leaderboard and your ranking for the specified course.");
    }

    public void printCourseCommands(CommandSender sender){
        for (Map.Entry<String, String> entry : courseCommands.entrySet()) {
            sender.sendMessage(entry.getValue());
        }
    }

    public Player getNearestPlayer(Location loc){
        double closest = Double.MAX_VALUE;
	    Player closestp = null;
	    for(Player i : Bukkit.getOnlinePlayers()){
		    double dist = i.getLocation().distance(loc);
		    if (closest == Double.MAX_VALUE || dist < closest){
			    closest = dist;
			    closestp = i;
            }
        }
        if (closestp == null){
            return null;
        }
        else{
            return closestp;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("course")) {
            ArrayList<String> fill = new ArrayList<>();
            /*List<String> courses = new ArrayList<>();
            if (args.length == 1 || args.length == 0) {
                if(args.length == 1){
                    if (!args[0].equals("")) {
                        for (String entry : courses) {
                            String tempEntry = entry;
                            if (tempEntry.toLowerCase().startsWith(args[0].toLowerCase())) {
                                fill.add(tempEntry);
                            }
                        }
                    } else {
                        for (String entry : courses) {
                            fill.add(entry);
                        }
                    }
                    return fill;
                }
            }*/
            return fill;
        }
        return null;
    }
}