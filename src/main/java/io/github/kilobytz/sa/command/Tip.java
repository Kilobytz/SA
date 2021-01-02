package io.github.kilobytz.sa.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;

import io.github.kilobytz.sa.tips.TipManager;

public class Tip implements TabExecutor {

    TipManager tM;
    HashMap<String,String> tipCommands = new HashMap<>();


    public void setTipData(TipManager tM) {
        this.tM=tM;
        setTipCommands();
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {


        if (command.getName().equalsIgnoreCase("tip")) {
            if (sender instanceof ConsoleCommandSender) {

                if(args.length == 0) {
                    sender.sendMessage("Invalid command.");
                    printTipCommands(sender);
                    return true;
                }

                switch (args[0]) {
                    case "add" :
                        if(args.length == 1) {
                            sender.sendMessage(ChatColor.RED + "Invalid usage. /tip add <tip-message>");
                            return true;
                        }
                        String tipMessage = args[1];
                        for(int i = 0; i < args.length; ++i) {
                            if(i > 1) {
                                tipMessage += " " + args[i];
                            }
                        }
                        tM.setTip(tipMessage);
                        sender.sendMessage(ChatColor.GREEN + "Tip added!");
                        return true;
                    case "remove" :
                        if(args.length == 1) {
                            sender.sendMessage(ChatColor.RED + "Invalid usage. /tip add <tip-message>");
                            return true;
                        }
                        try{
                            tM.delTip(Integer.parseInt(args[1]));
                            sender.sendMessage(ChatColor.GREEN + "Tip deleted!");
                            return true;
                        }catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid usage. /tip remove <tip-number>");
                            return true;
                        }
                    case "list" :
                        if(args.length > 1) {
                            sender.sendMessage(ChatColor.RED + "Invalid usage. /tip list");
                            return true;
                        }
                        tM.listTips(sender);
                        return true;
                    default:
                        sender.sendMessage("Invalid command.");
                        printTipCommands(sender);
                        return true;
                }

            }
            sender.sendMessage(ChatColor.RED + "Error. Only console access.");
            return true;
        }
        return false;
    }
    public void setTipCommands() {
        tipCommands.put("add","/tip add <tip>. Adds a new tip.");
        tipCommands.put("remove","/tip remove <tip number>. Removes a tip, use a tip's ID number to remove");
        tipCommands.put("list","/tip list. Lists all currently added tips.");
    }

    public void printTipCommands(CommandSender sender) {
        for (Map.Entry<String, String> entry : tipCommands.entrySet()) {
            sender.sendMessage(entry.getValue());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("tip")) {
            if (args.length == 1) {
                ArrayList<String> tipCom = new ArrayList<>();
                if (!args[0].equals("")) {
                    for (Map.Entry<String, String> entry : tipCommands.entrySet()) {
                        String tempEntry = entry.getKey();
                        if (tempEntry.toLowerCase().startsWith(args[0].toLowerCase())) {
                            tipCom.add(tempEntry);
                        }
                    }
                } else {
                    for (Map.Entry<String, String> entry : tipCommands.entrySet()) {
                        tipCom.add(entry.getKey());
                    }
                }
                return tipCom;
            }
        }
        return null;
    }
}