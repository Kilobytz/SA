package io.github.kilobytz.sa.command;

import io.github.kilobytz.sa.commandfunctions.WarpHandling;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Warp implements TabExecutor {

    WarpHandling warpHandling;

    public void setup(WarpHandling warpHandling) {this.warpHandling = warpHandling;}

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {

        if (command.getName().equalsIgnoreCase("warp")) {
            if (sender instanceof Player) {
                Player playerSent = (Player) sender;
                    int length = args.length;
                    if (length == 0) {
                        List<String> allWarps = warpHandling.getAllWarps();
                        if(allWarps.size() != 0) {
                            String warpString = allWarps.get(0);
                            if(allWarps.size() > 1) {
                                String symbol = ", ";
                                for (String warps : allWarps) {
                                    warpString = warpString + (symbol + warps);
                                }
                            }
                            playerSent.sendMessage(warpString);
                            return true;
                        }
                        else{
                            playerSent.sendMessage(String.format("%sError. There are no warps.",ChatColor.RED));
                            return true;
                        }
                    }
                    if (length == 1) {
                        if (warpHandling.checkWarp(args[0])) {
                            warpHandling.warpPlayer(playerSent,args[0]);
                            return true;
                        }
                        playerSent.sendMessage(String.format("%sError. Warp does not exist.",ChatColor.RED));
                        return true;
                    }
                    playerSent.sendMessage(String.format("%sError. Invalid character.",ChatColor.RED));
                    return true;
            }
            sender.sendMessage("Error. You are not a player.");
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("warp")) {
            if (args.length == 1) {
                ArrayList<String> fill = new ArrayList<>();
                List<String> warps = warpHandling.getAllWarps();
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
        }
        return null;
    }
}