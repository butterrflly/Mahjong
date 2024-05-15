package com.smoker.mahjong.doma.Game;


import java.util.ArrayList;
import java.util.Collections;
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

    public void cleanTile(){
        handTile.clear();
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


    /**
     * 每回合检查自己的手牌有没有 4 个一样的牌
     * 检查是否可以在自己回合内杠牌
     * @return 可以杠牌 的 列表 ，可能有多个可以开杠的牌
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
     * 因为可能会有多种吃法，所以返回一个二维数组
     * 二位数组 肯能为空， 也就是没有可以吃的 组合
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


    public boolean canHu(Tile tile) {
        ArrayList<Tile> tempHandTile = new ArrayList<>(handTile);
        tempHandTile.add(tile);

        ArrayList<Tile> pair = new ArrayList<>();

        int jump = 0;
        for (int i = 0; i < tempHandTile.size() - 1; i++) {

            if (tempHandTile.get(i).getId() / 10 == jump)  continue;

            if (tempHandTile.get(i).getId() / 10 == tempHandTile.get(i + 1).getId() / 10){
                pair.add(tempHandTile.get(i));
                pair.add(tempHandTile.get(i + 1));
                jump = tempHandTile.get(i).getId() / 10;
            }
        }

        if (pair.size() == 0) return false;

        while (pair.size() != 0){
            Tile tile1 = pair.remove(0);
            Tile tile2 = pair.remove(0);
            tempHandTile.remove(tile1);
            tempHandTile.remove(tile2);

            MeldUtil meldUtil1 = new MeldUtil();
            MeldUtil meldUtil2 = new MeldUtil();
            MeldUtil meldUtil3 = new MeldUtil();
            MeldUtil meldUtil4 = new MeldUtil();
            MeldUtil meldUtil5 = new MeldUtil();

            for (Tile t : tempHandTile) {
                if (t.getId() / 100 == 1) meldUtil1.addTile(t);
                if (t.getId() / 100 == 2) meldUtil2.addTile(t);
                if (t.getId() / 100 == 3) meldUtil3.addTile(t);
                if (t.getId() / 100 == 4) meldUtil4.addTile(t);
                if (t.getId() / 100 == 5) meldUtil5.addTile(t);
            }

            boolean result = meldUtil1.isHu() & meldUtil2.isHu() & meldUtil3.isHu() & meldUtil4.isHu() & meldUtil5.isHu();
            int triplet = meldUtil1.tripletNum() + meldUtil2.tripletNum() + meldUtil3.tripletNum() + meldUtil4.tripletNum() + meldUtil5.tripletNum();

            if (triplet > 0 && result){
                return true;
            }
            tempHandTile.add(tile1);
            tempHandTile.add(tile2);
        }

        return false;
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


    /**
     * 因为可能会有多种吃法，所以需要一种吃的组合
     * 所以额外的一个数组参数就是吃的组合
     */
    public void Chow(ArrayList<Tile> chowTiles, Tile tile) {
        // 吃牌
        chowTiles.remove(tile);
        Tile tile1 = chowTiles.remove(0);
        Tile tile2 = chowTiles.remove(1);
        handTile.remove(tile1);
        handTile.remove(tile2);
        chowTiles.add(tile1);
        chowTiles.add(tile);
        chowTiles.add(tile2);
        melds.add(new Meld(chowTiles, "chow"));
    }


    public void Hu(Tile tile) {
        // 胡牌
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

class MeldUtil {
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