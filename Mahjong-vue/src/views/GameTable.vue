<template>
    <div class="background">
        <button v-if="!gameStart" v-on:click="prepare()" class="ready-button">Prepare</button>

      <!-- name and prepare status of four player-->
        <div v-if="selfName" class="player-name myName">{{this.selfName}}</div>
        <div v-if="selfPrepare" class="player-name myPrepare">{{"Prepared!"}}</div>
        <div v-if="selfScore" class="player-name myScore">{{this.selfScore}}</div>

        <div v-if="prevName" class="player-name leftName">{{this.prevName}}</div>
        <div v-if="prevPrepare" class="player-name leftPrepare">{{"Prepared!"}}</div>
        <div v-if="prevScore" class="player-name leftScore">{{this.selfScore}}</div>

        <div v-if="oppoName" class="player-name oppoName">{{this.oppoName}}</div>
        <div v-if="oppoPrepare" class="player-name oppoPrepare">{{"Prepared!"}}</div>
        <div v-if="oppoScore" class="player-name oppoScore">{{this.selfScore}}</div>

        <div v-if="nextName" class="player-name rightName">{{this.nextName}}</div>
        <div v-if="nextPrepare" class="player-name rightPrepare">{{"Prepared!"}}</div>
        <div v-if="nextScore" class="player-name rightScore">{{this.selfScore}}</div>

        <!-- display message -->
        <div >
            <div v-if="message" class="info-message">{{ this.message }}</div>
            <div v-if="error" class="error-message">{{ this.error }}</div>
        </div>

        <div class="me">  <!--my tiles-->
            <SelfHandTiles :gameStatus="gameStart"
                           :handTiles="selfHandTiles"
                           :dealID="selfDealID"
                           :canDiscard="canDiscard"/>
            <SelfMeldTiles :gameStatus="gameStart"
                           :selfMeldTiles="selfMeldTiles"
                           :selfIfHideMeld="selfIfHideMeld" />

            <!-- my discard -->
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

            <!-- select how to chow -->
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

            <!-- select how to kong -->
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
            <PrevMeldTiles :gameStatus="gameStart"
                           :prevMeldTiles="prevMeldTiles"
                           :prevIfHideMeld="prevIfHideMeld"/>

           <!-- left discard -->
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
            <OppoMeldTiles :gameStatus="gameStart"
                           :oppoMeldTiles="oppoMeldTiles"
                           :oppoIfHideMeld="oppoIfHideMeld"/>

            <!-- opposite discard -->
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
            <NextMeldTiles :gameStatus="gameStart"
                           :nextMeldTiles="nextMeldTiles"
                           :nextIfHideMeld="nextIfHideMeld"/>

            <!-- right discard -->
            <img
                v-if="nextDiscardUrl !== ''"
                :src="nextDiscardUrl"
                alt="nextDiscard"
                class="Discard"
                id="nextDiscard"
            />
        </div>

        <!-- tiles displayed on the table -->
        <div class="tableTiles">
            <TableTiles :gameStatus="gameStart"
                        :tiles="tableTiles"/>
        </div>

        <!-- tiles of hu player-->
        <div class="huPlayerTiles">
            <HuPlayerTiles :meld-tiles="huMeldTiles"
                           :hand-tiles="huHandTiles"
                           :game-status="gameStart"/>
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
import HuPlayerTiles from "../components/HuPlayerTiles.vue";


