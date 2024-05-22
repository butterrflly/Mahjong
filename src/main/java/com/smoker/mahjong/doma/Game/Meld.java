package com.smoker.mahjong.doma.Game;

import java.util.ArrayList;
import java.util.Comparator;

public class Meld {
    private ArrayList<Tile> meld;

    private String type;

    public Meld(ArrayList<Tile> meld, String type) {
        this.meld = meld;
        this.type = type;
    }


    public ArrayList<Tile> getMeld() {
        return meld;
    }


    public String getType() {
        return type;
    }


    public void sort(){
        meld.sort(Comparator.comparing(Tile :: getId));
    }
}
