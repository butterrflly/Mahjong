<template>
    <div class="hand-tiles">
        <!-- 迭代 13 次，为每个牌生成一个 <img> 标签 -->
        <img
            v-for="index in 13"
            :key="index"
            alt="opp"
            class="tile"
            src="../assets/tiles-opposite/handin.png"
        />
    </div>
    <div v-if="ifDeal">
        <img alt="tile" class="tile" src="../assets/tiles-opposite/handin.png"/>
    </div>
</template>

<script>
export default {
    name: 'OppoHandTiles',
    props: {
        ifDeal: {
            required: true,
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
    display: flex; /* 使用 flex 布局 */
    gap: 0; /* 设置图片之间的间距 */
}

.tile {
    width: 38px;
    height: 60px;
    margin: 0; /* 清除默认的 margin */
    padding: 0; /* 清除默认的 padding */
}
</style>
