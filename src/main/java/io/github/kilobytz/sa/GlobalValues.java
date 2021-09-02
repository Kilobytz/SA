package io.github.kilobytz.sa;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;

public class GlobalValues {

    public static final String donatorName = "Member";
    public static final String builderName = "Builder";
    public static final String adminName = "Admin";
    public static final String ownerName = "Owner";
    public static final String player = "Player";

    public static List<String> allRanks = new LinkedList<>();
    
    public static ChatColor donatorColor = ChatColor.YELLOW;
    public static ChatColor builderColor = ChatColor.GREEN;
    public static ChatColor adminColor = ChatColor.RED;
    public static ChatColor ownerColor = ChatColor.BLUE;

    public GlobalValues(){
        allRanks.add(donatorName);
        allRanks.add(builderName);
        allRanks.add(adminName);
        allRanks.add(ownerName);
        allRanks.add(player);
    }

    public List<String> getAllRanks(){
        return allRanks;
    }
}
