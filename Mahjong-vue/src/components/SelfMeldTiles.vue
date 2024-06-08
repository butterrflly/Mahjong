<template>
    <div class="tile-container" v-if="gameStatus">
        <div v-for="(group, index) in selfMeldTiles" :key="index" class="tile-group">
            <img
                v-for="(tile, tileIndex) in group"
                :key="tileIndex"
                :src="getTileUrl(tile, index)"
                alt="meldTile"
                class="tile"
            />
        </div>
    </div>
</template>

<script>
export default {
    name: 'SelfMeldTiles',
    props: {
        selfMeldTiles: {
            required: true
        },

        selfIfHideMeld: {
            required: true
        },

        gameStatus: {
          required: true,
          type: Boolean
        },
    },
    methods: {
        // Generate the corresponding image URL based on the tile number
        getTileUrl(tile, groupIndex) {
            // Check the status of the current group
            if (this.selfIfHideMeld[groupIndex] === true) {
                return new URL('../assets/tiles-me/back.png', import.meta.url).href;
            }
            // The first two digits of the tile
            const tilePrefix = String(tile).slice(0, 2);
            return new URL(`../assets/tiles-me/${tilePrefix}.png`, import.meta.url).href;
        }
    }
}
</script>

<style scoped>
.tile-container {
    display: flex;
    flex-direction: row;
    align-items: center;
    position: fixed;
    bottom: 21%;
    left: 50%;
    transform: translate(-50%, 0);
}

.tile-group {
    display: flex;
    justify-content: center;
    margin-right: 20px;
}

.tile {
    width: 36px;
    height: 50px;
    margin: -1px;
}
</style>

