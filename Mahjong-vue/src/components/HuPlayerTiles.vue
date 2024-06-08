<template>
  <div v-if="gameStatus" class="handTiles">
    <img
        v-for="tile in handTiles"
        :key="tile"
        :src="getTileUrl(tile)"
        alt="tile"
        class="tile"
    />
  </div>
  <div v-if="gameStatus" class="meldTiles">
    <div v-for="(group, index) in meldTiles" :key="index" class="tile-group">
      <img
          v-for="(tile, tileIndex) in group"
          :key="tileIndex"
          :src="getTileUrl(tile, index)"
          alt="meldTile"
          class="tile"
      />
    </div>
  </div>
</template>

<script>
export default {
  name: "HuPlayerTiles",
  props: {
    gameStatus: {
      required: true,
      type: Boolean
    },

    handTiles: {
      required: true,
    },

    meldTiles: {
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
.handTiles {
  display: flex;
  align-items: center;
  position: fixed;
  left: 50%;
  bottom: 30%;
  transform: translate(-50%, 0);
}

.meldTiles {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: fixed;
  bottom: 30%;
  left: 50%;
  transform: translate(-50%, 0);
}

.tile-group {
  display: flex;
  justify-content: center;
  margin-bottom: 20px; /* 组与组之间的固定间距 */
}

.tile {
  width: 46px; /* 根据需要调整图片的宽度 */
  height: 64px; /* 根据需要调整图片的高度 */
  margin: -1px; /* 图片之间的间距 */
}
</style>