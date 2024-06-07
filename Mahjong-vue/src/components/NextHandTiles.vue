<template>

    <div v-if="gameStatus" class="hand-tiles">
        <img alt="left" id="l_wall" src="../assets/tiles-right/r_wall.png"/>
    </div>
    <div v-if="ifDeal">
        <img alt="dealTile" id="tile" src="../assets/tiles-right/handin.png"/>
    </div>
</template>

<script>
export default {
    name: 'NextHandTiles',
    props: {
        gameStatus: {
            required: true,
            type: Boolean
        },

        ifDeal: {
            required: true,
        }
    }
}
</script>

<style scoped>
.hand-tiles {
    display: flex;
}

#l_wall {
    width: 80px;
    height: 320px;
    position: fixed;
    right: 20%;
    bottom: 50%;
    transform: translate(0, 50%);
}

#tile {
    width:31px;
    height:50px;
    position: fixed;
    right: 23.8%;
    top: 26%;
}

</style>
