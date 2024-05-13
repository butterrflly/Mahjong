package com.smoker.mahjong.doma.Game;


import java.util.ArrayList;
import java.util.Comparator;

public class HandTile {

    private ArrayList<Tile> handTile;

    private ArrayList<Meld> melds;


    public HandTile() {
        handTile = new ArrayList<Tile>();
        melds = new ArrayList<Meld>();
    }

    public Tile addTile(Tile tile){
        handTile.add(tile);
        sort();
        return tile;
    }

    public void removeTile(Tile tile){
        handTile.remove(tile);
        sort();
    }

    public void sort(){
        handTile.sort(Comparator.comparing(Tile :: getId));
    }

    public ArrayList<Tile> getHandTile(){
        sort();
        return handTile;
    }
}
