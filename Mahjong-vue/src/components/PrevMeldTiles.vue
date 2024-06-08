<template>
    <div class="tile-container" v-if="gameStatus">
        <div v-for="(group, index) in prevMeldTiles" :key="index" class="tile-group">
            <img
                    v-for="(tile, tileIndex) in group"
                    :key="tileIndex"
                    :src="getTileUrl(tile, index)"
                    alt="tile"
                    class="tile"
            />
        </div>
    </div>
</template>

<script>
export default {
    name: 'PrevMeldTiles',
    props: {
        prevMeldTiles: {
            required: true
        },

        prevIfHideMeld: {
            required: true
        },

        gameStatus: {
          required: true,
          type: Boolean
        },
    },
    methods: {
        // Generate the corresponding image URL based on the card number
        getTileUrl(tile, groupIndex) {
            // Check the status of the current group
            if (this.prevIfHideMeld[groupIndex] === true) {
                return new URL('../assets/tiles-left/back.png', import.meta.url).href;
            }
            // The first two digits of the tile
            const tilePrefix = String(tile).slice(0, 2);
            return new URL(`../assets/tiles-left/${tilePrefix}.png`, import.meta.url).href;
        }
    }
}
</script>

<style scoped>
.tile-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    position: fixed;
    left: 28%;
    bottom: 50%;
    transform: translate(0, 50%);
}

.tile-group {
    display: flex;
    flex-direction: column;
    justify-content: center;
    margin-bottom: 20px;
}

.tile {
    width: 40px;
    height: 32px;
    margin: -5.5px;
}
</style>

