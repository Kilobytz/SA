package io.github.kilobytz.sa.command;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Boom implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {


        if (command.getName().equalsIgnoreCase("boom")) {
            int length = args.length;
            if(length == 0) {
                sender.sendMessage(String.format("%sError. Invalid.", ChatColor.RED));
                return true;
            }
            if(length == 1) {
                if(sender instanceof Player) {
                    if(sender.isOp()) {
                        if (args[0].equalsIgnoreCase("normal")) {
                            Location loc = ((Player) sender).getLocation();
                            loc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 50);
                            loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                            return true;
                        }
                        if (args[0].equalsIgnoreCase("huge")) {
                            Location loc = ((Player) sender).getLocation();
                            loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 10);
                            loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                            return true;
                        }
                        if (args[0].equalsIgnoreCase("large")) {
                            Location loc = ((Player) sender).getLocation();
                            loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 5);
                            loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                            return true;
                        }
                    }
                }
            }
            if(length == 4) {
                List<Player> pl = (List<Player>) Bukkit.getOnlinePlayers();
                World wrld = pl.get(0).getWorld();
                Location loc = new Location(wrld, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
                if (args[0].equalsIgnoreCase("normal")) {
                    wrld.spawnParticle(Particle.EXPLOSION_NORMAL, loc, 50);
                    wrld.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                    return true;
                }
                if (args[0].equalsIgnoreCase("huge")) {
                    wrld.spawnParticle(Particle.EXPLOSION_HUGE, loc, 10);
                    wrld.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                    return true;
                }
                if(args[0].equalsIgnoreCase("large")) {
                    wrld.spawnParticle(Particle.EXPLOSION_LARGE,loc,5);
                    wrld.playSound(loc,Sound.ENTITY_GENERIC_EXPLODE,1,1);
                    return true;
                }
            }
            else{
                sender.sendMessage(String.format("%sError. Invalid.", ChatColor.RED));
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("boom")) {
            List<String> fill = new ArrayList<>();
            List<String> sizes = new ArrayList<>();
            sizes.add("normal");
            sizes.add("large");
            sizes.add("huge");
            if(args.length <= 4) {
                if(sender instanceof Player) {
                    Player playerSent = (Player) sender;
                    if(args.length == 1) {                    if (!args[0].equals("")) {
                        for (String entry : sizes) {
                            if (entry.toLowerCase().startsWith(args[0].toLowerCase())) {
                                fill.add(entry);
                            }
                        }
                    } else {
                        fill.addAll(sizes);
                    }
                        return fill;
                    }
                    if(args.length == 2) {
                        fill.add(Double.toString(playerSent.getLocation().getX()));
                        return fill;
                    }
                    if(args.length == 3) {
                        fill.add(Double.toString(playerSent.getLocation().getY()));
                        return fill;
                    }
                    if(args.length == 4) {
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