export default {
    name: "GameTable",

    components: {
      HuPlayerTiles,
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

            // score
            selfScore:null,
            nextScore:null,
            oppoScore:null,
            prevScore:null,

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

            // if can hu
            canHu:false,
            canChow:false,
            canPang:false,
            canKong:false,

            // url to show discard tiles
            selfDiscardUrl:'',
            nextDiscardUrl:'',
            oppoDiscardUrl:'',
            prevDiscardUrl:'',

            // control the display of deal tiles
            selfDealID:null,
            nextDeal:false,
            oppoDeal:false,
            prevDeal:false,

            // information of hu player
            huName:'',
            huPosition:'',
            huHandTiles: '',
            huMeldTiles: '',

            // to display the way of kong and chow
            kongList:[],  // [TileID_1, TileID_2, ...]
            chowList:[],  // [[TileID_1, TileID_2, TileID_3], ...]

            tableTiles:[],

            // control the discard of player
            canDiscard:false,

            // messages to display
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
                this.selfScore = data.msg["self"]["score"];
                this.nextScore = data.msg["nextPlayer"]["score"];
                this.oppoScore = data.msg["oppositePlayer"]["score"];
                this.prevScore = data.msg["prevPlayer"]["score"];

            } else if (data.operation === "discard") {
                // The return value of the discard,
                // including the position and tile id,
                // is used to display the tiles that has been discarded on the table.
                this.showDiscard(data.msg["position"], data.msg["tileID"]);
                this.canDiscard = false;  // Tiles have already been dealt, no more discard are permitted

            } else if (data.operation === "getHandTile") {
                this.selfPrepare = this.prevPrepare = this.oppoPrepare = this.nextPrepare = false;
                // When updating hand tiles, hide tiles that has been discarded
                // i.e. add the discard tiles to hand tiles
                this.selfDealID = '';
                this.nextDeal = this.oppoDeal = this.prevDeal = false;
                this.selfHandTiles = data.msg["self"]["handTile"];
                this.nextHandTiles = data.msg["nextPlayer"]["handTile"];
                this.oppoHandTiles = data.msg["oppositePlayer"]["handTile"];
                this.prevHandTiles = data.msg["prevPlayer"]["handTile"];
                // if someone hu, get his hand tiles
                if (this.huPosition !== '') {
                  if (this.huPosition === "self") {
                      this.huHandTiles = data.msg["self"]["handTile"];
                  } else if (this.huPosition === "nextPlayer") {
                      this.huHandTiles = data.msg["nextPlayer"]["handTile"];
                  } else if (this.huPosition === "nextPlayer") {
                      this.huHandTiles = data.msg["oppositePlayer"]["handTile"];
                  } else if (this.huPosition === "prevPlayer") {
                      this.huHandTiles = data.msg["prevPlayer"]["handTile"];
                  }

                }

            } else if (data.operation === "getMeld") {
                this.selfMeldTiles = data.msg["selfMelds"];
                this.nextMeldTiles = data.msg["nextPlayerMelds"];
                this.oppoMeldTiles = data.msg["oppositePlayerMelds"];
                this.prevMeldTiles = data.msg["prevPlayerMelds"];
                this.selfIfHideMeld = data.msg["self"]["isHide list"];
                this.nextIfHideMeld = data.msg["nextPlayer"]["isHide list"];
                this.oppoIfHideMeld = data.msg["oppositePlayer"]["isHide list"];
                this.prevIfHideMeld = data.msg["prevPlayer"]["isHide list"];
                // if someone hu, get his meld tiles
                if (this.huPosition !== '') {
                  if (this.huPosition === "self") {
                    this.huMeldTiles = data.msg["selfMelds"];
                  } else if (this.huPosition === "nextPlayer") {
                    this.huHandTiles = data.msg["nextPlayerMelds"];
                  } else if (this.huPosition === "oppositePlayer") {
                    this.huHandTiles = data.msg["oppositePlayerMelds"];
                  } else if (this.huPosition === "prevPlayer") {
                    this.huHandTiles = data.msg["prevPlayerMelds"];
                  }
                }

            } else if (data.operation === "deal") {
                // At the start of the game, show all tiles
                this.gameStart = true;
                this.deal(data.msg["position"], data.msg["tileID"])

            } else if (data.operation === "canHu") {
                this.canHu = data.msg["canHu"];
                // automatically judge and send noHu
                if (this.canHu === false) {
                    WebSocketService.sendMessage(JSON.stringify({ operation: 'noHu'}));
                } else {
                  this.message = "Choose to Hu or not"
                }

            } else if (data.operation === "getPangOrKong") {
                this.canPang = data.msg["canPang"];
                this.canKong = data.msg["canKong"];
                // automatically judge and send noPangOrKong
                if (this.canPang === false && this.canKong === false) {
                    WebSocketService.sendMessage(JSON.stringify({ operation: 'noPangOrKong'}));
                } else if (this.canPang === true) {
                    this.message = "Choose to Pang or not"
                } else if (this.canKong ===  true) {
                  this.message = "Choose to Kong or not"
                }

            } else if (data.operation === "canChow") {
                this.canChow = data.msg["canChow"];
                // automatically judge and send noChow
                if (this.canChow === false) {
                    WebSocketService.sendMessage(JSON.stringify({ operation: 'noChow'}));
                } else {
                    this.message = "Choose to Chow or not"
                }

            } else if (data.operation === "getSelfAffair") {
                this.canKong = data.msg["canKong"];
                this.canHu = data.msg["canHu"];

            } else if (data.operation === "Hu") {
                this.huName = JSON.stringify(data.msg["playerName"]);
                this.huPosition = JSON.stringify(data.msg["position"]);
                this.message = this.huName + "has HU!!!!!"
                this.gameStart = false;
                this.restart();

            } else if (data.operation === "Kong") {
                this.kongList = data.msg["KongList"];
                // Only one situation, no selection
                if (this.kongList.length === 1) {
                    this.selectKong(this.kongList[0])
                } else {
                  this.message = "Please choose a way to Kong:"
                }

            } else if (data.operation === "Chow") {
                this.chowList = data.msg["ChowList"];
                // Only one situation, no selection
                if (this.chowList.length === 1) {
                    this.selectChow(this.chowList[0])
                } else {
                    this.message = "Please choose a way to Chow:"
                }

            } else if (data.operation === "discardRequest") {
                // can discard
                this.message = JSON.stringify(data.msg);
                this.canDiscard = true;

            } else if (data.operation === "getTableTile") {
                this.tableTiles = data.msg["tableTile"];

            } else if (data.operation === "Draw") {
                this.message = "Draw, game over";
                this.restart();
            }
        },

        // prepare, send prepare message and display ready message
        prepare() {
            try {
                WebSocketService.sendMessage(JSON.stringify({ operation: 'prepare'}));
                this.message = "Prepare success! Waiting for other players to ready..."
            } catch (error) {
                console.error('Error during POST:', error);
                this.error = "An error occurred while preparing. Please try again later.";
            }
        },

        // Deal the tiles to the player,
        // i.e. return the url of the tiles discarded in this position,
        // corresponding to the display in the handTiles component
        deal(dealPosition, dealID) {
            try{
                // Clear tiles that has been discarded
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

        // Display the discarded tiles on the desk,
        // i.e. return a discardUrl
        showDiscard(discardPosition, discardID) {
            try{
                // Getting the first two number of tileID
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
            // Clear buttons of chow, pang and kong
            this.canPang = false;
            this.canKong = false;
            this.canChow = false;
        },

        kong() {
            WebSocketService.sendMessage(JSON.stringify({operation: "Kong"}));
            // Clear buttons of chow, pang and kong
            this.canKong = false;
            this.canPang = false;
            this.canChow = false;
        },

        selectKong(id){
            WebSocketService.sendMessage(JSON.stringify({operation: "selectKong", tileID: id}));
            // Clear buttons of chow, pang and kong
            // Clear tiles that has been discarded
            this.hideImages("Discard")
            this.canChow = false;
            this.canPang = false;
            this.canKong = false;
            this.kongList = ''  // hide the chosen menu
        },

        chow() {
            WebSocketService.sendMessage(JSON.stringify({operation: "Chow"}));
            // Clear buttons of chow, pang and kong
            this.canChow = false;
            this.canPang = false;
            this.canKong = false;
        },

        selectChow(list) {
            WebSocketService.sendMessage(JSON.stringify({operation: "selectChow", tileIDList: JSON.stringify(list)}));
            // Clear tiles that has been discarded
            this.hideImages("Discard")
            // Clear buttons of chow, pang and kong
            this.canChow = false;
            this.canPang = false;
            this.canKong = false;
            this.chowList = '';  // hide the chosen menu

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
            // Clear tiles that has been discarded
            this.hideImages("Discard")
            this.canPang = false;
            this.canKong = false;
        },

        noChow() {
            WebSocketService.sendMessage(JSON.stringify({operation: "noChow"}));
            // Clear tiles that has been discarded
            this.hideImages("Discard")
            this.canChow = false;
        },

        restart() {
            this.gameStart = false;
            this.tableTiles = [];
            this.selfHandTiles = this.nextHandTiles = this.oppoHandTiles = this.prevHandTiles = [];
            this.selfPrepare = this.nextPrepare = this.oppoPrepare = this.prevPrepare = false;
            this.selfMeldTiles = this.nextMeldTiles = this.oppoMeldTiles = this.prevMeldTiles = [];
            this.canHu = false;
            this.canChow = false;
            this.canPang = false;
            this.canKong = false;
            this.selfDiscardUrl = '';
            this.nextDiscardUrl = '';
            this.oppoDiscardUrl = '';
            this.prevDiscardUrl = '';
            this.selfDealID = null;
            this.nextDeal = false;
            this.oppoDeal = false;
            this.prevDeal = false;
            this.kongList = [];
            this.chowList = [];
            this.canDiscard = false;
        },

        // helper method
        // hide the img of specified class name
        hideImages(className) {
            // Get all image elements of the specified class name
            const tileImages = this.$el.querySelectorAll(className);

            // Iterate over these image elements, setting their src attribute to null
            tileImages.forEach(img => {
                img.src = '';
            });
        },


        // Generate an image URL based on the tileID,
        // which can be used for the tiles wall in front of self and for choosing how to chow.
        getTileUrl(tile) {
            // get first two numbers of tileID
            const tilePrefix = Math.floor(tile / 10);
            return new URL(`../assets/tiles-me/${tilePrefix}.png`, import.meta.url).href;
        },

    },

    // Registering Message Handling Callback Function
    mounted() {
        WebSocketService.addMessageListener(this.handleMessage);
        this.hideImages("Discard");
    },

    // Remove the Message Handling Callback Function
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

    /* player name container style */
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

    /* ready button style */
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

    /* error message style */
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

    /* info message style */
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

    /* top player name */
    .oppoName {
        position: fixed;
        display: inline-block;
        top: 5%;
        left: 50%;
        transform: translateX(-50%);
    }

    /* bottom player name */
    .myName {
        position: fixed;
        display: inline-block;
        bottom: -1%;
        left: 50%;
        transform: translateX(-50%);
    }

    /* left player name */
    .leftName {
        position: fixed;
        display: inline-block;
        top: 50%;
        left: 5%;
        transform: translateY(-50%);
    }

    /* right player name */
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
        bottom: 18%;
        left: 45%;
    }
    .Chow:hover {
        transform: scale(1.1); /* 鼠标悬停时按钮放大为原来的 1.1 倍 */
    }

    .ChowSkip {
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
      bottom: 18%;
      left: 53%;
    }
    .ChowSkip:hover {
      transform: scale(1.1); /* 鼠标悬停时按钮放大为原来的 1.1 倍 */
    }

    .Pang {
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
        bottom:18%;
        left:60%;
    }
    .Pang:hover {
        transform: scale(1.1); /* 鼠标悬停时按钮放大为原来的 1.1 倍 */
    }

    .Kong {
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
        bottom: 18%;
        left: 70%;
    }
    .Kong:hover {
        transform: scale(1.1); /* 鼠标悬停时按钮放大为原来的 1.1 倍 */
    }

    .PangKongSkip {
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
      bottom: 18%;
      left: 65%;
    }
    .PangKongSkip:hover {
      transform: scale(1.1); /* 鼠标悬停时按钮放大为原来的 1.1 倍 */
    }

    .Hu {
        background-color: #e30606; /* 按钮背景颜色 */
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
        bottom: 18%;
        left: 40%;
    }
    .Hu:hover {
        transform: scale(1.1); /* 鼠标悬停时按钮放大为原来的 1.1 倍 */
    }

    .HuSkip {
      background-color: #e30606; /* 按钮背景颜色 */
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
      bottom: 18%;
      left: 50%;
    }
    .HuSkip:hover {
        transform: scale(1.1); /* 鼠标悬停时按钮放大为原来的 1.1 倍 */
    }

    .hidden {
        display: none;
    }

    #selfDiscard {
        position: fixed;
        left: 50%;
        bottom: 28%;
        transform: translate(-50%, 0);
        width: 36px;
        height: 50px;
    }

    #nextDiscard {
        position: fixed;
        right: 33%;
        top: 50%;
        transform: translate(0, -50%);
        width: 43px;
        height: 33px;
    }

    #oppoDiscard {
        position: fixed;
        left: 50%;
        top: 33%;
        transform: translate(-50%, 0);
        width: 35px;
        height: 50px;
    }

    #prevDiscard {
        position: fixed;
        left: 32%;
        top: 50%;
        transform: translate(0, -50%);
        width: 43px;
        height: 33px;
    }
</style>