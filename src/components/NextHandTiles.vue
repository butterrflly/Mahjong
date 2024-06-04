<template>
    <div class="hand-tiles">
        <img alt="left" id="l_wall" src="../assets/tiles-left/l_wall.png"/>
    </div>
</template>

<script>
export default {
    name: 'NextHandTiles',
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
    left: 20%;
    bottom: 50%;
    transform: translate(0, 50%);
}
</style>
