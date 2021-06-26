package io.github.kilobytz.sa.players.ranks;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.kilobytz.sa.GlobalValues;
import io.github.kilobytz.sa.SA;

public class Donator extends Rank{
    
    @Override
    public String getName() {
        return GlobalValues.donatorName;
    }

    @Override
    public ChatColor getColour() {
        return GlobalValues.donatorColor;
    }

    @Override
    public void setPerms(SA main, Player player) {
        setName(player);
        player.addAttachment(main,"sa.donator",true);
    }
    
}
