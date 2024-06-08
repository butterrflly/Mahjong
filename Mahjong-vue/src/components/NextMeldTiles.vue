<template>
    <div class="hand-tiles" v-if="gameStatus">
        <div v-for="(group, index) in nextMeldTiles" :key="index" class="tile-group">
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
    name: 'NextMeldTiles',
    props: {
        nextMeldTiles: {
            required: true
        },

        nextIfHideMeld: {
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
            if (this.nextIfHideMeld[groupIndex] === true) {
                return new URL('../assets/tiles-right/back.png', import.meta.url).href;
            }
            // The first two digits of the tile
            const tilePrefix = String(tile).slice(0, 2);
            return new URL(`../assets/tiles-right/${tilePrefix}.png`, import.meta.url).href;
        }
    }
}
</script>

<style scoped>
.hand-tiles {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 20px;
    position: fixed;
    right: 28%;
    bottom: 50%;
    transform: translate(0, 50%);
}

.tile-group {
    display: flex;
    justify-content: center;
    margin-bottom: 20px;
    flex-direction: column;
}

.tile {
    width: 40px;
    height: 32px;
    margin: -5.5px;
}
</style>

