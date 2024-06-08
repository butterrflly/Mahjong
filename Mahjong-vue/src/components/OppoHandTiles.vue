<template>
    <div v-if="ifDeal">
        <img alt="tile" class="tile" id="deal" src="../assets/tiles-opposite/handin.png"/>
    </div>
    <div v-if="gameStatus" class="hand-tiles">
        <!-- Iterate 13 times, generating a tag for each card <img> -->
        <img
            v-for="index in 13"
            :key="index"
            alt="opp"
            class="tile"
            src="../assets/tiles-opposite/handin.png"
        />
    </div>
</template>

<script>
export default {
    name: 'OppoHandTiles',
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
    position: fixed;
    left: 50%;
    top: 20%;
    transform: translate(-50%, 0);
    display: flex;
    gap: 0;
}

.tile {
    width: 38px;
    height: 60px;
    margin: -0.5px;
    padding: 0;
    position: relative;
    box-shadow: -3px 6px 6px rgba(0, 0, 0, 0.1);
}

#deal {
    left: 30%;
    top: 20%;
    position: fixed;
}
</style>
