<template>
    <div class="hand-tiles">
        <!-- 迭代 13 次，为每个牌生成一个 <img> 标签 -->
        <img
            v-for="index in 13"
            :key="index"
            alt="opp"
            class="tile"
            :src="setUrl()"
        />
    </div>
</template>

<script>
export default {
    name: 'OppoHandTiles',

    methods: {
        setUrl(){
            return new URL(`../assets/tiles-opposite/opp_wall.png`, import.meta.url).href
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
}

.tile {
    width: 38px;
    height: 60px;
}
</style>
