<template>
    <div class="hand-tiles">
        <img alt="right" id="r_wall" src="../assets/tiles-right/r_wall.png"/>
    </div>
    <div v-if="ifDeal">
        <img alt="tile" id="tile" src="../assets/tiles-right/handin.png"/>
    </div>
</template>

<script>
export default {
    name: 'PrevHandTiles',
    props: {
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
    right: 20%;
    bottom: 50%;
    transform: translate(0, 50%);
}

#tile {
    width: 38px;
    height: 60px;
    position: fixed;
    right: 20%;
    bottom: 30%;
}
</style>
