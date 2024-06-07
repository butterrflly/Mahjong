<template>
    <div class="background">
        <button v-if="!gameStart" v-on:click="prepare()" class="ready-button">Prepare</button>
        <div v-if="selfName" class="player-name myName">{{this.selfName}}</div>
        <div v-if="selfPrepare" class="player-name myPrepare">{{"Prepared!"}}</div>
        <div v-if="prevName" class="player-name leftName">{{this.prevName}}</div>
        <div v-if="prevPrepare" class="player-name leftPrepare">{{"Prepared!"}}</div>
        <div v-if="oppoName" class="player-name oppoName">{{this.oppoName}}</div>
        <div v-if="oppoPrepare" class="player-name oppoPrepare">{{"Prepared!"}}</div>
        <div v-if="nextName" class="player-name rightName">{{this.nextName}}</div>
        <div v-if="nextPrepare" class="player-name rightPrepare">{{"Prepared!"}}</div>

        <div >
            <div v-if="message" class="info-message">{{ this.message }}</div>
            <div v-if="error" class="error-message">{{ this.error }}</div>
        </div>

        <div class="me">  <!--my tiles-->
            <SelfHandTiles :gameStatus="gameStart"
                           :handTiles="selfHandTiles"
                           :dealID="selfDealID"
                           :canDiscard="canDiscard"/>
            <SelfMeldTiles :selfMeldTiles="selfMeldTiles"
                           :selfIfHideMeld="selfIfHideMeld" />
            <img
                v-if="selfDiscardUrl !== ''"
                :src="selfDiscardUrl"
                alt="selfDiscard"
                class="Discard"
                id="selfDiscard"
            />
            <button v-on:click="hu()" v-if="canHu" class="Hu">Hu</button>
            <button v-on:click="noHu()" v-if="canHu" class="HuSkip">Skip</button>
            <button v-on:click="chow()" v-if="canChow" class="Chow">Chow</button>
            <div>
                <div v-if="chowList.length > 1"
                     v-for="(group, index) in chowList"
                     v-on:click="selectChow(chowList[index])"
                     :key="index" class="chowGroup">
                    <img
                        v-for="(tile, tileIndex) in group"
                        :key="tileIndex"
                        :src="getTileUrl(tile)"
                        alt="chowTile"
                        class="chowTile"
                    />
                </div>
            </div>
            <button v-on:click="pang()" v-if="canPang" class="Pang">Pang</button>
            <button v-on:click="kong()" v-if="canKong" class="Kong">Kong</button>
            <img
                v-if="kongList > 1"
                v-for="tile in kongList"
                :key="tile"
                :src="getTileUrl(tile)"
                alt="kongTile"
                class="kongTile"
                v-on:click="selectKong(tile)"
            />
            <button v-on:click="noPangOrKong()" v-if="canPang||canKong" class="PangKongSkip">Skip</button>
            <button v-on:click="noChow()" v-if="canChow" class="ChowSkip">Skip</button>
        </div>

        <div class="left">  <!--left tiles-->
            <PrevHandTiles :gameStatus="gameStart"
                           :ifDeal="prevDeal"/>
            <PrevMeldTiles :prevMeldTiles="prevMeldTiles"
                           :prevIfHideMeld="prevIfHideMeld"/>
            <img
                v-if="prevDiscardUrl !== ''"
                :src="prevDiscardUrl"
                alt="prevDiscard"
                class="Discard"
                id="prevDiscard"
            />
        </div>

        <div class="opposite">  <!--opposite tiles-->

            <OppoHandTiles :gameStatus="gameStart"
                           :ifDeal="oppoDeal"/>
            <OppoMeldTiles :oppoMeldTiles="oppoMeldTiles"
                           :oppoIfHideMeld="oppoIfHideMeld"/>
            <img
                v-if="oppoDiscardUrl !== ''"
                :src="oppoDiscardUrl"
                alt="oppoDiscard"
                class="Discard"
                id="oppoDiscard"
            />
        </div>

        <div class="right">  <!--right tiles-->

            <NextHandTiles :gameStatus="gameStart"
                           :ifDeal="nextDeal"/>
            <NextMeldTiles :nextMeldTiles="nextMeldTiles"
                           :nextIfHideMeld="nextIfHideMeld"/>
            <img
                v-if="nextDiscardUrl !== ''"
                :src="nextDiscardUrl"
                alt="nextDiscard"
                class="Discard"
                id="nextDiscard"
            />
        </div>

        <div class="tableTiles">
            <TableTiles :gameStatus="gameStart"
                        :tiles="tableTiles"/>
        </div>
    </div>
