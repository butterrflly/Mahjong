<template>
    <div class="hand-tiles">
        <!-- 迭代 handTiles.handTile 数组中的每个元素，为每个牌生成一个 <img> 标签 -->
        <img
            v-for="tile in handTiles"
            :key="tile"
            :src="getTileUrl(tile)"
            alt="tile"
            class="tile"
            v-on:click="discard(tile)"
        />
    </div>
</template>

<script>
import WebSocketService from '../websocket.js';

export default {
    name: 'SelfHandTiles',
    props: {
        handTiles: {
            required: true
        }
    },
    methods: {
        // 根据牌编号生成相应的图片 URL
        getTileUrl(tile) {
            // 获取牌的前两位
            const tilePrefix = String(tile).slice(0, 2);
            return new URL(`../assets/tiles-front/${tilePrefix}.png`, import.meta.url).href;
        },

        discard(id) {
            try{
                WebSocketService.sendMessage(JSON.stringify({operation: "discard", tileID: id}));
            }catch (error){
                console.log("Error when discard");
            }

        }
    }
}
</script>

<style scoped>
.hand-tiles {
    position: fixed;
    left: 50%;
    bottom: 10%;
    transform: translate(-50%, 0);
}

.tile {
    width: 38px;
    height: 60px;
}
</style>
