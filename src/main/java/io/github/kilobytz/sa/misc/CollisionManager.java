package io.github.kilobytz.sa.misc;

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
        if(board.getTeam("Player") == null) {
            Team collision = board.registerNewTeam("Player");
            collision.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        setTeamConfig();
        try {
            if (!board.getTeam("Player").getEntries().contains(event.getPlayer().getDisplayName())) {
                if(pM.getPlayerInst(event.getPlayer()).hasRank()) {
                    return;
                }
                board.getTeam("Player").addEntry(event.getPlayer().getDisplayName());
            }
        }catch (NullPointerException e) {
            Team collision = board.registerNewTeam("Player");
            collision.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            board.getTeam("Player").addEntry(event.getPlayer().getDisplayName());
        }
    }
}
