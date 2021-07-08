package io.github.kilobytz.sa.command;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class OpenEnderChest implements TabExecutor{

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {


        if (command.getName().equalsIgnoreCase("enderchest")) {
            if (sender instanceof Player) {
                Player playerSent = (Player) sender;
                playerSent.openInventory(playerSent.getEnderChest());
                return true;
            }
            sender.sendMessage("Error. You are not a player.");
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("enderchest")) {
            return new ArrayList<>();
        }
        return null;
    }
}