package com.smoker.mahjong.doma.Game;

import java.util.ArrayList;
import java.util.Comparator;

public class Meld implements hide {
    private ArrayList<Tile> meld;

    private String type;

    private boolean isHide;

    public Meld(ArrayList<Tile> meld, String type, boolean isHide) {
        this.meld = meld;
        this.type = type;
        this.isHide = isHide;
    }


    public ArrayList<Tile> getMeld() {
        return meld;
    }

    public int[] getMeldArray() {
        int[] meldArray = new int[meld.size()];
        for (int i = 0; i < meld.size(); i++) {
            meldArray[i] = meld.get(i).getId();
        }
        return meldArray;
    }

    public void open() {
        isHide = false;
    }

    public boolean getHide() {
        return isHide;
    }



    public String getType() {
        return type;
    }


    public void sort(){
        meld.sort(Comparator.comparing(Tile :: getId));
    }
}
