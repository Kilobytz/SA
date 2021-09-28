package io.github.kilobytz.sa.misc;

import org.bukkit.event.Listener;
import io.github.kilobytz.sa.SA;
import io.github.kilobytz.sa.warping.WarpHandling;

public class WorldListener implements Listener {
    
    WarpHandling wH;
    SA main;
    public void setInfo(WarpHandling wH, SA main) {
      this.wH = wH;
      this.main = main;
    }
}

