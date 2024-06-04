<template>
    <div class="background">
        <div class="me">  <!--my tiles-->
            <SelfHandTiles :handTiles="selfHandTiles" />
            <img
                :src="deal()"
                alt="selfDeal"
                class="Deal"
                id="selfDeal"
            />
            <SelfMeldTiles :selfMeldTiles="selfMeldTiles" :selfIfHideMeld="selfIfHideMeld"/>
            <img
                :src="discard()"
                alt="selfDiscard"
                class="Discard"
                id="selfDiscard"
            />
            <button v-on:click="hu()" v-if="canHu">Hu</button>
            <button v-on:click="noHu()" v-if="canHu">Skip</button>
            <button v-on:click="chow()" v-if="canChow">Chow</button>
            <div>
                <div v-for="(group, index) in chowList" v-on:click="selectChow(chowList[index])" :key="index" class="chowGroup">
                    <img
                        v-for="(tile, tileIndex) in group"
                        :key="tileIndex"
                        :src="getTileUrl(tile)"
                        alt="chowTile"
                        class="chowTile"
                    />
                </div>
            </div>
            <button v-on:click="pang()" v-if="canPang">Pang</button>
            <button v-on:click="kong()" v-if="canKong">Kong</button>
            <img
                v-for="tile in kongList"
                :key="tile"
                :src="getTileUrl(tile)"
                alt="kongTile"
                class="kongTile"
                v-on:click="selectKong(tile)"
            />
            <div v-on:click="selectKong(id)"></div>
            <button v-on:click="noAffair()" v-if="canChow||canPang||canKong">Skip</button>
        </div>
        <div class="left">  <!--left tiles-->
            <NextHandTiles />
            <img
                :src="deal()"
                alt="nextDeal"
                class="Deal"
                id="nextDeal"
            />
            <NextMeldTiles :nextMeldTiles="nextMeldTiles" :nextIfHideMeld="nextIfHideMeld"/>
            <img
                :src="discard()"
                alt="nextDiscard"
                class="Discard"
                id="nextDiscard"
            />
        </div>
        <div class="opposite">  <!--opposite tiles-->
            <OppoHandTiles />
            <img
                :src="deal()"
                alt="oppoDeal"
                class="Deal"
                id="oppoDeal"
            />
            <OppoMeldTiles :oppoMeldTiles="oppoMeldTiles" :oppoIfHideMeld="oppoIfHideMeld"/>
            <img
                :src="discard()"
                alt="oppoDiscard"
                class="Discard"
                id="oppoDiscard"
            />
        </div>
        <div class="right">  <!--right tiles-->
            <PrevHandTiles />
            <img
                :src="deal()"
                alt="prevDeal"
                class="Deal"
                id="prevDeal"
            />
            <PrevMeldTiles :prevMeldTiles="prevMeldTiles" :prevIfHideMeld="prevIfHideMeld"/>
            <img
                :src="discard()"
                alt="prevDiscard"
                class="Discard"
                id="prevDiscard"
            />
        </div>
    </div>
</template>

<script>
import {postData} from "@/api.js";
import WebSocketService from '../websocket.js';
import SelfHandTiles from "@/components/SelfHandTiles.vue";
import NextHandTiles from "@/components/NextHandTiles.vue";
import OppoHandTiles from "@/components/OppoHandTiles.vue";
import PrevHandTiles from "@/components/PrevHandTiles.vue";
import SelfMeldTiles from "@/components/SelfMeldTiles.vue";
import OppoMeldTiles from "@/components/OppoMeldTiles.vue";
import PrevMeldTiles from "@/components/PrevMeldTiles.vue";
import NextMeldTiles from "@/components/NextMeldTiles.vue";


