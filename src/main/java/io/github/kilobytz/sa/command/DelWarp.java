package io.github.kilobytz.sa.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import io.github.warping.WarpHandling;

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
                if (playerSent.isOp()) {
                    int length = args.length;
                    if (length == 0) {
                        playerSent.sendMessage(String.format("%sError, field is blank.", ChatColor.RED));
                        return true;
                    }
                    if (length == 1){
                        if(warpHandling.checkWarp(args[0])) {
                            warpHandling.delWarp(args[0]);
                            playerSent.sendMessage(String.format("%sWarp " + args[0] + " deleted.",ChatColor.GREEN));
                            return true;
                        }
                        playerSent.sendMessage(String.format("%sInvalid warp",ChatColor.GREEN));
                        return true;
                    }
                    playerSent.sendMessage(String.format("%sError, invalid character.", ChatColor.RED));
                    return true;
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
        }
        return null;
    }

}