<template>
    <div class="background">
        <div >
            <button v-on:click="prepare()">Prepare</button>
<!--            &lt;!&ndash; 在 errorMessage 不为空时显示错误消息 &ndash;&gt;-->
<!--            <div v-if="error" class="error-message">{{ this.error }}</div>-->
<!--            &lt;!&ndash; 在 successMessage 不为空时显示成功消息 &ndash;&gt;-->
<!--            <div v-if="message" class="success-message">{{ this.message }}</div>-->
        </div>
        <div class="me">  <!--my tiles-->
            <SelfHandTiles :handTiles="selfHandTiles" :dealID="selfDealID" :canDiscard="canDiscard"/>
            <SelfMeldTiles :selfMeldTiles="selfMeldTiles" :selfIfHideMeld="selfIfHideMeld" />
            <img
                :src="selfDiscardUrl"
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
            <button v-on:click="noAffair()" v-if="canChow||canPang||canKong">Skip</button>
        </div>
        <div class="left">  <!--left tiles-->
            <NextHandTiles :ifDeal="nextDeal"/>
            <NextMeldTiles :nextMeldTiles="nextMeldTiles" :nextIfHideMeld="nextIfHideMeld"/>
            <img
                :src="nextDiscardUrl"
                alt="nextDiscard"
                class="Discard"
                id="nextDiscard"
            />
        </div>
        <div class="opposite">  <!--opposite tiles-->
            <OppoHandTiles :ifDeal="oppoDeal"/>
            <OppoMeldTiles :oppoMeldTiles="oppoMeldTiles" :oppoIfHideMeld="oppoIfHideMeld"/>
            <img
                :src="oppoDiscardUrl"
                alt="oppoDiscard"
                class="Discard"
                id="oppoDiscard"
            />
        </div>
        <div class="right">  <!--right tiles-->
            <PrevHandTiles :ifDeal="prevDeal"/>
            <PrevMeldTiles :prevMeldTiles="prevMeldTiles" :prevIfHideMeld="prevIfHideMeld"/>
            <img
                :src="prevDiscardUrl"
                alt="prevDiscard"
                class="Discard"
                id="prevDiscard"
            />
        </div>
    </div>
</template>

