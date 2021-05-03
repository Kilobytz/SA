package io.github.kilobytz.sa.players;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.mysql.jdbc.Statement;

import org.bukkit.entity.Player;
import io.github.kilobytz.sa.GlobalValues;
import io.github.kilobytz.sa.SA;

public class PlayerManager {

    SA main;
    List<String> ranks = new ArrayList<String>();
    HashMap<String, Boolean> perms = new HashMap<>();
    private LinkedList<PracPlayer> players = new LinkedList<>();

    
    public PlayerManager(SA main) {
        this.main = main;
    }

    public void setConfPerms(Player player) {
        //for(String names : perms.keySet()) {
        //    player.addAttachment(main,names,perms.get(names));
        //}
        player.addAttachment(main, "ei.item.*", true);
    }

    public void addPlayer(PracPlayer player) {
        players.add(player);
    }



    public boolean setRank(Player player, String rank) {
        if(rankNameValid(rank)) {
            getPlayerInst(player).setRankInitial(main,rank);
            return true;
        }
        return false;
    }

    public boolean removeRank(Player player) {
        if(getPlayerInst(player).hasRank()) {
            getPlayerInst(player).getRank().remove(main, player);
            return true;
        }
        return false;
    }

    public PracPlayer getPlayerInst(Player player) {
        for(PracPlayer playerLoop : players) {
            if(playerLoop.equals(player)) {
                return playerLoop;
            }
        }
        return null;
    }

    public boolean rankNameValid(String rank){
        rank = rank.substring(0,1).toUpperCase() + rank.substring(1).toLowerCase();
        switch(rank) {
            case GlobalValues.builderName :
            case GlobalValues.adminName :
            case GlobalValues.ownerName :
                return true;
            default :
            return false;
        }
    }
    public String getPlayerRank(Player player) {
        try {
            String ID = player.getUniqueId().toString();
            for (String key : main.getConfig().getConfigurationSection("users").getKeys(false)) {
                if (key.equalsIgnoreCase(ID)) {
                    if(!(main.getConfig().get("users." + ID) == null)) {
                        return (String) main.getConfig().get("users." + key); 
                    }

                }
            }
        }catch(NullPointerException e) {
            return null;
        }
        return null;
    }

    

    public void savePlayers(java.sql.Statement statement,Player player) throws SQLException {
        if(getPlayerInst(player).hasRank()){
            statement.executeUpdate("INSERT INTO players (uuid, rank) VALUES ('"+ getPlayerInst(player).getID() +"', '"+ getPlayerInst(player).getRankName() +"') ON DUPLICATE KEY UPDATE rank = '"+ getPlayerInst(player).getRankName() +"';");
        }
        else{
            statement.executeUpdate("INSERT INTO players (uuid, rank) VALUES ('"+ getPlayerInst(player).getID() +"', NULL) ON DUPLICATE KEY UPDATE rank = NULL;");
        }
    }

}
