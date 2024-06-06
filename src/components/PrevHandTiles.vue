<template>
    <div v-if="gameStatus" class="hand-tiles">
        <img alt="right" id="r_wall" src="../assets/tiles-left/l_wall.png"/>
    </div>
    <div v-if="ifDeal">
        <img alt="dealTile" id="tile" src="../assets/tiles-left/handin.png"/>
    </div>
</template>

<script>
export default {
    name: 'PrevHandTiles',
    props: {
        gameStatus: {
            required: true,
            type: Boolean
        },

        ifDeal: {
            required: true,
            type: Boolean
        }
    }
}
</script>

<style scoped>
.hand-tiles {
    display: flex;
}

#r_wall {
    width: 80px;
    height: 320px;
    position: fixed;
    left: 20%;
    bottom: 50%;
    transform: translate(0, 50%);
}

#tile {
    width: 31px;
    height: 50px;
    position: fixed;
    left: 19.7%;
    bottom: 26.5%;
}
</style>
