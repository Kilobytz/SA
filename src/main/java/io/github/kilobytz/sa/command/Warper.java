package io.github.kilobytz.sa.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;

import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class Warper implements TabExecutor {


    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {

        if (command.getName().equalsIgnoreCase("warper")) {
            if (sender instanceof Player) {
                Player playerSent = (Player) sender;
                ItemStack warper = new ItemStack(Material.FIREWORK_CHARGE);
                net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(warper);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                tag.setInt("warper", 0);
                nmsItem.setTag(tag);
                warper = CraftItemStack.asCraftMirror(nmsItem);
                FireworkEffectMeta fireworkMeta = (FireworkEffectMeta)warper.getItemMeta();
                fireworkMeta.setDisplayName("Warper");
                List<String> loreList = new ArrayList<>();
                loreList.add("Teleports the player to warps.");
                fireworkMeta.setLore(loreList);
                FireworkEffect eff = FireworkEffect.builder().withColor(Color.WHITE).build();
                fireworkMeta.setEffect(eff); 
                warper.setItemMeta(fireworkMeta);
                playerSent.getInventory().addItem(warper);
                
                return true;
            }
            sender.sendMessage("Error. You are not a player.");
            return true;
        }
        return false;
    }
    

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("warper")) {
            if (args.length >= 0) {
                return new ArrayList<>();
            }
        }
        return null;
    }
}
