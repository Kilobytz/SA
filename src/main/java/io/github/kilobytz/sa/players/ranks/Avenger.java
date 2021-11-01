package io.github.kilobytz.sa.players.ranks;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.kilobytz.sa.SA;

public class Avenger extends Rank{
    
    @Override
    public String getName() {
        return "Avenger";
    }

    @Override
    public ChatColor getColour() {
        return ChatColor.DARK_PURPLE;
    }

    @Override
    public void setPerms(SA main, Player player) {
        setName(player);
        player.addAttachment(main,"minecraft.command.gamemode",true);
        player.addAttachment(main,"minecraft.command.tp",true);
        player.addAttachment(main,"minecraft.command.enchant",true);
        player.addAttachment(main,"minecraft.command.give",true);
        player.addAttachment(main,"minecraft.command.xp",true);
    }
    
}
