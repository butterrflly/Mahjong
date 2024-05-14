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

    public void removeTile(ArrayList<Tile> tiles){
        for (Tile tile : tiles)
            handTile.remove(tile);
    }


    public boolean canPang(int tileID) {
        int num = 0;
        for (Tile tile : handTile)
            if (tile.getId() / 10 == tileID / 10)
                num ++;


        return num >= 2;
    }


    public boolean canKong(int tileID) {
        int numHand = 0;
        int numMelds = 0;

        for (Tile tile : handTile) {
            if (tile.getId() / 10 == tileID / 10) {
                numHand++;
            }
        }

        for (Meld meld : melds) {
            int count = 0;
            for (Tile tile : meld.getMeld()) {
                if (tile.getId() / 10 == tileID / 10) {
                    count++;
                }
            }
            if (count == 3) {
                numMelds++;
            }
        }

        return numHand >= 3 || numMelds > 0;
    }


    public ArrayList<Tile> canKong(){
        ArrayList<Tile> kongTiles = new ArrayList<>();
        int id = 0;
        for (Tile tile : handTile){
            if (id == tile.getId() / 10)
                continue;
            if (canKong(tile.getId())){
                kongTiles.add(tile);
                id = tile.getId() / 10;
            }
        }
        return kongTiles;
    }


    public ArrayList<ArrayList<Tile>> canChow(Tile tile) {
        ArrayList<ArrayList<Tile>> chowTiles = new ArrayList<>();
        int num = tile.getId() / 10;

        if (num / 10 > 3) return null;

        ArrayList<Tile> tiles = new ArrayList<>();
        for (Tile value : handTile) {
            if (value.getId() / 10 == num){
                tiles.add(value);
            }
        }

        if (tiles.size() < 2) return null;

        Tile tile1 = inArrayList(tiles, num - 2);
        Tile tile2 = inArrayList(tiles, num - 1);
        Tile tile3 = inArrayList(tiles, num + 1);
        Tile tile4 = inArrayList(tiles, num + 2);


        if (tile1 != null && tile2 != null){
            ArrayList<Tile> c = new ArrayList<>();
            c.add(tile1);
            c.add(tile2);
            c.add(tile);
            chowTiles.add(c);
        } else if (tile2 != null && tile3 != null){
            ArrayList<Tile> c = new ArrayList<>();
            c.add(tile2);
            c.add(tile);
            c.add(tile3);
            chowTiles.add(c);
        } else if (tile3 != null && tile4 != null){
            ArrayList<Tile> c = new ArrayList<>();
            c.add(tile);
            c.add(tile3);
            c.add(tile4);
            chowTiles.add(c);
        }

        if (chowTiles.size() == 0) return null;

        return chowTiles;
    }

    private Tile inArrayList(ArrayList<Tile> tiles, int id) {
        for (Tile value : tiles) {
            if (value.getId() == id) {
                return value;
            }
        }
        return null;
    }


    public boolean canHu(Tile tile) {


        return true;
    }


    public void Pang(Tile tile) {
        // 碰牌
        ArrayList<Tile> meld = new ArrayList<>();

        for (Tile value : handTile) {
            if (tile.getId() / 10 == value.getId() / 10 && meld.size() < 2) {
                meld.add(value);
            }
        }

        removeTile(meld);
        meld.add(tile);
        melds.add(new Meld(meld, "pang"));
    }

    public void Kong(Tile tile) {
        // 杠牌
        ArrayList<Tile> meld = new ArrayList<>();

        for (Tile value : handTile) {
            if (tile.getId() / 10 == value.getId() / 10) {
                meld.add(value);
            }
        }

        if (meld.size() >= 3) {
            removeTile(meld);
            if (meld.size() == 3) meld.add(tile);
            melds.add(new Meld(meld, "pang"));
        } else {
            for (int i = 0; i < melds.size(); i++) {
                Meld m = melds.get(i);
                int num = 0;
                for (Tile value : m.getMeld()){
                    if  (tile.getId() / 10 == value.getId() / 10){
                        num++;
                    }
                }
                if (num == 3){
                    melds.remove(m);
                    meld.addAll(m.getMeld());
                    meld.add(tile);
                    melds.add(new Meld(meld, "pang"));
                    break;
                }
            }
        }
    }


    public void Chow(ArrayList<Tile> tiles, Tile tile) {
        // 吃牌
        tiles.remove(tile);
        Tile tile1 = tiles.get(0);
        Tile tile2 = tiles.get(1);
        handTile.remove(tile1);
        handTile.remove(tile2);
        tiles.add(tile1);
        tiles.add(tile);
        tiles.add(tile2);
        melds.add(new Meld(tiles, "chow"));
    }


    public void Hu(Tile tile) {
        // 胡牌
    }



    public void sort(){
        handTile.sort(Comparator.comparing(Tile :: getId));
    }

    public ArrayList<Tile> getHandTile(){
        sort();
        return handTile;
    }

    public ArrayList<Meld> getMelds(){
        return melds;
    }
}
