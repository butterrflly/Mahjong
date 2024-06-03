package com.smoker.mahjong.doma.Game;

import java.util.ArrayList;
import java.util.Comparator;

public class MeldUtil {
    private ArrayList<Tile> tiles;
    private ArrayList<ArrayList<Tile>> mayTriplet;
    private int triplet;
    private int sequence;

    public MeldUtil(){
        tiles = new ArrayList<>();
        mayTriplet = new ArrayList<>();
    }

    public void addTile(Tile tile){
        tiles.add(tile);
    }

    public boolean isHu(){
        tiles.sort(Comparator.comparingInt(Tile::getId));

        if (tiles.size() == 0) {
            triplet = 0;
            sequence = 0;
            return true;
        }

        if (tiles.size() % 3 != 0) {
            return false;
        }


        for (int i = 0; i < tiles.size() - 2; i++) {
            if (tiles.get(i).getId() / 10 == tiles.get(i + 1).getId() / 10 && tiles.get(i).getId() / 10 == tiles.get(i + 2).getId() / 10){
                ArrayList<Tile> temp = new ArrayList<>();
                temp.add(tiles.get(i));
                temp.add(tiles.get(i + 1));
                temp.add(tiles.get(i + 2));
                mayTriplet.add(temp);
                i = i + 2;
            }
        }


        if (tiles.get(0).getId() / 100 > 3){
            if (tiles.size() / 3 == mayTriplet.size()) {
                triplet = mayTriplet.size();
                sequence = 0;
                return true;
            }
            return false;
        }


        if (tiles.size() == 12) {
            if (mayTriplet.size() == 0) return false;

            if (mayTriplet.size() == 1) {
                ArrayList<Tile> temp = new ArrayList<>(tiles);
                temp.removeAll(mayTriplet.get(0));
                triplet = 1;
                sequence = 3;
                return isSequence(temp);
            }

            if (mayTriplet.size() == 2) {
                ArrayList<Tile> temp1 = new ArrayList<>(tiles);
                ArrayList<Tile> temp2 = new ArrayList<>(tiles);
                ArrayList<Tile> temp3 = new ArrayList<>(tiles);
                temp1.removeAll(mayTriplet.get(0));
                temp1.removeAll(mayTriplet.get(1));
                temp2.removeAll(mayTriplet.get(0));
                temp3.removeAll(mayTriplet.get(1));
                triplet = 2;
                sequence = 2;
                return isSequence(temp1) || isSequence(temp2) || isSequence(temp3);
            }

            if (mayTriplet.size() == 3) {
                ArrayList<Tile> temp1 = new ArrayList<>(tiles);
                ArrayList<Tile> temp2 = new ArrayList<>(tiles);
                ArrayList<Tile> temp3 = new ArrayList<>(tiles);
                ArrayList<Tile> temp4 = new ArrayList<>(tiles);
                temp1.removeAll(mayTriplet.get(0));
                temp1.removeAll(mayTriplet.get(1));
                temp1.removeAll(mayTriplet.get(2));
                temp2.removeAll(mayTriplet.get(0));
                temp3.removeAll(mayTriplet.get(1));
                temp4.removeAll(mayTriplet.get(2));
                triplet = 3;
                sequence = 1;
                return isSequence(temp1) || isSequence(temp2) || isSequence(temp3) || isSequence(temp4);
            }

            if (mayTriplet.size() == 4) {
                triplet = 4;
                sequence = 0;
                return true;
            }
        }


        if (tiles.size() == 9) {
            if (mayTriplet.size() == 0) {
                triplet = 0;
                sequence = 3;
                return isSequence(tiles);
            }

            if (mayTriplet.size() == 1) {
                ArrayList<Tile> temp = new ArrayList<>(tiles);
                temp.removeAll(mayTriplet.get(0));
                triplet = 1;
                sequence = 2;
                return isSequence(temp);
            }

            if (mayTriplet.size() == 2) {
                ArrayList<Tile> temp1 = new ArrayList<>(tiles);
                temp1.removeAll(mayTriplet.get(0));
                temp1.removeAll(mayTriplet.get(1));
                triplet = 2;
                sequence = 1;
                return isSequence(temp1);
            }

            if (mayTriplet.size() == 3) {
                triplet = 3;
                sequence = 0;
                return true;
            }
        }


        if (tiles.size() == 6) {
            if (mayTriplet.size() == 0) {
                triplet = 0;
                sequence = 2;
                return isSequence(tiles);
            }

            if (mayTriplet.size() == 1) {
                ArrayList<Tile> temp = new ArrayList<>(tiles);
                temp.removeAll(mayTriplet.get(0));
                triplet = 1;
                sequence = 1;
                return isSequence(temp);
            }

            if (mayTriplet.size() == 2) {
                triplet = 2;
                sequence = 0;
                return true;
            }
        }


        if (tiles.size() == 3) {
            if (mayTriplet.size() == 0) {
                triplet = 0;
                sequence = 1;
                return isSequence(tiles);
            }

            if (mayTriplet.size() == 1) {
                triplet = 1;
                sequence = 0;
                return true;
            }

        }


        return false; // buzhidao
    }

    public boolean isSequence(ArrayList<Tile> tiles){
        tiles.sort(Comparator.comparingInt(Tile::getId));
        ArrayList<Integer> tileIds = new ArrayList<>();
        for (Tile tile : tiles) {
            tileIds.add(tile.getId() / 10);
        }
        while (tileIds.size() != 0) {
            int id = tileIds.get(0);
            if (tileIds.contains(id + 1) && tileIds.contains(id + 2)){
                tileIds.remove(Integer.valueOf(id));
                tileIds.remove(Integer.valueOf(id + 1));
                tileIds.remove(Integer.valueOf(id + 2));
            }
            else{
                return false;
            }
        }
        return true;
    }

    public int tripletNum(){
        return triplet;
    }

    public int sequenceNum(){
        return sequence;
    }

}