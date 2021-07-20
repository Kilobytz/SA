package io.github.kilobytz.sa.misc;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import io.github.kilobytz.sa.SA;

public class WorldLoader {

    SA main;
    
    public WorldLoader(SA main){
        this.main = main;
    }
    public boolean loadWorld(String name) {
        File file = new File(main.getServer().getWorldContainer(), name);
        if (file.exists()) {
            if(main.getServer().getWorld(name) != null) {
                return true;
            }
            WorldCreator wc = new WorldCreator(name);
            Bukkit.getServer().createWorld(wc);
            return true;
        }
        return false;
        
    }

}
