package io.github.kilobytz.sa.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Spawn implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {


        if (command.getName().equalsIgnoreCase("spawn")) {
            if (sender instanceof Player) {
                Player playerSent = (Player) sender;
                Location spawn = playerSent.getWorld().getSpawnLocation();
                playerSent.teleport(spawn);
                return true;
            }
            sender.sendMessage("Error. You are not a player.");
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("spawn")) {
            return new ArrayList<>();
        }
        return null;
    }
}