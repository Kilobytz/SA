package io.github.kilobytz.sa.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import io.github.kilobytz.sa.warping.WarpHandling;

public class DelWarp implements TabExecutor {

    WarpHandling warpHandling;

    public void setup(WarpHandling warpHandling) {this.warpHandling = warpHandling;}

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {
        if (command.getName().equalsIgnoreCase("delwarp")) {
            if (sender instanceof Player) {
                Player playerSent = (Player) sender;
                int length = args.length;
                if (playerSent.isOp()) {
                    if (length == 0) {
                        playerSent.sendMessage(String.format("%sError, field is blank.", ChatColor.RED));
                        return true;
                    }
                    if (length >= 1){
                        String warpLoc = args[0];
                        if(length > 1){
                            StringBuilder stringBuilder = new StringBuilder(50);
                                for(int i = 0; i < length-1; ++i){
                                    stringBuilder.append(args[i]);
                                    stringBuilder.append(" ");
                                }
                            stringBuilder.append(args[length-1]);
                            warpLoc = stringBuilder.toString();
                        }
                        if(warpHandling.checkWarp(warpLoc)){
                            warpHandling.delWarp(warpLoc);
                            playerSent.sendMessage(String.format("%sWarp " + warpLoc + " deleted.",ChatColor.GREEN));
                            return true;                        
                        }
                        playerSent.sendMessage(String.format("%sInvalid warp",ChatColor.GREEN));
                        return true;
                    }
                }
                playerSent.sendMessage(String.format("%sI'm sorry, but you do not have permission to perform this command. Please contact the server administrator if you believe that this is in error.", ChatColor.RED));
                return true;
            }
            sender.sendMessage("Error. You are not a player.");
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("delwarp")) {
            if (args.length == 1) {
                ArrayList<String> fill = new ArrayList<>();
                List<String> warps = warpHandling.getAllWarpNames();
                    if (!args[0].equals("")) {
                        for (String entry : warps) {
                            String tempEntry = entry;
                            if (tempEntry.toLowerCase().startsWith(args[0].toLowerCase())) {
                                fill.add(tempEntry);
                            }
                        }
                    } else {
                        for (String entry : warps) {
                            fill.add(entry);
                        }
                    }
                    return fill;
            }
            else{
                return new ArrayList<>();
            }
        }
        return null;
    }

}