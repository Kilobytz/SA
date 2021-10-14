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
                if (length > 1){
                    sender.sendMessage("Error. Invalid input.");
                    return true;
                }
                if(warpHandling.checkWarp(args[0])){
                    warpHandling.delWarp(args[0]);
                }
                warpHandling.setWarp(args[0], playerSent.getLocation());
                playerSent.sendMessage(String.format("%sWarp " + args[0] + " set.",ChatColor.GREEN));
                return true;                        
                
            }
            sender.sendMessage("Error. You are not a player.");
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("setwarp")) {
            return new ArrayList<>();
        }
        return null;
    }

    }