</template>

<script>
import WebSocketService from '../websocket.js';
import SelfHandTiles from "../components/SelfHandTiles.vue";
import NextHandTiles from "../components/NextHandTiles.vue";
import OppoHandTiles from "../components/OppoHandTiles.vue";
import PrevHandTiles from "../components/PrevHandTiles.vue";
import SelfMeldTiles from "../components/SelfMeldTiles.vue";
import OppoMeldTiles from "../components/OppoMeldTiles.vue";
import PrevMeldTiles from "../components/PrevMeldTiles.vue";
import NextMeldTiles from "../components/NextMeldTiles.vue";
import TableTiles from "../components/TableTiles.vue";


export default {
    name: "GameTable",

    components: {
        TableTiles,
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
            gameStart: false,
            // player name
            selfName:'',
            nextName:'',
            oppoName:'',
            prevName:'',

            // prepare status
            selfPrepare:'',
            nextPrepare:'',
            oppoPrepare:'',
            prevPrepare:'',

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
            tableTiles:[],
            canDiscard:false,
            message:'',
            error:'',

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
                this.selfPrepare = data.msg["self"]["prepare"];
                this.nextPrepare = data.msg["nextPlayer"]["prepare"];
                this.oppoPrepare = data.msg["oppositePlayer"]["prepare"];
                this.prevPrepare = data.msg["prevPlayer"]["prepare"];

            } else if (data.operation === "discard") {
                // 出牌的返回值，包括位置和牌id，用于把出的牌展示在桌面
                this.showDiscard(data.msg["position"], data.msg["tileID"]);
                this.canDiscard = false;  // 已经出过牌，不允许再出牌
                console.log("已经展示过出的牌，canDiscard=" + this.canDiscard)

            } else if (data.operation === "getHandTile") {
                // 游戏开始时，显示所有手牌
                this.gameStart = true;
                this.selfPrepare = this.prevPrepare = this.oppoPrepare = this.nextPrepare = false;
                // 更新手牌时，隐藏发的牌，即将发的牌加入手牌
                this.selfDealID = '';
                this.nextDeal = this.oppoDeal = this.prevDeal = false;
                this.selfHandTiles = data.msg["self"]["handTile"];
                this.nextHandTiles = data.msg["nextPlayer"]["handTile"];
                this.oppoHandTiles = data.msg["oppositePlayer"]["handTile"];
                this.prevHandTiles = data.msg["prevPlayer"]["handTile"];

            } else if (data.operation === "getMeld") {
                this.selfMeldTiles = data.msg["selfMelds"];
                this.nextMeldTiles = data.msg["nextPlayerMelds"];
                this.oppoMeldTiles = data.msg["oppositePlayerMelds"];
                this.prevMeldTiles = data.msg["prevPlayerMelds"];
                console.log("this.selfMeldTiles=" + data.msg["selfMelds"]);
                console.log("this.nextMeldTiles=" + data.msg["nextPlayerMelds"]);
                console.log("this.oppoMeldTiles=" + data.msg["oppositePlayerMelds"]);
                console.log("this.prevMeldTiles=" + data.msg["prevPlayerMelds"]);
                this.selfIfHideMeld = data.msg["self"]["isHide list"];
                this.nextIfHideMeld = data.msg["nextPlayer"]["isHide list"];
                this.oppoIfHideMeld = data.msg["oppositePlayer"]["isHide list"];
                this.prevIfHideMeld = data.msg["prevPlayer"]["isHide list"];

            } else if (data.operation === "deal") {
                this.deal(data.msg["position"], data.msg["tileID"])

            } else if (data.operation === "canHu") {
                this.canHu = data.msg["canHu"];
                if (this.canHu === false) {
                    WebSocketService.sendMessage(JSON.stringify({ operation: 'noHu'}));
                }

            } else if (data.operation === "getPangOrKong") {
                console.log("receive affair")

                this.canPang = data.msg["canPang"];
                this.canKong = data.msg["canKong"];
                if (this.canPang === false && this.canKong === false) {
                    WebSocketService.sendMessage(JSON.stringify({ operation: 'noPangOrKong'}));
                }

            } else if (data.operation === "canChow") {
                this.canChow = data.msg["canChow"];
                if (this.canChow === false) {
                    WebSocketService.sendMessage(JSON.stringify({ operation: 'noChow'}));
                }

            } else if (data.operation === "getSelfAffair") {
                this.canKong = data.msg["canKong"];
                console.log("can self kong status:" + this.canKong);

                this.canHu = data.msg["canHu"];
                console.log("can hu status:" + this.canHu);

            } else if (data.operation === "Hu") {
                this.huName = JSON.stringify(data.msg["playerName"]);

            } else if (data.operation === "Kong") {
                this.kongList = data.msg["KongList"];
                // 一种杠法不选择，直接开杠
                if (this.kongList.length === 1) {
                    this.kong(this.kongList[0])
                }
                console.log ("kong list:" + this.kongList);

            } else if (data.operation === "Chow") {
                this.chowList = data.msg["ChowList"];
                // 一种吃法不选择，直接开吃
                if (this.chowList.length === 1) {
                    this.chow(this.chowList[0])
                }
                console.log("chowList:" + this.chowList);

            } else if (data.operation === "discardRequest") {
                console.log ("receive discard request")
                this.message = JSON.stringify(data.msg);
                this.canDiscard = true;

            } else if (data.operation === "getTableTile") {
                this.tableTiles = data.msg["tableTile"];
                console.log ("table tile=" + this.tableTiles);

            } else if (data.operation === "Draw") {
                this.message = "Draw, game over";
                this.gameStart = false;
            }
        },

        prepare() {
            try {
                WebSocketService.sendMessage(JSON.stringify({ operation: 'prepare'}));
                this.message = "Prepare success! Waiting for other players to ready..."
            } catch (error) {
                console.error('Error during POST:', error);
                this.error = "An error occurred while preparing. Please try again later.";
            }
        },

        // 给玩家发牌，即返回这个位置发的牌的url，对应在手牌组件中展示
        deal(dealPosition, dealID) {
            try{
                // 清空之前出的牌
                this.hideImages("Discard");
                this.message = '';

                if (dealPosition === "self") {
                    this.selfDealID = dealID;
                } else if (dealPosition === "nextPlayer") {
                    this.nextDeal = true;
                } else if (dealPosition === "oppositePlayer") {
                    this.oppoDeal = true;
                } else if (dealPosition === "prevPlayer") {
                    this.prevDeal = true;
                }
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
                    this.nextDiscardUrl = new URL(`../assets/tiles-right/${tilePrefix}.png`, import.meta.url).href;
                } else if (discardPosition === "oppositePlayer") {
                    this.oppoDiscardUrl = new URL(`../assets/tiles-opposite/${tilePrefix}.png`, import.meta.url).href;
                } else if (discardPosition === "prevPlayer") {
                    this.prevDiscardUrl = new URL(`../assets/tiles-left/${tilePrefix}.png`, import.meta.url).href;
                }
                this.message = "Waiting for other players..."
            }catch(error){
                console.log("Error when discard");
            }
        },

        pang() {
            WebSocketService.sendMessage(JSON.stringify({operation: "Pang"}));
            // 清空之前出的牌
            this.hideImages("Discard")
            // 清空吃碰杠按钮
            this.canPang = false;
            this.canKong = false;
            this.canChow = false;
        },

        kong() {
            WebSocketService.sendMessage(JSON.stringify({operation: "Kong"}));
            // 清空之前出的牌
            this.hideImages(".Discard")
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
            console.log("选择吃，发送了吃的操作")
            // 清空之前出的牌
            this.hideImages("Discard")
            // 清空吃碰杠按钮
            this.canChow = false;
            this.canPang = false;
            this.canKong = false;
        },

        selectChow(list) {
            WebSocketService.sendMessage(JSON.stringify({operation: "selectChow", tileIDList: JSON.stringify(list)}));
            console.log("选择了怎么样吃，发送了selectChow，tileIDList=" + list)
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

        noPangOrKong() {
            WebSocketService.sendMessage(JSON.stringify({operation: "noPangOrKong"}));
            this.canPang = false;
            this.canKong = false;
        },

        noChow() {
            WebSocketService.sendMessage(JSON.stringify({operation: "noChow"}));
            this.canChow = false;
        },

        // helper method
        // 隐藏指定class图片
        hideImages(className) {
            // 获取所有指定 class 的图片元素
            const tileImages = this.$el.querySelectorAll(className);

            // 遍历这些图片元素，将它们的 src 属性设为空
            tileImages.forEach(img => {
                img.src = '';
            });
            document.querySelectorAll("." + className).forEach(element => {
                element.classList.add("hidden");
            });
        },


        // 根据牌编号生成相应的图片 URL, 用作自己面前的牌组以及选择吃法
        getTileUrl(tile) {
            // 获取牌的前两位
            const tilePrefix = Math.floor(tile / 10);
            return new URL(`../assets/tiles-me/${tilePrefix}.png`, import.meta.url).href;
        },

    },

    // 注册消息处理回调函数
    mounted() {
        WebSocketService.addMessageListener(this.handleMessage);
        this.hideImages("Discard");
    },

    // 移除消息处理回调函数
    beforeUnmount() {
        WebSocketService.removeMessageListener(this.handleMessage);
    }
}

