package io.github.kilobytz.sa.commandfunctions;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class CollisionManager implements Listener {

    ScoreboardManager manager;
    Scoreboard board;

    public void setTeamConfig() {
        this.manager = Bukkit.getScoreboardManager();
        this.board = manager.getMainScoreboard();
        if(board.getTeam("collision") == null) {
            Team collision = board.registerNewTeam("collision");
            collision.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.FOR_OWN_TEAM);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            if (!board.getTeam("collision").getEntries().contains(event.getPlayer().getDisplayName())) {
                board.getTeam("collision").addEntry(event.getPlayer().getDisplayName());
            }
        }catch (NullPointerException e) {
            Team collision = board.registerNewTeam("collision");
            collision.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            board.getTeam("collision").addEntry(event.getPlayer().getDisplayName());
        }
    }
}
