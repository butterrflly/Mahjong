<template>
    <div class="tile-container">
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
        }
    },
    methods: {
        // 根据牌编号生成相应的图片 URL
        getTileUrl(tile, groupIndex) {
            // 检查当前组的状态
            if (this.nextIfHideMeld[groupIndex] === true) {
                return new URL('../assets/tiles-left/back.png', import.meta.url).href;
            }
            // 获取牌的前两位
            const tilePrefix = String(tile).slice(0, 2);
            return new URL(`../assets/tiles-left/${tilePrefix}.png`, import.meta.url).href;
        }
    }
}
</script>

<style scoped>
.tile-container {
    display: flex;
    /*flex-direction: column; !* 纵向排列 *!*/
    align-items: center; /* 水平居中对齐，可选 */
    gap: 20px; /* 图片之间的间距 */
    position: fixed;
    left: 10%;
    bottom: 0;
    transform: translate(0, -50%);
}

.tile-group {
    display: flex;
    justify-content: center;
    margin-bottom: 20px; /* 组与组之间的固定间距 */
}

.tile {
    width: 50px; /* 根据需要调整图片的宽度 */
    height: 50px; /* 根据需要调整图片的高度 */
    margin: 0; /* 图片之间的间距 */
}
</style>

