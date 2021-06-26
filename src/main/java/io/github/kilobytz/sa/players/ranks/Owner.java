package io.github.kilobytz.sa.players.ranks;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.kilobytz.sa.GlobalValues;
import io.github.kilobytz.sa.SA;

public class Owner extends Rank{
    
    @Override
    public String getName() {
        return GlobalValues.ownerName;
    }

    @Override
    public ChatColor getColour() {
        return GlobalValues.ownerColor;
    }

    @Override
    public void setPerms(SA main, Player player) {
        setName(player);
        if(!player.isOp()){
            player.setOp(true);
        }
    }
    
}