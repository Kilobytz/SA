package io.github.kilobytz.sa.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import io.github.kilobytz.sa.misc.CourseHandling;

public class LeaderboardCom implements TabExecutor {

    CourseHandling cH;
    public void setup(CourseHandling cH) {this.cH = cH;}

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {


        if (command.getName().equalsIgnoreCase("leaderboard")) {
            if(!cH.courseExists(args[0])){
                sender.sendMessage(ChatColor.RED + "Error. No such course exists.");
                return true;
            }
            cH.openLeaderboard((Player)sender, args[0]);
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("leaderboard")) {
            return new ArrayList<>();
        }
        return null;
    }
}
