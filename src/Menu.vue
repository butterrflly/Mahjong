<script setup>
import GameTitle from './components/GameTitle.vue'
import RulesButton from "@/components/RulesButton.vue";
import MatchingButton from "@/components/MatchingButton.vue";
</script>

<template>
    <div class="background">
        <header>
            <img alt="Vue logo" class="logo" src="./assets/logo.png" width="150" height="150" />
            <div class="gameTitle">
                <GameTitle msg="Mahjong Game" />
            </div>
        </header>

        <main>
            <router-link to="/GameRules"><RulesButton class="rulesButton"/></router-link>
            <router-link to="/Login"><MatchingButton class="matchButton"/></router-link>

        </main>

    </div>
</template>

<style scoped>
    @import "src/assets/font/font.css";
    header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
    font-family: "阿里妈妈灵动体 VF Thin";
    }

    .logo {
    margin: 10rem 0 0 0;
    position: fixed;
    left: 280px;
    top:-100px;
    }

    .background {
        background-image: url('@/assets/background.png');
        background-size: cover;
        background-repeat: no-repeat;
        background-position: center;
        position: fixed;
        top:0;
        left:0;
        width: 100%;
        height: 100vh;
    }

    .rulesButton {
        position: fixed;
        margin: 20rem 10rem 0 0;
        left: 39%;
        width: 22%;
        height: 10vh;
    }

    .matchButton {
        position: fixed;
        margin: 35rem 10rem 0 0;
        left: 35%;
        width: 30%;
        height: 10vh;
    }
</style>
