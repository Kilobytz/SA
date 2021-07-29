package io.github.kilobytz.sa.gui;

import java.util.HashMap;

import io.github.kilobytz.sa.SA;

public class WarpEditManager {


    HashMap<Integer,String> warps = new HashMap<>(); //

    SA main;
    WarpEditor wE;

    //make hashmap of the pages for warp pages here

    public WarpEditManager(SA main){
        this.main = main;
        //make method to obtain GUI warp info from DB and save to warps
        wE = new WarpEditor(warps);
        //method for loading pages full of warps based on DB load
    }
}
