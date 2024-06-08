<template>
  <div v-if="!gameStatus" class="handTiles">
    <img
        v-for="tile in handTiles"
        :key="tile"
        :src="getTileUrl(tile)"
        alt="tile"
        class="tile"
    />
  </div>
  <div v-if="!gameStatus" class="meldTiles">
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
    // Generate the corresponding image URL based on the card number
    getTileUrl(tile) {
      if (tile != null && Math.abs(tile) !== 0){
        // The first two digits of the tiles
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
  margin-bottom: 20px;
}

.tile {
  width: 46px;
  height: 64px;
  margin: -1px;
}
</style>