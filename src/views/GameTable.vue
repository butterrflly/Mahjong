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
        </div>
        <div class="left">  <!--left tiles-->
            <NextHandTiles />
            <img
                :src="deal()"
                alt="nextDeal"
                class="Deal"
                id="nextDeal"
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
        </div>
        <div class="right">  <!--right tiles-->
            <PrevHandTiles />
            <img
                :src="deal()"
                alt="rightDeal"
                class="Deal"
                id="oppoDeal"
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


export default {
    name: "GameTable",

    components: {
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

            canHuMsg:'',
            affairMsg:'',
            message:'',
        }
    },

    methods:{
        // handle the data received by listener
        handleMessage(data) {
            if (data.operation === "getRoomPlayerMessage") {
                this.selfName = data.msg["self"]["name"];
                this.nextName = data.msg["nextPlayer"]["name"];
                this.oppoName = data.msg["oppositePlayer"]["name"];
                this.prevName = data.msg["prevPlayer"]["name"];
            } else if (data.operation === "discard") {
                this.discardPosition = data.msg["position"];
                this.discardID = data.msg["tileID"];
            } else if (data.operation === "getHandTile") {
                this.selfHandTiles = data.msg["self"]["handTile"];
                this.nextHandTiles = data.msg["nextPlayer"]["handTile"];
                this.oppoHandTiles = data.msg["oppositePlayer"]["handTile"];
                this.prevHandTiles = data.msg["prevPlayer"]["handTile"];
            } else if (data.operation === "getMeld") {
                this.selfMeldTiles = data.msg["self"]["melds"];
                this.nextMeldTiles = data.msg["nextPlayer"]["melds"];
                this.oppoMeldTiles = data.msg["oppositePlayer"]["melds"];
                this.prevMeldTiles = data.msg["prevPlayer"]["melds"];
                this.selfIfHideMeld = data.msg["self"]["isHide list"];
                this.nextIfHideMeld = data.msg["nextPlayer"]["isHide list"];
                this.oppoIfHideMeld = data.msg["oppositePlayer"]["isHide list"];
                this.prevIfHideMeld = data.msg["prevPlayer"]["isHide list"];
            } else if (data.operation === "deal") {
                this.deal(data.msg["position"], data.msg["tileID"])
            } else if (data.operation === "canHu") {
                this.canHuMsg = data.msg;
            } else if (data.operation === "getAffair") {
                this.affairMsg = data.msg;
            }
        },

        deal(dealPosition, dealID){
            // 清空之前发的牌
            this.clearTileImages(".deal")
            // 获取牌的前两位
            const tilePrefix = String(dealID).slice(0, 2);
            if (dealPosition === "self") {
                return new URL(`../assets/tiles-front/${tilePrefix}.png`, import.meta.url).href;
            } else if (dealPosition === "nextPlayer") {
                return new URL(`../assets/tiles-left/${tilePrefix}.png`, import.meta.url).href;
            } else if (dealPosition === "oppositePlayer") {
                return new URL(`../assets/tiles-opposite/${tilePrefix}.png`, import.meta.url).href;
            } else if (dealPosition === "prevPlayer") {
                return new URL(`../assets/tiles-right/${tilePrefix}.png`, import.meta.url).href;
            }
        },

        // 出牌
        async discard(owner, name, id){
            try {
                const response = await postData('/game/discard', { owner: owner, name: name, tileID: id});
                console.log('Response from POST:', response);
            } catch (error) {
                console.error('Error during POST:', error);
            }
        },

        // return boolean
        async canPang(owner, name){

            try {
                // 是否可以碰
                const response = await postData('/game/canPang', { owner: owner, name: name, tileID: id});
                console.log('Response from POST:', response);
                return response;
            } catch (error) {
                console.error('Error during POST:', error);
            }
        },

        // 碰牌
        async pang(owner, name, id){
            try {
                // 是否可以碰
                const response = await postData('/game/canPang', { owner: owner, name: name, tileID: id});
                console.log('Response from POST:', response);
            } catch (error) {
                console.error('Error during POST:', error);
            }
        },

        async kong(owner, name, id){

        },

        async chow(owner, name, id){

        },

        async hu(owner, name, id){

        },

        async draw(owner, name){

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