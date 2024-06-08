<template>
    <div class="tile-container" v-if="gameStatus">
        <div v-for="(group, index) in oppoMeldTiles" :key="index" class="tile-group">
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
    name: 'OppoMeldTiles',
    props: {
        oppoMeldTiles: {
            required: true
        },

        oppoIfHideMeld: {
            required: true
        },

        gameStatus: {
          required: true,
          type: Boolean
        },
    },
    methods: {
        // 根据牌编号生成相应的图片 URL
        getTileUrl(tile, groupIndex) {
            // 检查当前组的状态
            if (this.oppoIfHideMeld[groupIndex] === true) {
                return new URL('../assets/tiles-opposite/back.png', import.meta.url).href;
            }
            // 获取牌的前两位
            const tilePrefix = String(tile).slice(0, 2);
            return new URL(`../assets/tiles-opposite/${tilePrefix}.png`, import.meta.url).href;
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
    top: 28%;
    left: 50%;
    transform: translate(-50%, 0);
}

.tile-group {
    display: flex;
    justify-content: center;
    margin-right: 20px; /* gap between groups */
}

.tile {
    width: 28px;
    height: 40px;
    margin: -1px; /* gap between tiles */
}
</style>

