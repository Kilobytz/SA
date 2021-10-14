package io.github.kilobytz.sa.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import io.github.kilobytz.sa.players.NoInteracting;

import java.util.ArrayList;
import java.util.List;

public class PvPToggle implements TabExecutor {

    NoInteracting nI;

    public void setup(NoInteracting nI) {this.nI = nI;}

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {


        if (command.getName().equalsIgnoreCase("pvptoggle")) {
            if(args.length > 0) {
                sender.sendMessage("Error. You don't need more fucking words than that.");
                return true;
            }
            nI.togglePvP();
            sender.sendMessage("PvP enabled: " + Boolean.toString(nI.getPvPStatus()));
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("pvptoggle")) {
            return new ArrayList<>();
        }
        return null;
    }
}
