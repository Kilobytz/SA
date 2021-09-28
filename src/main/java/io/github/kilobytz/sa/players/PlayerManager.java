package io.github.kilobytz.sa.players;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import io.github.kilobytz.sa.SA;
import io.github.kilobytz.sa.players.ranks.RankManager;

public class PlayerManager {

    SA main;
    List<String> ranks = new ArrayList<String>();
    HashMap<String, Boolean> perms = new HashMap<>();
    private HashMap<UUID,PracPlayer> players = new HashMap<>();

    
    public PlayerManager(SA main) {
        this.main = main;
    }

    public void setConfPerms(Player player) {
        //for(String names : perms.keySet()) {
        //    player.addAttachment(main,names,perms.get(names));
        //}
    }

    public void addPlayer(PracPlayer player) {
        players.put(player.getID(),player);
    }

    public void removePlayer(PracPlayer player) {
        players.remove(player.getID());
    }

    public boolean setRank(Player player, String rank) {
        if(rankNameValid(rank)) {
            if(getPlayerInst(player).hasRank()) {
                getPlayerInst(player).getRank().remove(main, player);
            }
            getPlayerInst(player).setRankInitial(main,rank);
            return true;
        }
        return false;
    }

    public boolean removeRank(Player player) {
        if(getPlayerInst(player).hasRank()) {
            getPlayerInst(player).getRank().remove(main, player);
            getPlayerInst(player).removeRank();
            return true;
        }
        return false;
    }

    public PracPlayer getPlayerInst(Player player) {
        return players.get(player.getUniqueId());
    }

    public boolean rankNameValid(String rank){
        rank = rank.substring(0,1).toUpperCase() + rank.substring(1).toLowerCase();
        if(RankManager.getRank(rank)!= null){
            return true;
        }
        return false;
    }

    

    public void savePlayers(java.sql.Statement statement,Player player) throws SQLException {
        if(getPlayerInst(player).hasRank()){
            statement.executeUpdate("INSERT INTO players (uuid, rank) VALUES ('"+ getPlayerInst(player).getID() +"', '"+ getPlayerInst(player).getRankName() +"') ON DUPLICATE KEY UPDATE rank = '"+ getPlayerInst(player).getRankName() +"';");
        }
        else{
            statement.executeUpdate("INSERT INTO players (uuid, rank) VALUES ('"+ getPlayerInst(player).getID() +"', NULL) ON DUPLICATE KEY UPDATE rank = NULL;");
        }
        statement.close();
    }

}
