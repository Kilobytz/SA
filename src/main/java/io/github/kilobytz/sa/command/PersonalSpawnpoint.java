package io.github.kilobytz.sa.command;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class PersonalSpawnpoint implements TabExecutor{
    
    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {


        if (command.getName().equalsIgnoreCase("setpersonalspawnpoint")) {
            if (sender instanceof Player) {
                Player playerSent = (Player) sender;
                Location loc = playerSent.getPlayer().getLocation();
                playerSent.getPlayer().setBedSpawnLocation(loc, true);
                playerSent.getPlayer().sendMessage("Your spawnpoint has been set");
                return true;
            }
            sender.sendMessage("Error. You are not a player.");
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("setpersonalspawnpoint")) {
            return new ArrayList<>();
        }
        return null;
    }
}