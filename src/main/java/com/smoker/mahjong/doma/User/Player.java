package com.smoker.mahjong.doma.User;


import com.smoker.mahjong.doma.Game.HandTile;
import com.smoker.mahjong.doma.Game.Meld;
import com.smoker.mahjong.doma.Game.Tile;
import java.util.ArrayList;

public class Player {
    private int id;
    private String name;
    private HandTile handTile;
    private ArrayList<Meld> melds;
    private int score;
    private boolean prepare;

    public Player(String name) {
        this.name = name;
        this.handTile = new HandTile();
        this.melds = new ArrayList<Meld>();
        this.score = 5000; // 分数初始化
        this.prepare = false;
    }


    public void addInitialHand(Tile tile) {
        //Distribute the initial hand
        handTile.addTile(tile);
        handTile.sort();
    }


    /**
     * 用于游戏结束后的算分
     *
     * @param banker 庄家
     * @param winner 赢家，可能是null
     * @param loser  输家，可能是null
     */
    public void scoring(Player banker, Player winner, ArrayList<Player> loser, String HuTYpe) {
        int points = 100;
        // 荒牌
        if (winner == null) {
            return;
        }

        if (HuTYpe.equals("normal")) {
            points = 100;
        } else if (HuTYpe.equals("seven pairs")) {
            points = 200;
        }


        if (winner == this) {
            // 胡牌
            if (loser.size() == 1) {
                // 不是 自摸胡牌
                if (banker == this) {
                    // 庄家小胡
                    score += points * 8;
                } else {
                    // 闲家小胡
                    if (loser.get(0) == banker) {
                        // 庄家点炮
                        score += points * 6;
                    } else {
                        // 闲家点炮
                        score += points * 5;
                    }
                }
            } else {
                // 自摸胡牌
                if (banker == this) {
                    // 庄家自摸
                    score += points * 12;
                } else {
                    // 闲家自摸
                    score += points * 8;
                }
            }
        } else {
            if (loser.contains(this)) {
                // 是自己点炮 或 有人自摸
                points *= 2;
            }
            // 别人点炮且 无自摸
            if (banker == this) {
                // 自己是庄家
                score -= points * 2;
            } else {
                // 自己不是庄家
                if (winner == banker) {
                    // 庄家胡牌
                    score -= points * 2;
                } else {
                    // 闲家胡牌
                    score -= points;
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public HandTile getHandTile() {
        return handTile;
    }

    public int getScore() {
        return score;
    }



    public boolean isPrepare() {
        return prepare;
    }

    public void setPrepare() {
        prepare = !prepare;
    }

}