export default {
    name: "GameTable",

    components: {
        PrevMeldTiles,
        OppoMeldTiles,
        NextMeldTiles,
        SelfMeldTiles,
        PrevHandTiles,
        OppoHandTiles,
        NextHandTiles,
        SelfHandTiles,
    },

    data(){
        return{
            // name of each player
            selfName:'',
            nextName:'',
            oppoName:'',
            prevName:'',
            // {"self", "nextPlayer", "oppositePlayer", "prevPlayer"}
            discardPosition:'',
            dealPosition:'',
            // int
            discardID:'',
            dealID:'',
            // hand tiles of each player, [int, int, ...]
            selfHandTiles:'',
            nextHandTiles:'',
            oppoHandTiles:'',
            prevHandTiles:'',
            // meld tiles of each player, 2D array
            // [[int, int, int], ...]
            selfMeldTiles:'',
            nextMeldTiles:'',
            oppoMeldTiles:'',
            prevMeldTiles:'',
            // if meld tiles hide, a boolean for a group
            // [boolean, boolean, ...]
            selfIfHideMeld:'',
            nextIfHideMeld:'',
            oppoIfHideMeld:'',
            prevIfHideMeld:'',

            canHu:false,
            canChow:false,
            canPang:false,
            canKong:false,

            ifHu:false,
            ifChow:false,
            ifPang:false,
            ifKong:false,

            huName:'',
            kongList:'',  // [TileID_1, TileID_2, ...]
            chowList:'',  // [[TileID_1, TileID_2, TileID_3], ...]
            message:'',
        }
    },

    methods:{
        // handle the data received by listener
        handleMessage(data) {
            if (data.operation === "getRoomPlayerMessage") {
                this.selfName = JSON.stringify(data.msg["self"]["name"]);
                this.nextName = JSON.stringify(data.msg["nextPlayer"]["name"]);
                this.oppoName = JSON.stringify(data.msg["oppositePlayer"]["name"]);
                this.prevName = JSON.stringify(data.msg["prevPlayer"]["name"]);
            } else if (data.operation === "discard") {
                this.discard(data.msg["position"], data.msg["tileID"])
            } else if (data.operation === "getHandTile") {
                this.selfHandTiles = JSON.stringify(data.msg["self"]["handTile"]);
                this.nextHandTiles = JSON.stringify(data.msg["nextPlayer"]["handTile"]);
                this.oppoHandTiles = JSON.stringify(data.msg["oppositePlayer"]["handTile"]);
                this.prevHandTiles = JSON.stringify(data.msg["prevPlayer"]["handTile"]);
            } else if (data.operation === "getMeld") {
                this.selfMeldTiles = JSON.stringify(data.msg["self"]["melds"]);
                this.nextMeldTiles = JSON.stringify(data.msg["nextPlayer"]["melds"]);
                this.oppoMeldTiles = JSON.stringify(data.msg["oppositePlayer"]["melds"]);
                this.prevMeldTiles = JSON.stringify(data.msg["prevPlayer"]["melds"]);
                this.selfIfHideMeld = JSON.stringify(data.msg["self"]["isHide list"]);
                this.nextIfHideMeld = JSON.stringify(data.msg["nextPlayer"]["isHide list"]);
                this.oppoIfHideMeld = JSON.stringify(data.msg["oppositePlayer"]["isHide list"]);
                this.prevIfHideMeld = JSON.stringify(data.msg["prevPlayer"]["isHide list"]);
            } else if (data.operation === "deal") {
                this.deal(data.msg["position"], data.msg["tileID"])
            } else if (data.operation === "canHu") {
                this.canHu = data.msg["canHu"];
            } else if (data.operation === "getAffair") {
                this.canChow = data.msg["canChow"];
                this.canPang = data.msg["canPang"];
                this.canKong = data.msg["canKong"];
            } else if (data.operation === "Hu") {
                this.huName = JSON.stringify(data.msg["playerName"]);
            } else if (data.operation === "Kong") {
                this.kongList = JSON.stringify(data.msg["KongList"]);
            } else if (data.operation === "Chow") {
                this.chowList = JSON.stringify(data.msg["ChowList"]);
            }
        },

        deal(dealPosition, dealID) {
            // 清空之前发的牌
            this.clearTileImages(".Deal")
            // 获取牌的前两位
            const tilePrefix = String(dealID).slice(0, 2);
            if (dealPosition === "self") {
                return new URL(`../assets/tiles-front/${tilePrefix}.png`, import.meta.url).href;
            } else if (dealPosition === "nextPlayer") {
                return new URL(`../assets/tiles-left/handin.png`, import.meta.url).href;
            } else if (dealPosition === "oppositePlayer") {
                return new URL(`../assets/tiles-opposite/handin.png`, import.meta.url).href;
            } else if (dealPosition === "prevPlayer") {
                return new URL(`../assets/tiles-right/handin.png`, import.meta.url).href;
            }
        },

        // 出牌
        discard(discardPosition, discardID) {
            // 清空之前出的牌
            this.clearTileImages(".Discard")
            // 获取牌的前两位
            const tilePrefix = String(discardID).slice(0, 2);
            if (discardPosition === "self") {
                return new URL(`../assets/tiles-me/${tilePrefix}.png`, import.meta.url).href;
            } else if (discardPosition === "nextPlayer") {
                return new URL(`../assets/tiles-left/${tilePrefix}.png`, import.meta.url).href;
            } else if (discardPosition === "oppositePlayer") {
                return new URL(`../assets/tiles-opposite/${tilePrefix}.png`, import.meta.url).href;
            } else if (discardPosition === "prevPlayer") {
                return new URL(`../assets/tiles-right/${tilePrefix}.png`, import.meta.url).href;
            }
        },

        pang() {
            WebSocketService.sendMessage(JSON.stringify({operation: "Pang"}));
            // 清空之前出的牌
            this.clearTileImages(".Discard")
            // 清空吃碰杠按钮
            this.canPang = false;
            this.canKong = false;
            this.canChow = false;
        },

        kong() {
            WebSocketService.sendMessage(JSON.stringify({operation: "Kong"}));
            // 清空之前出的牌
            this.clearTileImages(".Discard")
            // 清空吃碰杠按钮
            this.canKong = false;
            this.canPang = false;
            this.canChow = false;
        },

        selectKong(id){
            WebSocketService.sendMessage(JSON.stringify({operation: "selectKong", tileID: id}));
        },

        chow() {
            WebSocketService.sendMessage(JSON.stringify({operation: "Chow"}));
            // 清空之前出的牌
            this.clearTileImages(".Discard")
            // 清空吃碰杠按钮
            this.canChow = false;
            this.canPang = false;
            this.canKong = false;
        },

        selectChow(list) {
            WebSocketService.sendMessage(JSON.stringify({operation: "selectChow", tileIDList: list}));
        },

        hu() {
            WebSocketService.sendMessage(JSON.stringify({operation: "Hu"}));
            this.canHu = false;
            this.canPang = false;
            this.canKong = false;
            this.canChow = false;
        },

        noHu() {
            WebSocketService.sendMessage(JSON.stringify({operation: "noHu"}));
            this.canHu = false;
        },

        noAffair() {
            WebSocketService.sendMessage(JSON.stringify({operation: "noAffair"}));
            this.canChow = false;
            this.canPang = false;
            this.canKong = false;
        },

        // helper method
        // 清空指定class图片的 URL, className = ".class"
        clearTileImages(className) {
            // 获取所有指定 class 的图片元素
            const tileImages = this.$el.querySelectorAll(className);

            // 遍历这些图片元素，将它们的 src 属性设为空
            tileImages.forEach(img => {
                img.src = '';
            });
        },

        // 根据牌编号生成相应的图片 URL
        getTileUrl(tile) {
            // 获取牌的前两位
            const tilePrefix = String(tile).slice(0, 2);
            return new URL(`../assets/tiles-me/${tilePrefix}.png`, import.meta.url).href;
        }

    },

    // 连接 WebSocket 并注册消息处理回调函数
    created() {
        WebSocketService.addMessageListener(this.handleMessage);
    },

    // 移除消息处理回调函数
    beforeUnmount() {
        WebSocketService.removeMessageListener(this.handleMessage);
    }
}

