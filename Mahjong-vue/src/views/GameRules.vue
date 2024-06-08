<template>
  <div class="background">
    <div class="content">
      <header>
        <router-link to="/Menu"><BackButton class="backButton"/></router-link>
        <h1>BEIJING MAHJONG RULES</h1>
      </header>

      <section>
        <h2>Basic rules</h2>
        <p>Beijing Mahjong is a traditional Chinese mahjong game. It is usually played by four players and uses 136 cards, including:</p>
        <ul>
          <li>Dots (1 to 9, 4 of each type)</li>
          <li>Bamboos (1 to 9, 4 of each type)</li>
          <li>Characters (1 to 9, 4 of each type)</li>
          <li>Winds (four each for East, South, West, and North.)</li>
          <li>Dragons (4 tiles each for medium, hair, and white)</li>
        </ul>
      </section>

      <section>
        <h2>The goal of the game</h2>
        <p>The goal of the game is to form a prescribed tile type and "win" by drawing tiles and Pong / Kong / Chow.</p>
      </section>

      <section>
        <h2>Winning conditions</h2>
        <p>To win, the following basic conditions need to be followed:</p>
        <ul>
          <li>Two same tiles are needed first</li>
          <li>The rest of the tiles must form Sequence, Triplet or Kong</li>
        </ul>
      </section>

      <section>
        <h2>Scoring rules</h2>
        <p>After winning a hand, the score is calculated based on the combination of hand type and special tile. Here are some common ways to score:</p>
        <ul>
          <li>Ping Hu (Standard Winning Hand): Basic Points</li>
          <li>Zimo (Self-drawn Win): Extra Points</li>
          <li>Qi Dui (Seven Pairs): Extra Points</li>
          <li>Qing Yi Se (Pure One Suit): Extra Points</li>
          <li>Da San Yuan (Big Three Dragons): Major Extra Points</li>
        </ul>
      </section>

      <section>
        <h2>Common terms</h2>
        <p>In Beijing Mahjong, there are some common terms to be aware of:</p>
        <ul>
          <li>Chow：Get a tile from the Previous player and combine a sequence with two of the hand tiles.</li>
          <li>Pong：Get a tile which is same with two of hand tiles , combine three of them into a Triplet.</li>
          <li>Kong：Get a tile which is same with three of hand tiles , combine four of them into a Kong, and take a new tiles from the Mahjong tiles wall.</li>
          <li>Dealer: Always by the dealer first touch tiles, and first out of tiles, same time ,do not need to touch tiles</li>
        </ul>
      </section>

      <section>
        <h2>Game flow</h2>
        <p>The following is the basic game flow of Beijing Mahjong：</p>
        <ul>
          <li><strong>Preparation Phase:</strong>Shuffle the tiles, code the tiles and determine the dealer. Each player draws 13 tiles and the dealer draws 14.</li>
          <li><strong>Draw cards:</strong>In counterclockwise order, the dealer draws a tile first.</li>
          <li><strong>Discarded:</strong>Each player takes turns playing cards, and the other players can choose to Pong / Kong / Chow.</li>
          <li><strong>Pong / Kong / Chow：</strong>According to the rules,do the operation of Pong / Kong / Chow, and replenishing cards.</li>
          <li><strong>Win：</strong>When a player satisfies the winning conditions, the winning hand is declared and settled.</li>
          <li><strong>Settlement：</strong>Points are scored according to the winning hand type and special tiles, and the next game is played。</li>
        </ul>
      </section>

      <img alt="Mahjong" class="logo" src="../assets/mahjong.png" width="334" height="240"/>
    </div>
  </div>
</template>

<script>
import BackButton from "../components/BackButton.vue";
import RulesButton from "../components/RulesButton.vue";
import {Collection, Postcard, GoldMedal} from "@element-plus/icons-vue";

export default {
  name: "GameRules",
  components: {Collection, Postcard, GoldMedal, RulesButton, BackButton}
}
</script>

<style scoped>
h1 {
  h1 {
    font-weight: 500;
    font-size: 0.8rem; /* 10% smaller than 1.5rem */
    position: fixed;
    top: 20px;
    left: 620px;
    text-align: center;
    color: #2c3e50;
  }
}

.backButton {
  position: fixed;
  margin: 0 0 0 0;
  left: 5%;
  top: 4%;
  width: 3%;
  height: 4vh;
}

.logo {
  margin: 8rem 0 0 0; /* Adjusted margin */
  position: fixed;
  left: 850px;
  bottom: 10px;
}

.background {
  background-image: url('../assets/background.png');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  overflow: hidden;
}

.content {
  margin: 8rem 0 0 0; /* Adjusted margin */
  position: absolute;
  left: 80px;
  top: 20px;
  width: calc(100% - 160px);
  height: calc(100vh - 40px);
  overflow-y: auto;
}

section {
  padding: 15px; /* Adjusted padding */
  margin-bottom: 5px; /* Reduced margin */
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  float: left;
  width: 40%;
  margin-right: 3%; /* Reduced margin */
}
</style>
