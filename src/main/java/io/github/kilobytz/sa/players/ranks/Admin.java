package io.github.kilobytz.sa.players.ranks;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.kilobytz.sa.SA;

public class Admin extends Rank{

    @Override
    public String getName() {
        return "Admin";
    }

    @Override
    public ChatColor getColour() {
        return ChatColor.RED;
    }

    @Override
    public void setPerms(SA main, Player player) {
        setName(player);
        if(!player.isOp()){
            player.setOp(true);
        }
        player.addAttachment(main, "minecraft.command.op", false);
        player.addAttachment(main, "minecraft.command.deop",false);
        player.addAttachment(main, "minecraft.command.stop",false);
        player.addAttachment(main, "sa.admin",false);
    }
    
}