</script>

<style scoped>
    .background {
        background-image: url('@/assets/table.jpg');
        background-size: cover;
        background-repeat: no-repeat;
        background-position: center;
        position: fixed;
        top:0;
        left:0;
        width: 100%;
        height: 100vh;
    }

    /* my single hand tile */
    .hand {
        width: 38px;
        height: 60px;
    }

    /* opposite single hand tile */
    .opp_hand {
        width: 38px;
        height: 60px;
    }

    .me {
        display: flex;
        justify-content: center; /* 水平居中 */
        align-items: center;    /* 垂直居中 */
        height: 100vh;          /* 满屏高度 */
    }

    /* opposite hand tiles */
    .opposite_hand {
        position: fixed;
        left: 50%;
        top: 20%;
        transform: translate(-50%, 0);
    }

    .left_table {
        display: flex;
        /*flex-direction: column; !* 纵向排列 *!*/
        align-items: center; /* 水平居中对齐，可选 */
        gap: 20px; /* 图片之间的间距 */
        position: fixed;
        left: 10%;
        bottom: 0;
        transform: translate(0, -50%);
    }

    #l_wall {
        width: 80px;
        height: 320px;
        position: fixed;
        left: 20%;
        bottom: 50%;
        transform: translate(0, 50%);
    }

    #r_wall {
        width: 80px;
        height: 320px;
        position: fixed;
        right: 20%;
        bottom: 50%;
        transform: translate(0, 50%);
    }


</style>