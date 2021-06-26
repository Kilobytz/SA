package io.github.kilobytz.sa.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import io.github.kilobytz.sa.players.PlayerListener;

public class Muteall implements TabExecutor {

    PlayerListener pL;

    public void setup(PlayerListener pL) {this.pL = pL;}

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {
        if (command.getName().equalsIgnoreCase("muteall")) {
            if(args.length > 0) {
                sender.sendMessage("Error. You don't need more fucking words than that.");
                return true;
            }
            pL.toggleMute();
            sender.sendMessage("Muted: " + Boolean.toString(pL.getMuteStatus()));
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("muteall")) {
            return new ArrayList<>();
        }
        return null;
    }

}