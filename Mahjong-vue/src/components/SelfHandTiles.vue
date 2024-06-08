<template>
    <div v-if="gameStatus" class="container">
        <div class="hand-tiles">
            <!-- Iterate over each element in the handTiles.handTile array to generate a tag for each tile <img> -->
            <img
                v-for="tile in handTiles"
                :key="tile"
                :src="getTileUrl(tile)"
                alt="tile"
                class="tile"
                v-on:click="discard(tile, canDiscard)"
            />
        </div>
        <div class="deal-tiles">
            <img v-if="getTileUrl(dealID)!== ''"
                 alt="dealTile" class="tile"
                 :src="getTileUrl(dealID)"
                 v-on:click="discard(dealID, canDiscard)"/>
        </div>
    </div>
</template>

<script>
import WebSocketService from '../websocket.js';

export default {
    name: 'SelfHandTiles',
    props: {
        gameStatus: {
            required: true,
            type: Boolean
        },

        handTiles: {
            required: true
        },

        dealID: {
            required: true
        },

        canDiscard: {
            required: true,
        }
    },
    methods: {
        // Generate the corresponding image URL based on the card number
        getTileUrl(tile) {
            if (tile != null && Math.abs(tile) !== 0){
                // The first two digits of the tiles
                const tilePrefix = Math.floor(tile / 10);
                return new URL(`../assets/tiles-front/${tilePrefix}.png`, import.meta.url).href;
            }else {
                return ''
            }
        },

        discard(id, canDiscard) {
            console.log(canDiscard)
            if (canDiscard === true){
                try{
                    WebSocketService.sendMessage(JSON.stringify({operation: "discard", tileID: id}));
                }catch (error){
                    console.log("Error when discard");
                }
            }else{
                console.log("can't discard")
            }
        }
    }
}
</script>

<style scoped>
.container {
    display: flex;
    align-items: center;
    position: fixed;
    left: 50%;
    bottom: 8%;
    transform: translate(-50%, 0);
}

.hand-tiles {
    position: relative;
    box-shadow: 3px -6px 8px rgba(0, 0, 0, 0.1);
}

.deal-tiles {
    position: relative;
    margin-left: 20px;
    box-shadow: 3px -6px 8px rgba(0, 0, 0, 0.1);
}

.hand-tiles img {
    transition: transform 0.3s;
}
.hand-tiles img:hover {
    transform: translateY(-15px);
}
.deal-tiles img {
    transition: transform 0.3s;
}
.deal-tiles img:hover {
    transform: translateY(-15px);
}

.tile {
    width: 50px;
    height: 80px;
}
</style>
