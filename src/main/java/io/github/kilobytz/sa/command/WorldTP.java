package io.github.kilobytz.sa.command;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import io.github.kilobytz.sa.misc.WorldLoader;

public class WorldTP implements TabExecutor {

    WorldLoader wL;

    public void setup(WorldLoader wL) {this.wL = wL;}

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {

        if (command.getName().equalsIgnoreCase("worldtp")) {
            if (sender instanceof Player) {
                Player playerSent = (Player) sender;
                    int length = args.length;
                    if (length == 0) {
                        playerSent.sendMessage(ChatColor.RED + "Error. Please enter a world's name.");
                        return true;
                    }
                    if (length == 1) {
                            if(wL.loadWorld(args[0])){
                                playerSent.teleport(new Location(Bukkit.getWorld(args[0]), playerSent.getLocation().getX(), playerSent.getLocation().getY(), playerSent.getLocation().getZ()));
                                playerSent.sendMessage("Teleported to world "+ args[0]);
                                return true;
                            }
                            playerSent.sendMessage(ChatColor.RED + "Error. World name invalid.");
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
        if (command.getName().equalsIgnoreCase("worldtp")) {
            return new ArrayList<>();
        }
        return null;
    }
}