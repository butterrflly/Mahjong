package com.smoker.mahjong.doma.Game;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents the hand tiles of a player and provides various methods
 * to manipulate and check the tiles for actions like Pong, Kong, Chow, and Hu.
 */
public class HandTile {

    private ArrayList<Tile> handTile; // List of tiles in hand
    private ArrayList<Meld> melds; // List of melds (Pong, Kong, Chow)

    public HandTile() {
        handTile = new ArrayList<Tile>();
        melds = new ArrayList<Meld>();
    }

    /**
     * Adds a tile to the hand and sorts the hand.
     * @param tile the tile to add
     * @return the added tile
     */
    public Tile addTile(Tile tile){
        handTile.add(tile);
        sort(); // Sort the hand after adding the tile
        return tile;
    }

    /**
     * Removes a tile from the hand and sorts the hand.
     * @param tile the tile to remove
     * @return the ID of the removed tile
     */
    public int removeTile(Tile tile){
        handTile.remove(tile);
        sort(); // Sort the hand after removing the tile
        return tile.getId();
    }

    /**
     * Clears all tiles from the hand.
     */
    public void cleanTile(){
        handTile.clear();
    }

    /**
     * Removes multiple tiles from the hand.
     * @param tiles the list of tiles to remove
     */
    public void removeTile(ArrayList<Tile> tiles){
        for (Tile tile : tiles)
            handTile.remove(tile);
    }

    /**
     * Checks if a tile can be used to form a Pong.
     * @param tileID the ID of the tile to check
     * @return true if Pong can be formed, false otherwise
     */
    public boolean canPang(int tileID) {
        int num = 0;
        for (Tile tile : handTile)
            if (tile.getId() / 10 == tileID / 10)
                num ++;

        return num >= 2;
    }

