package com.smoker.mahjong.doma.Game;



public class Tile {
    private int id;
    private String suit;
    private String num;
    private boolean isHidden;

    public Tile(int id, boolean isHidden) {
        this.id = id;
        this.isHidden = isHidden;
    }

    public int getId() {
        return id;
    }

}
