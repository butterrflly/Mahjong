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
        // 根据牌编号生成相应的图片 URL
        getTileUrl(tile) {
            if (tile != null && Math.abs(tile) !== 0){
                // 获取牌的前两位
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
    width: 300px; /* 固定宽度 */
    height: 200px; /* 固定高度 */
    display: flex;
    align-items: center;
    flex-wrap: wrap; /* 使图片自动换行 */
    overflow: hidden; /* 防止图片超出容器 */
    position: fixed;
    left: 50%;
    bottom: 45%;
    transform: translate(-50%, 50%);
}

.tile {
  width: 29px; /* 根据需要调整图片的宽度 */
  height: 40px; /* 根据需要调整图片的高度 */
  margin: -1px; /* 图片之间的间距 */
}
</style>