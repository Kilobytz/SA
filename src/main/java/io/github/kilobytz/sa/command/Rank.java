package io.github.kilobytz.sa.command;

import io.github.kilobytz.sa.ranks.RankManager;
import io.github.kilobytz.sa.tips.TipManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rank implements TabExecutor {

    RankManager rM;
    HashMap<String,String> rankCommands = new HashMap<>();


    public void setRankData(RankManager rM) {
        this.rM=rM;
        setRankCommands();
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {


        if (command.getName().equalsIgnoreCase("rank")) {

                if(args.length < 2 || args.length > 3 ) {
                    sender.sendMessage("Invalid command." + ChatColor.RED);
                    printRankCommands(sender);
                    return true;
                }

                boolean nameValid = checkPlayerName(args[1]);
                boolean rankValid = false;

                if(args.length == 3) {
                    rankValid = checkRankName(args[2]);
                }


                switch (args[0]) {

                    case "remove" :
                        if(nameValid == false) {
                            sender.sendMessage("Invalid player name." + ChatColor.RED);
                            return true;
                        }
                        if(!rM.removeRanks(Bukkit.getPlayer(args[1].toLowerCase()))) {
                            sender.sendMessage("Error. Ranks not found." + ChatColor.RED);
                        }
                        else {
                            sender.sendMessage("Rank removed!." + ChatColor.GREEN);
                        }
                        return true;

                    case "add" :
                        if(rankValid == false) {
                            sender.sendMessage("Invalid rank name." + ChatColor.RED);
                            return true;
                        }
                        if(nameValid == false) {
                            sender.sendMessage("Invalid player name." + ChatColor.RED);
                            return true;
                        }
                        if(!rM.setRank(args[1].toLowerCase(),args[2].toLowerCase())) {
                            sender.sendMessage("Error. Player already has a rank." + ChatColor.RED);
                        }
                        else {
                            sender.sendMessage("Rank added!." + ChatColor.GREEN);
                        }
                        return true;

                    default:
                        sender.sendMessage("Error." + ChatColor.RED);
                        return true;
                }

        }
        return false;
    }
    public void setRankCommands() {
        rankCommands.put("add","/rank add <username> <rank>. Sets a user's rank.");
        rankCommands.put("remove","/rank remove <username>. Removes a user's rank.");
    }

    public void printRankCommands(CommandSender sender) {
        for (Map.Entry<String, String> entry : rankCommands.entrySet()) {
            sender.sendMessage(entry.getValue());
        }
    }

    public boolean checkRankName(String name) {
        for(String rankName : rM.getRanks()) {
            if(rankName.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkPlayerName(String name) {
        for(Player playerName : Bukkit.getOnlinePlayers()) {
            if(Bukkit.getPlayer(name) == playerName) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("rank")) {
            if (args.length == 1) {
                ArrayList<String> rankCom = new ArrayList<>();
                if (!args[0].equals("")) {
                    for (Map.Entry<String, String> entry : rankCommands.entrySet()) {
                        String tempEntry = entry.getKey();
                        if (tempEntry.toLowerCase().startsWith(args[0].toLowerCase())) {
                            rankCom.add(tempEntry);
                        }
                    }
                } else {
                    for (Map.Entry<String, String> entry : rankCommands.entrySet()) {
                        rankCom.add(entry.getKey());
                    }
                }
                return rankCom;
            }
        }
        return null;
    }
}