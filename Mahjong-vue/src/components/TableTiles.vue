<template>
    <div v-if="gameStatus" class="container">
        <img
            v-for="tile in tiles"
            :key="tile"
            :src="getTileUrl(tile)"
            alt="tile"
            class="tile"
        />
    </div>
</template>

<script>
export default {
    name: "TableTiles",
    props: {
        gameStatus: {
            required: true,
            type: Boolean
        },

        tiles: {
            required: true,
        }
    },

    methods: {
        // Generate the corresponding image URL based on the tile number
        getTileUrl(tile) {
            if (tile != null && Math.abs(tile) !== 0){
                // The first two digits of the tile
                const tilePrefix = Math.floor(tile / 10);
                return new URL(`../assets/tiles-me/${tilePrefix}.png`, import.meta.url).href;
            }else {
                return ''
            }
        },
    }
}
</script>

<style scoped>
.container {
    width: 300px;
    height: 200px;
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    overflow: hidden;
    position: fixed;
    left: 50%;
    bottom: 45%;
    transform: translate(-50%, 50%);
}

.tile {
  width: 29px;
  height: 40px;
  margin: -1px;
}
</style>