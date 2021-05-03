package io.github.kilobytz.sa.commandfunctions;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import io.github.kilobytz.sa.players.PlayerManager;

public class CollisionManager implements Listener {

    ScoreboardManager manager;
    Scoreboard board;
    PlayerManager pM;

    public CollisionManager(PlayerManager pM) {
        this.pM = pM;
    }
    

    public void setTeamConfig() {
        this.manager = Bukkit.getScoreboardManager();
        this.board = manager.getMainScoreboard();
        if(board.getTeam("collision") == null) {
            Team collision = board.registerNewTeam("collision");
            collision.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        setTeamConfig();
        try {
            if (!board.getTeam("collision").getEntries().contains(event.getPlayer().getDisplayName())) {
                if(pM.getPlayerInst(event.getPlayer()).hasRank()) {
                    return;
                }
                board.getTeam("collision").addEntry(event.getPlayer().getDisplayName());
            }
        }catch (NullPointerException e) {
            Team collision = board.registerNewTeam("collision");
            collision.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            board.getTeam("collision").addEntry(event.getPlayer().getDisplayName());
        }
    }
}
