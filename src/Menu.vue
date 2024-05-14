<script setup>
import GameTitle from './components/GameTitle.vue'
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

        </main>
    </div>
</template>

<style scoped>
header {
  line-height: 1.5;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
  left: 40px;
}

.background {
    background-image: url('@/assets/background.jpeg');
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
    position: fixed;
    top:0;
    left:0;
    width: 100%;
    height: 100vh;
}

@media (min-width: 1024px) {
    header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
    }

    .logo {
    margin: 0 2rem 0 0;
    }

    header .logo {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
    }

    .background {
        background-image: url('@/assets/background.jpeg');
        background-size: cover;
        background-repeat: no-repeat;
        background-position: center;
        position: fixed;
        top:0;
        left:0;
        width: 100%;
        height: 100vh;
    }
}
</style>
