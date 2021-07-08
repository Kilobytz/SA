package io.github.kilobytz.sa.players.ranks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import io.github.kilobytz.sa.SA;

public abstract class Rank {
    

    ChatColor rankColor;
    String rankName;

    public String getName() {
        return rankName;
    }

    public ChatColor getColour() {
        return rankColor;
    }

    public void setPerms(SA main, Player player) {
        player.sendMessage("rank class invalid");
        return;
    }

    public void setName(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard mainBoard = manager.getMainScoreboard();
        if(mainBoard.getTeam(getName()) == null) {
            Team rankTeam = mainBoard.registerNewTeam(getName());
            rankTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            rankTeam.setPrefix(getColour() + "[" + getName()  + "] " + ChatColor.WHITE);
        }
        if (!mainBoard.getTeam(getName()).getEntries().contains(player.getDisplayName())) {
            mainBoard.getTeam(getName()).addEntry(player.getDisplayName());
        }
    }
    

    public void remove(SA main,Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
            Scoreboard mainBoard = manager.getMainScoreboard();
            mainBoard.getTeam(getName()).removeEntry(player.getDisplayName());
            mainBoard.getTeam("collision").addEntry(player.getDisplayName());
            if(player.isOp()) {
                player.setOp(false);
            }
            for (Object perm : player.getEffectivePermissions()) {
                PermissionAttachmentInfo pai = (PermissionAttachmentInfo) perm;
                if(pai.getAttachment() != null) {
                    player.removeAttachment(pai.getAttachment());
                }
            }
            player.addAttachment(main, "minecraft.command.help", true);
            player.addAttachment(main, "minecraft.command.msg", true);
            player.addAttachment(main, "minecraft.command.me", true);
            player.addAttachment(main, "bukkit.command.version", true);
            player.addAttachment(main, "bukkit.command.help", true);
            player.addAttachment(main, "bukkit.command.plugins", true);
            player.addAttachment(main, "sa.general", true);
    }
    

}
