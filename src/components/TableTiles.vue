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
    display: flex;
    align-items: center;
    position: fixed;
    left: 50%;
    bottom: 50%;
    transform: translate(-50%, 50%);
}
</style>