package io.github.kilobytz.sa.players.ranks;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.kilobytz.sa.SA;

public class Builder extends Rank{

    @Override
    public String getName() {
        return "Builder";
    }

    @Override
    public ChatColor getColour() {
        return ChatColor.GREEN;
    }

    @Override
    public void setPerms(SA main, Player player) {
        setName(player);
        player.addAttachment(main,"minecraft.command.gamemode",true);
        player.addAttachment(main,"minecraft.command.tp",true);
        player.addAttachment(main,"sa.warp",true);
    }   
}
