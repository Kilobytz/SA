package io.github.kilobytz.sa.command;

import io.github.kilobytz.sa.entities.ShulkerBoss;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.Double.parseDouble;

public class SpawnShulkerBoss implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {


        if (command.getName().equalsIgnoreCase("spawnboss")) {
            if (sender.isOp()){
                int length = args.length;
                if(length != 3) {
                    return true;
                }
                List<Player> peeps = (List<Player>) Bukkit.getOnlinePlayers();
                UUID id = peeps.get(0).getUniqueId();
                ShulkerBoss boss = new ShulkerBoss(((CraftWorld) Bukkit.getPlayer(id).getWorld()).getHandle());
                boss.spawn(parseDouble(args[0]),parseDouble(args[1]),parseDouble(args[2]),((CraftWorld) Bukkit.getPlayer(id).getWorld()).getHandle());
                return true;
            }
            sender.sendMessage(String.format("%sI'm sorry, but you do not have permission to perform this command. Please contact the server administrator if you believe that this is in error.", ChatColor.RED));
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("spawnboss")) {
            List<String> fill = new ArrayList<>();
            if(args.length <= 3) {
                if(sender instanceof Player) {
                    Player playerSent = (Player) sender;
                    if(args.length == 1) {
                        fill.add(Double.toString(playerSent.getLocation().getX()));
                        return fill;
                    }
                    if(args.length == 2) {
                        fill.add(Double.toString(playerSent.getLocation().getY()));
                        return fill;
                    }
                    if(args.length == 3) {
                        fill.add(Double.toString(playerSent.getLocation().getZ()));
                        return fill;
                    }
                }
                return fill;
            }
        }
        return null;
    }
}
