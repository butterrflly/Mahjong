<template>
    <div class="background">
        <div class="me">  <!--my tiles-->
            <div class="my_hand">
                <img alt="f-1" class="hand" id="f-1" src="../assets/tiles-front/11.png"/>
                <img alt="f-2" class="hand" id="f-2" src="../assets/tiles-front/12.png"/>
                <img alt="f-3" class="hand" id="f-3" src="../assets/tiles-front/13.png"/>
                <img alt="f-4" class="hand" id="f-4" src="../assets/tiles-front/14.png"/>
                <img alt="f-5" class="hand" id="f-5" src="../assets/tiles-front/15.png"/>
                <img alt="f-6" class="hand" id="f-6" src="../assets/tiles-front/16.png"/>
                <img alt="f-7" class="hand" id="f-7" src="../assets/tiles-front/17.png"/>
                <img alt="f-8" class="hand" id="f-8" src="../assets/tiles-front/18.png"/>
                <img alt="f-9" class="hand" id="f-9" src="../assets/tiles-front/19.png"/>
                <img alt="f-10" class="hand" id="f-10" src="../assets/tiles-front/21.png"/>
                <img alt="f-11" class="hand" id="f-11" src="../assets/tiles-front/22.png"/>
                <img alt="f-12" class="hand" id="f-12" src="../assets/tiles-front/23.png"/>
                <img alt="f-13" class="hand" id="f-13" src="../assets/tiles-front/24.png"/>
            </div>
            <div class="deal" v-if="currentPlayer = 0">
                <img alt="deal" id="deal_0" src=""/>
            </div>
            <div class="discard" v-if="currentPlayer = 0 & deal(owner, order[0])">
                 <img alt="discard" id="discard_0" src=""/>
            </div>
            <div class="pang" v-if="currentPlayer = 0 & canPang(owner, order[0])">

            </div>
        </div>
        <div class="left">  <!--left tiles-->
            <img alt="left" id="l_wall" src="../assets/tiles-left/l_wall.png"/>
            <div class="deal" v-if="currentPlayer = 1">
                <img alt="deal" id="deal_1" src=""/>
            </div>
            <div class="discard" v-if="currentPlayer = 1 & deal(owner, order[1])">
                <img alt="discard" id="discard_1" src=""/>
            </div>
            <div class="left_table">  <!--left table-tiles-->
                <div>  <!--stack 1-->
                    <img alt="l-1" class="hand" id="l-1" src="../assets/tiles-left/l_back.png"/>
                    <img alt="l-2" class="hand" id="l-2" src="../assets/tiles-left/l_back.png"/>
                    <img alt="l-3" class="hand" id="l-3" src="../assets/tiles-left/l_back.png"/>
                    <img alt="l-4" class="hand" id="l-4" src="../assets/tiles-left/l_back.png"/>
                </div>
                <div>  <!--stack 2-->
                    <img alt="l-5" class="hand" id="l-5" src="../assets/tiles-left/l_back.png"/>
                    <img alt="l-6" class="hand" id="l-6" src="../assets/tiles-left/l_back.png"/>
                    <img alt="l-7" class="hand" id="l-7" src="../assets/tiles-left/l_back.png"/>
                </div>
            </div>
        </div>
        <div class="opposite">  <!--opposite tiles-->
            <div class="opposite_hand">
                <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
                <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
                <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
                <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
                <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
                <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
                <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
                <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
                <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
                <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
                <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
                <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
                <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
            </div>
            <div class="deal" v-if="currentPlayer = 2">
                <img alt="deal" id="deal_2" src=""/>
            </div>
            <div class="discard" v-if="currentPlayer = 2 & deal(owner, order[2])">
                <img alt="discard" id="discard_2" src=""/>
            </div>
        </div>
        <div class="right">  <!--right tiles-->
            <img alt="right" id="r_wall" src="../assets/tiles-right/r_wall.png"/>
            <div class="deal" v-if="currentPlayer = 3">
                <img alt="deal" id="deal_3" src=""/>
            </div>
            <div class="discard" v-if="currentPlayer = 3 & deal(owner, order[3])">
                <img alt="discard" id="discard_3" src=""/>
            </div>
        </div>
    </div>
</template>

<script>
import {postData} from "@/api.js";

export default {
    name: "GameTable",

    data(){
        return{
            name:'',
            owner:'',
            banker:'',
            order:'',
            currentPlayer:'',
            dealID:'',
            discardID:'',
            canPangId:'',
            canKongID:'',
            canChowID:[],
            gameProcess:true,
        }
    },

    // 生命钩子函数，在页面被加载时调用getOwner()
    created() {
        this.getName();
        this.getOwner();
        this.getBanker();
        this.getOrder();
    },

    // 生命钩子函数，在该页面被挂载时调用，我作为gameController使用
    mounted() {
        while(this.gameProcess){
            this.deal(this.owner, this.order[this.currentPlayer]);
            this.discard(this.owner, this.order[this.currentPlayer], this.discardID);
            this.canPang(this.owner, this.order[this.currentPlayer]);
            this.canKong(this.owner, this.order[this.currentPlayer]);
            this.canChow(this.owner, this.order[this.currentPlayer]);
            this.canHu(this.owner, this.order[this.currentPlayer]);
            this.canDraw(this.owner, this.order[this.currentPlayer]);
            this.currentPlayer = (this.currentPlayer + 1) % 4;
        }
    },

    methods:{
        // 获取房主名字string
        async getOwner(name){
            try {
                const response = await postData('/game/getUser', { name: name });
                this.owner = response.data;
                console.log('Response from POST:', response);
            } catch (error) {
                console.error('Error during POST:', error);
            }
        },

        // 获取该玩家的名字
        async getName(){
            // 使用fetch从ipify获取用户的IP地址
            fetch('https://api.ipify.org?format=json')
                .then(response => response.json())
                .then(data => {
                    const userIp = data.ip;
                    this.ip = userIp;
                    console.log("User IP Address: ", userIp);
                })
                .catch(error => console.error('Error fetching IP address:', error));
            // 根据ip请求该用户的名字
            try {
                const response = await postData('/game/getName', { ip: this.ip });
                this.name = response;
                console.log('Response from POST:', response);
            } catch (error) {
                console.error('Error during POST:', error);
            }
        },

        // 获取庄家
        async getBanker(owner, name){
            try {
                const response = await postData('/game/getBanker', { owner: owner, name: name });
                this.banker = response.data;
                this.currentPlayer = this.order.indexOf(this.banker);
                console.log('Response from POST:', response);
            } catch (error) {
                console.error('Error during POST:', error);
            }
        },

        // 获取出牌顺序
        async getOrder(owner, name){
            try {
                const response = await postData('/game/getSequence', { owner: owner, name: name });
                this.order = response.data;
                console.log('Response from POST:', response);
            } catch (error) {
                console.error('Error during POST:', error);
            }
        },

        // 获取发牌牌号
        async deal(owner, name){
            try {
                const response = await postData('/game/deal', { owner: owner, name: name });
                // 储存发牌的牌号 （此处可作弊）
                this.dealID = response.data;
                console.log('Response from POST:', response);
            } catch (error) {
                console.error('Error during POST:', error);
            }
            // 返回一个true，用于表明已经发完牌
            return true;
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

        }

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


    .my_hand {
        position: fixed;
        left: 50%;
        bottom: 10%;
        transform: translate(-50%, 0);
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