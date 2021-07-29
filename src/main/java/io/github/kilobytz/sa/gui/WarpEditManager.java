package io.github.kilobytz.sa.gui;

import java.util.HashMap;

public class WarpEditManager {


    HashMap<Integer,String> warps = new HashMap<>(); //

    WarpEditor wE;

    //make hashmap of the pages for warp pages here

    public WarpEditManager(){
        //make method to obtain GUI warp info from DB and save to warps
        wE = new WarpEditor(warps);
        //method for loading pages full of warps based on DB load
    }
}
