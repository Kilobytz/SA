package io.github.kilobytz.sa.misc;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

import io.github.kilobytz.sa.SA;

public class WorldLoader {
    SA main;
    public boolean loadWorld(String name) {
        try{
            if (Bukkit.getWorld(name) != null) {
                return true;
            }
            WorldCreator wc = new WorldCreator(name);
            Bukkit.getServer().createWorld(wc);
            return true;
        }catch(Exception e) {
            return false;
        }
        
    }

}