</script>

<style scoped>
    .background {
        background-image: url('../assets/table.jpg');
        background-size: cover;
        background-repeat: no-repeat;
        background-position: center;
        position: fixed;
        top:0;
        left:0;
        width: 100%;
        height: 100vh;
    }

    /* 玩家名字容器样式 */
    .player-name {
        background-color: rgba(255, 0, 0, 0); /* 使用 rgba() 函数设置透明的背景颜色 */
        color: #ffffff; /* 文本颜色为白色 */
        border-radius: 5px; /* 圆角边框 */
        padding: 10px; /* 内边距 */
        margin: 10px 0; /* 上下外边距 */
        text-align: center; /* 文本居中 */
        font-size: 1.2em; /* 字体大小 */
        font-weight: bold; /* 字体加粗 */
        width: fit-content; /* 宽度自动适应内容 */
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); /* 添加阴影 */
    }

    .ready-button {
        background-color: #007bff; /* 按钮背景颜色 */
        color: #fff; /* 按钮文字颜色 */
        padding: 15px 30px; /* 按钮内边距 */
        border: none; /* 去除边框 */
        border-radius: 5px; /* 圆角边框 */
        font-size: 1.5em; /* 字体大小 */
        font-weight: bold; /* 字体加粗 */
        text-align: center; /* 文本居中 */
        cursor: pointer; /* 鼠标悬停时显示为指针 */
        box-shadow: 0 6px 8px rgba(0, 0, 0, 0.1); /* 阴影效果 */
        transition: 0.1s ease; /* 动画过渡效果 */
        position: fixed;
        top:45%;
        left:45%;
    }
    .ready-button:hover {
        transform: scale(1.1); /* 鼠标悬停时按钮放大为原来的 1.1 倍 */
    }

    .error-message {
        color: red; /* 设置文字颜色为红色 */
        text-align: center; /* 居中对齐文字 */
        font-weight: bold; /* 使文字加粗 */
        background-color: #ffe6e6; /* 设置背景颜色为浅红色 */
        padding: 10px; /* 添加内边距 */
        margin: 10px auto; /* 设置外边距，并使元素居中 */
        border: 1px solid red; /* 添加红色边框 */
        width: fit-content; /* 自动适应内容宽度 */
    }

    .info-message {
        color: #155724; /* 设置文字颜色为深绿色 */
        text-align: center; /* 居中对齐文字 */
        font-weight: bold; /* 使文字加粗 */
        background-color: #d4edda; /* 设置背景颜色为浅绿色 */
        padding: 10px; /* 添加内边距 */
        margin: 10px auto; /* 设置外边距，并使元素居中 */
        border: 1px solid #c3e6cb; /* 添加绿色边框 */
        width: fit-content; /* 自动适应内容宽度 */
        border-radius: 5px; /* 添加圆角 */
    }

    /* 顶部玩家名字 */
    .oppoName {
        position: fixed;
        display: inline-block;
        top: 5%;
        left: 50%;
        transform: translateX(-50%);
    }

    /* 底部玩家名字 */
    .myName {
        position: fixed;
        display: inline-block;
        bottom: -1%;
        left: 50%;
        transform: translateX(-50%);
    }

    /* 左侧玩家名字 */
    .leftName {
        position: fixed;
        display: inline-block;
        top: 50%;
        left: 5%;
        transform: translateY(-50%);
    }

    /* 右侧玩家名字 */
    .rightName {
        position: fixed;
        display: inline-block;
        top: 50%;
        right: 5%;
        transform: translateY(-50%);
    }

    /* top player */
    .opposite {
        top: 5%;
        left: 50%;
    }

    /* bottom player */
    .me {
        bottom: 5%;
        left: 50%;
    }

    /* left player */
    .left {
        top: 50%;
        left: 5%;
    }

    /* right player */
    .right {
        top: 50%;
        right: 5%;
    }

    .myPrepare {
        position: fixed;
        display: inline-block;
        bottom: 10px;
        left: 60%;
        transform: translateX(-50%);
    }

    .leftPrepare {
        position: fixed;
        display: inline-block;
        top: 60%;
        left: 10px;
        transform: translateY(-50%);
    }

    .rightPrepare {
        position: fixed;
        display: inline-block;
        top: 40%;
        right: 10px;
        transform: translateY(-50%);
    }

    .oppoPrepare {
        position: fixed;
        display: inline-block;
        top: 10px;
        left: 60%;
        transform: translateX(-50%);
    }

    .tableTiles {
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
    }

    .Chow {
        background-color: #007bff; /* 按钮背景颜色 */
        color: #fff; /* 按钮文字颜色 */
        padding: 5px 10px; /* 按钮内边距 */
        border: none; /* 去除边框 */
        border-radius: 5px; /* 圆角边框 */
        font-size: 1.5em; /* 字体大小 */
        font-weight: bold; /* 字体加粗 */
        text-align: center; /* 文本居中 */
        cursor: pointer; /* 鼠标悬停时显示为指针 */
        box-shadow: 0 6px 8px rgba(0, 0, 0, 0.1); /* 阴影效果 */
        transition: 0.1s ease; /* 动画过渡效果 */
        position: fixed;
        top:70%;
        left:45%;
    }
    .Chow:hover {
        transform: scale(1.1); /* 鼠标悬停时按钮放大为原来的 1.1 倍 */
    }

    .Pang {

    }
    .Pang:hover {
        transform: scale(1.1); /* 鼠标悬停时按钮放大为原来的 1.1 倍 */
    }

    .Kong {

    }
    .Kong:hover {
        transform: scale(1.1); /* 鼠标悬停时按钮放大为原来的 1.1 倍 */
    }

    .Hu {

    }
    .Hu:hover {
        transform: scale(1.1); /* 鼠标悬停时按钮放大为原来的 1.1 倍 */
    }

    .HuSkip {

    }
    .HuSkip:hover {
        transform: scale(1.1); /* 鼠标悬停时按钮放大为原来的 1.1 倍 */
    }

    .PangKongSkip {

    }
    .PangKongSkip:hover {
        transform: scale(1.1); /* 鼠标悬停时按钮放大为原来的 1.1 倍 */
    }

    .ChowSkip {

    }
    .ChowSkip:hover {
        transform: scale(1.1); /* 鼠标悬停时按钮放大为原来的 1.1 倍 */
    }

    .hidden {
        display: none;
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
        right: 30%;
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
        left: 30%;
        top: 50%;
        transform: translate(0, -50%);
        width: 59px;
        height: 48px;
    }
</style>