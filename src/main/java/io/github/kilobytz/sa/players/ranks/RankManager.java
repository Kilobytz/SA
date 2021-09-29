package io.github.kilobytz.sa.players.ranks;
import java.util.HashMap;

public class RankManager {

    private static HashMap<String,Rank> allRanks = new HashMap<>();

    static{
        allRanks.put("Avenger", new Avenger());
        allRanks.put("Builder", new Builder());
        allRanks.put("Admin", new Admin());
        allRanks.put("Owner", new Owner());
    }

    public static HashMap<String,Rank> getAllRanks(){
        return allRanks;
    }
    public static Rank getRank(String rankName){
        return allRanks.get(rankName);
    }
}
