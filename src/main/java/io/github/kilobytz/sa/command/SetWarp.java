package io.github.kilobytz.sa.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import io.github.kilobytz.sa.warping.WarpHandling;

import java.util.ArrayList;
import java.util.List;

public class SetWarp implements TabExecutor {

    WarpHandling warpHandling;

    public void setup(WarpHandling warpHandling) {this.warpHandling = warpHandling;}

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {

        if (command.getName().equalsIgnoreCase("setwarp")) {
            if (sender instanceof Player) {
                Player playerSent = (Player) sender;
                int length = args.length;
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
                    }
                    warpHandling.setWarp(warpLoc, playerSent.getLocation());
                    playerSent.sendMessage(String.format("%sWarp " + warpLoc + " set.",ChatColor.GREEN));
                    return true;                        
                }
            }
            sender.sendMessage("Error. You are not a player.");
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("setwarp")) {
            if (args.length == 1) {
                ArrayList<String> fill = new ArrayList<>();
                List<String> warps = warpHandling.getAllWarpNames();
                if (sender.isOp()) {
                    if (!args[0].equals("")) {
                        for (String entry : warps) {
                            if (entry.toLowerCase().startsWith(args[0].toLowerCase())) {
                                fill.add(entry);
                            }
                        }
                    } else {
                        fill.addAll(warps);
                    }
                    return fill;
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