<script>
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
            discardID: null,
            dealID: null,
            // hand tiles of each player, [int, int, ...]
            selfHandTiles:[],
            nextHandTiles:[],
            oppoHandTiles:[],
            prevHandTiles:[],
            // meld tiles of each player, 2D array
            // [[int, int, int], ...]
            selfMeldTiles:[],
            nextMeldTiles:[],
            oppoMeldTiles:[],
            prevMeldTiles:[],
            // if meld tiles hide, a boolean for a group
            // [boolean, boolean, ...]
            selfIfHideMeld:[],
            nextIfHideMeld:[],
            oppoIfHideMeld:[],
            prevIfHideMeld:[],

            canHu:false,
            canChow:false,
            canPang:false,
            canKong:false,

            ifHu:false,
            ifChow:false,
            ifPang:false,
            ifKong:false,

            selfDiscardUrl:'',
            nextDiscardUrl:'',
            oppoDiscardUrl:'',
            prevDiscardUrl:'',

            selfDealID:null,
            nextDeal:false,
            oppoDeal:false,
            prevDeal:false,

            huName:'',
            kongList:[],  // [TileID_1, TileID_2, ...]
            chowList:[],  // [[TileID_1, TileID_2, TileID_3], ...]
            canDiscard:false,
            message:'',
            // error:'',

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
                // 出牌的返回值，包括位置和牌id，用于把出的牌展示在桌面
                this.showDiscard(data.msg["position"], data.msg["tileID"]);
                this.canDiscard = false;  // 已经出过牌，不允许再出牌
            } else if (data.operation === "getHandTile") {
                this.selfDealID = '';
                this.nextDeal = this.oppoDeal = this.prevDeal = false;
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
                // this.deal(data.msg["position"], data.msg["tileID"])
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
            } else if (data.operation === "discardRequest") {
                this.message = JSON.stringify(data.msg);
                this.canDiscard = true;
            }
        },

        prepare() {
            try {
                WebSocketService.sendMessage(JSON.stringify({ operation: 'prepare'}));
                this.message = "Prepare success!"
            } catch (error) {
                console.error('Error during POST:', error);
                this.message = "An error occurred while preparing. Please try again later.";
            }
        },

        // 给玩家发牌，即返回这个位置发的牌的url，对应在手牌组件中展示
        deal(dealPosition, dealID) {
            try{
                // 清空之前出的牌
                this.clearTileImages(".Discard")

                if (dealPosition === "self") {
                    this.selfDealID = dealID;
                } else if (dealPosition === "nextPlayer") {
                    this.nextDeal = true;
                } else if (dealPosition === "oppositePlayer") {
                    this.oppoDeal = true;
                } else if (dealPosition === "prevPlayer") {
                    this.prevDeal = true;
                }
                console.log("this.selfDealID:" + this.selfDealID)
                console.log("this.nextDeal:" + this.nextDeal)
                console.log("this.oppoDeal:" + this.oppoDeal)
                console.log("this.prevDeal:" + this.prevDeal)
            }catch(error){
                console.log("Error when deal");
            }

        },

        // 把出的牌展示在桌面上，即返回一个discardUrl
        showDiscard(discardPosition, discardID) {
            try{
                // 获取牌的前两位
                const tilePrefix = Math.floor(discardID / 10);
                if (discardPosition === "self") {
                    this.selfDiscardUrl =  new URL(`../assets/tiles-me/${tilePrefix}.png`, import.meta.url).href;
                } else if (discardPosition === "nextPlayer") {
                    this.nextDiscardUrl = new URL(`../assets/tiles-left/${tilePrefix}.png`, import.meta.url).href;
                } else if (discardPosition === "oppositePlayer") {
                    this.oppoDiscardUrl = new URL(`../assets/tiles-opposite/${tilePrefix}.png`, import.meta.url).href;
                } else if (discardPosition === "prevPlayer") {
                    this.prevDiscardUrl = new URL(`../assets/tiles-right/${tilePrefix}.png`, import.meta.url).href;
                }
            }catch(error){
                console.log("Error when discard");
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
    mounted() {
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

    .error-message {
        color: red; /* 设置文字颜色为红色 */
        text-align: center; /* 居中对齐文字 */
        font-weight: bold; /* 使文字加粗（可选） */
        background-color: #ffe6e6; /* 设置背景颜色为浅红色（可选） */
        padding: 10px; /* 添加内边距 */
        margin: 10px auto; /* 设置外边距，并使元素居中 */
        border: 1px solid red; /* 添加红色边框（可选） */
        width: fit-content; /* 自动适应内容宽度 */
    }

    .success-message {

    }

    #selfDiscard {
        position: fixed;
        left: 50%;
        bottom: 30%;
        transform: translate(-50%, 0);
        width: 46px;
        height: 64px;
    }

    #nextDiscard {
        position: fixed;
        left: 30%;
        top: 50%;
        transform: translate(0, -50%);
        width: 59px;
        height: 48px;
    }

    #oppoDiscard {
        position: fixed;
        left: 50%;
        top: 30%;
        transform: translate(-50%, 0);
        width: 42px;
        height: 60px;
    }

    #prevDiscard {
        position: fixed;
        right: 30%;
        top: 50%;
        transform: translate(0, -50%);
        width: 59px;
        height: 48px;
    }

    /*!* my single hand tile *!*/
    /*.hand {*/
    /*    width: 38px;*/
    /*    height: 60px;*/
    /*}*/

    /*!* opposite single hand tile *!*/
    /*.opp_hand {*/
    /*    width: 38px;*/
    /*    height: 60px;*/
    /*}*/

    /*.me {*/
    /*    display: flex;*/
    /*    justify-content: center; !* 水平居中 *!*/
    /*    align-items: center;    !* 垂直居中 *!*/
    /*    height: 100vh;          !* 满屏高度 *!*/
    /*}*/

    /*!* opposite hand tiles *!*/
    /*.opposite_hand {*/
    /*    position: fixed;*/
    /*    left: 50%;*/
    /*    top: 20%;*/
    /*    transform: translate(-50%, 0);*/
    /*}*/

    /*.left_table {*/
    /*    display: flex;*/
    /*    !*flex-direction: column; !* 纵向排列 *!*!*/
    /*    align-items: center; !* 水平居中对齐，可选 *!*/
    /*    gap: 20px; !* 图片之间的间距 *!*/
    /*    position: fixed;*/
    /*    left: 10%;*/
    /*    bottom: 0;*/
    /*    transform: translate(0, -50%);*/
    /*}*/

    /*#l_wall {*/
    /*    width: 80px;*/
    /*    height: 320px;*/
    /*    position: fixed;*/
    /*    left: 20%;*/
    /*    bottom: 50%;*/
    /*    transform: translate(0, 50%);*/
    /*}*/

    /*#r_wall {*/
    /*    width: 80px;*/
    /*    height: 320px;*/
    /*    position: fixed;*/
    /*    right: 20%;*/
    /*    bottom: 50%;*/
    /*    transform: translate(0, 50%);*/
    /*}*/


</style>