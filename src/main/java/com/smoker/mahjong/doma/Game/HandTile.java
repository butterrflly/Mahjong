package com.smoker.mahjong.doma.Game;


import java.util.ArrayList;

public class HandTile {

    private ArrayList<Tile> handTile;

    private ArrayList<Meld> melds;


    public HandTile() {
        handTile = new ArrayList<Tile>();
        melds = new ArrayList<Meld>();
    }

    public void addTile(Tile tile){
        handTile.add(tile);
    }

    public void removeTile(Tile tile){}

    public void sort(){}

    public ArrayList<Tile> getHandTile(){
        return handTile;
    }
}
