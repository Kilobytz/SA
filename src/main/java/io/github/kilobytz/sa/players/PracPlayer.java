package io.github.kilobytz.sa.players;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.github.kilobytz.sa.GlobalValues;
import io.github.kilobytz.sa.SA;
import io.github.kilobytz.sa.players.ranks.Admin;
import io.github.kilobytz.sa.players.ranks.Builder;
import io.github.kilobytz.sa.players.ranks.Owner;
import io.github.kilobytz.sa.players.ranks.Rank;

public class PracPlayer {
    
    private final UUID playerID;
    private Rank rank;

    public PracPlayer(UUID playerID, String rank) {
        this.playerID = playerID;
        setRank(rank);
    }

    public UUID getID() {
        return playerID;
    }

    public void setRankInitial(SA main,String rank) {
        rank = rank.substring(0,1).toUpperCase() + rank.substring(1).toLowerCase();
        setRank(rank);
        if(this.rank != null) {
            this.rank.setPerms(main,Bukkit.getPlayer(playerID));
        }
    } 

    public void setRank(String rank) {
        switch(rank) {
            case GlobalValues.builderName :
                this.rank = new Builder();;
                return;
            case GlobalValues.adminName :
                this.rank = new Admin();
                return;
            case GlobalValues.ownerName :
                this.rank = new Owner();
                return;
            default :
            this.rank = null;
        }
    }

    public Rank getRank() {
        return rank;
    }

    public String getRankName(){
        if(rank != null) {
            return rank.getName();
        }
        return "NULL";
    }

    public boolean equals(Player player) {
        if(player.getUniqueId() == playerID) {
            return true;
        }
        return false;
    }
    public boolean equals(UUID id) {
        if(id == playerID) {
            return true;
        }
        return false;
    }
    
    public boolean hasRank() {
        if(rank == null) {
            return false;
        }
        return true;
    }
}