    /**
     * Checks if a tile can be used to form a Kong.
     * @param tileID the ID of the tile to check
     * @return true if Kong can be formed, false otherwise
     */
    public boolean canKong(int tileID) {
        int numHand = 0;
        int numMelds = 0;
        boolean contain = false;

        for (Tile tile : handTile) {
            if (tile.getId() == tileID) {
                contain = true;
                continue;
            }
            if (tile.getId() / 10 == tileID / 10) {
                numHand++;
            }
        }

        if (contain){
            return numHand >= 3;
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

    /**
     * Checks if there are any tiles that can form a Kong.
     * @return a list of tiles that can form a Kong
     */
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

    /**
     * Checks if a tile can be used to form a Chow.
     * @param tile the tile to check
     * @return a list of possible Chow combinations
     */
    public ArrayList<ArrayList<Tile>> canChow(Tile tile) {
        ArrayList<ArrayList<Tile>> chowTiles = new ArrayList<>();
        int num = tile.getId() / 10;

        if (num / 10 > 3) return chowTiles;

        ArrayList<Tile> tiles = new ArrayList<>();
        for (Tile value : handTile) {
            if (value.getId() / 100 == num / 10){
                tiles.add(value);
            }
        }

        if (tiles.size() < 2) return chowTiles;

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
        }
        if (tile2 != null && tile3 != null){
            ArrayList<Tile> c = new ArrayList<>();
            c.add(tile2);
            c.add(tile);
            c.add(tile3);
            chowTiles.add(c);
        }
        if (tile3 != null && tile4 != null){
            ArrayList<Tile> c = new ArrayList<>();
            c.add(tile);
            c.add(tile3);
            c.add(tile4);
            chowTiles.add(c);
        }
        return chowTiles;
    }

    /**
     * Checks if a tile can be used to form a Hu.
     * @param tile the tile to check
     * @return the type of Hu that can be formed (0 if none)
     */
    public int canHu(Tile tile) {
        ArrayList<Tile> tempHandTile = new ArrayList<>(handTile);

        if (!tempHandTile.contains(tile)) {
            tempHandTile.add(tile);
        }

        tempHandTile.sort(Comparator.comparingInt(Tile::getId));

        ArrayList<Tile> pair = new ArrayList<>();

        for (int i = 1; i < tempHandTile.size(); i++) {
            if (tempHandTile.get(i - 1).getId() / 10 == tempHandTile.get(i).getId() / 10){
                pair.add(tempHandTile.get(i - 1));
                pair.add(tempHandTile.get(i));
                i++;
            }
        }

        if (pair.size() == 0) return 0;

        // Check for 7 pairs
        if (pair.size() == 14) {
            Map<Integer, Integer> countMap = new HashMap<>();
            for (Tile tempTile : pair) {
                int currentID = tempTile.getId() / 10;
                countMap.put(currentID, countMap.getOrDefault(currentID, 0) + 1);
            }

            // Check for 4 pairs (big 7 pairs)
            for (int count : countMap.values()) {
                if (count >= 4) {
                    System.out.println("big 7 pair");
                    return 8;
                }
            }
            System.out.println("7 small pair");
            return 4;
        }

        int meldTripletNum = 0;
        for (Meld meld : melds){
            if (meld.getType().equals("Pang") || meld.getType().equals("Kong")){
                meldTripletNum++;
            }
        }

        while (pair.size() != 0) {
            ArrayList<Tile> temp = new ArrayList<>(tempHandTile);
            temp.remove(pair.remove(0));
            temp.remove(pair.remove(0));

            ArrayList<Integer> tileIDs = new ArrayList<>(temp.stream().map(Tile::getId).map(id -> id / 10).collect(Collectors.toList()));
            int tripletNum = 0;
            boolean canHu = false;

            while (tileIDs.size() >= 3) {
                if (tileIDs.get(0).equals(tileIDs.get(1)) && tileIDs.get(0).equals(tileIDs.get(2))) {
                    tileIDs.subList(0, 3).clear(); // Remove first three elements
                    tripletNum++;
                } else if (tileIDs.contains(tileIDs.get(0) + 1) && tileIDs.contains(tileIDs.get(0) + 2)) {
                    if (tileIDs.get(0) / 10 > 3){
                        continue;
                    }
                    tileIDs.remove(Integer.valueOf(tileIDs.get(0) + 2));
                    tileIDs.remove(Integer.valueOf(tileIDs.get(0) + 1));
                    tileIDs.remove(0);
                } else {
                    canHu = false;
                    break;
                }
                canHu = true;
            }
            if (canHu && tripletNum + meldTripletNum >= 1){
                System.out.println("normal");
                return 2;
            }
        }

        return 0;
    }

    /**
     * Forms a Pong with the given tile.
     * @param tile the tile to form a Pong with
     */
    public void Pang(Tile tile) {
        // Form a Pong
        ArrayList<Tile> meld = new ArrayList<>();

        for (Tile value : handTile) {
            if (tile.getId() / 10 == value.getId() / 10 && meld.size() < 2) {
                meld.add(value);
            }
        }

        removeTile(meld);
        meld.add(tile);
        melds.add(new Meld(meld, "Pang", false));
    }

    /**
     * Forms a Kong with the given tile.
     * @param tile the tile to form a Kong with
     */
    public void Kong(Tile tile) {
        // Form a Kong
        ArrayList<Tile> meld = new ArrayList<>();

        for (Tile value : handTile) {
            if (tile.getId() / 10 == value.getId() / 10) {
                meld.add(value);
            }
        }

        if (meld.size() >= 3) {
            removeTile(meld);
            if (meld.size() == 3) meld.add(tile);
            melds.add(new Meld(meld, "Kong", handTile.contains(tile)));
        } else {
            for (int i = 0; i < melds.size(); i++) {
                Meld m = melds.get(i);

                if (m.getType().equals("Pang")){
                    if (tile.getId() / 10 == m.getMeld().get(0).getId() / 10){
                        melds.remove(m);
                        meld.addAll(m.getMeld());
                        meld.add(tile);
                        melds.add(new Meld(meld, "Kong", false));
                        break;
                    }
                }
            }
        }
    }

    /**
     * Forms a Chow with the given tiles and tile.
     * @param chowTiles the tiles to form a Chow with
     * @param tile the tile to form a Chow with
     */
    public void Chow(ArrayList<Tile> chowTiles, Tile tile) {
        // Form a Chow
        chowTiles.remove(tile);
        Tile tile1 = chowTiles.remove(0);
        Tile tile2 = chowTiles.remove(0);
        handTile.remove(tile1);
        handTile.remove(tile2);
        chowTiles.add(tile1);
        chowTiles.add(tile);
        chowTiles.add(tile2);
        melds.add(new Meld(chowTiles, "Chow", false));
    }

    /**
     * Forms a Hu with the given tile.
     * @param tile the tile to form a Hu with
     * @return the type of Hu formed
     */
    public int Hu(Tile tile) {
        // Form a Hu
        int HuType = canHu(tile);

        if (!handTile.contains(tile)){
            handTile.add(tile);
        }

        ArrayList<Tile> tempHandTile = new ArrayList<>(handTile);

        // Check for 大单吊 (big single wait)
        if (tempHandTile.size() == 2){
            System.out.println("大 单吊");
            HuType *= 2;
        }

        // Check for 一条龙 (dragon)
        ArrayList<Integer> tileIDs = new ArrayList<>(tempHandTile.stream().map(Tile::getId).map(id -> id / 10).distinct().collect(Collectors.toList()));
        boolean hasAllSuites = true;
        for (int i = 1; i <= 3; i++) {
            hasAllSuites = true;
            for (int j = 1; j <= 9; j++) {
                int tileID = i * 10 + j;
                if (!tileIDs.contains(tileID)) {
                    hasAllSuites = false;
                    break;
                }
            }
            if (hasAllSuites) {
                break;
            }
        }

        if (hasAllSuites) {
            System.out.println("一条龙");
            HuType *= 2;
        }

        // Check for 清一色 (pure one suit)
        for (Meld meld : melds){
            tempHandTile.addAll(meld.getMeld());
        }

        if (tempHandTile.stream().map(Tile::getId).map(id -> id / 100).distinct().count() == 1){
            System.out.println("清一色");
            HuType *= 2;
        }

        return HuType;
    }

    private Tile inArrayList(ArrayList<Tile> tiles, int id) {
        for (Tile value : tiles) {
            if (value.getId() / 10 == id) {
                return value;
            }
        }
        return null;
    }

    public void sort(){
        handTile.sort(Comparator.comparing(Tile::getId)); // Sort the hand tiles
    }

    public ArrayList<Tile> getHandTile(){
        sort();
        return handTile;
    }

    public ArrayList<Meld> getMelds(){
        return melds;
    }
}
