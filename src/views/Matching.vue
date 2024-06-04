<template>
    <div class="background">
        <h2>Matching a game</h2>
        <div class="room">
            <button v-if="createRoomButtonIf" v-on:click="createRoom()">creatRoom</button>
            <button v-if="joinRoomButtonIf" v-on:click="joinRoom()">Join in the Room</button>
            <h2>room number: {{roomNum}}</h2>
            <h2>room info: {{roomInfo}}</h2>
        </div>

        <div v-if="createRoomIf">
            <!-- if the room is created and can't start -->
            <h2 v-if="!canStart">Waiting for players to join...</h2>
            <!-- can start -->
            <button v-on:click="startGame(owner)" class="startGameButton">Start Game!</button>
        </div>
        <div v-if="joinRoomIf">
            <h2>Enter the owner of the room:</h2>
            owner name:<input v-model = "this.owner"/><br />
            <button v-on:click="join(owner)">Join</button>
            <!-- if the room is created and can't start-->
            <h2 v-if="!canStart & joinedIf">Waiting for players to join...</h2>
            <!-- can start -->
            <button v-on:click="startGame(owner)" class="startGameButton">Start Game!</button>
        </div>
        <div v-if="message" class="success-message">{{ message }}</div>
    </div>
</template>

<script lang = 'ts'>
//创建房间
import { postData } from '../api.js';
import WebSocketService from '../websocket.js';

export default {
    data(){
        return{
            owner: '',
            // conditions for judgement
            createRoomButtonIf: true,
            createRoomIf: false,
            joinRoomButtonIf: true,
            joinRoomIf: false,
            joinedIf: false,
            canStart: false,
            roomExist: false,
            // message to display
            message: null,
            roomNum: null,
            roomInfo: null,
            // info of each player,
            // {"name" : String, "prepare" : boolean, "score" : int}
            selfInfo: null,
            nextInfo: null,
            oppoInfo: null,
            prevInfo: null,
        }
    },

    methods:{
        // handle the data received by listener
        handleMessage(data) {
            if (data.operation == "getGameRooms") {
                this.roomNum = data.msg["room number"];
                this.roomInfo = data.msg["room message"];
            }else if (data.operation == "Duplicate room number") {
                this.message = "Duplicate room number!"
            }else if (data.operation == "getRoomPlayerMessage") {
                this.selfInfo = data.msg["self"];
                this.nextInfo = data.msg["nextPlayer"];
                this.oppoInfo = data.msg["oppositePlayer"];
                this.prevInfo = data.msg["prevPlayer"];
            }
        },

        async createRoom() {
            this.createRoomButtonIf = false;
            this.joinRoomButtonIf = false;
            this.createRoomIf = true;
            try {
                const response = await postData('game/newRoom', { owner: this.selfName});
                console.log('Response from POST:', response);
                this.message = "room created";
                // 保存owner
                await this.updateOwner(this.selfName);
            } catch (error) {
                console.error('Error during POST:', error);
            }
        },

        async joinRoom() {
            this.joinRoomButtonIf = false;
            this.createRoomButtonIf = false;
            this.joinRoomIf = true;
        },


        async join(owner) {
            await this.ifRoomExist(owner);
            if(this.roomExist){
                try {
                    const response = await postData('game/addPlayer', { owner: owner, name: this.selfName});
                    console.log('Response from POST:', response)
                    this.owner = owner;
                    if(response == "玩家已存在"){
                        this.message = "Player already in the room";
                    }else if(response == "房间已满"){
                        this.message = "Room is full";
                    }else if(response == "添加成功"){
                        this.message = "Join successful!";
                        this.joinedIf = true;
                        // 保存owner
                        await this.updateOwner(this.owner);

                    }
                } catch (error) {
                    console.error('Error during POST:', error);
                }
            }else{
                this.message = "can't find this room!"
            }

        },

        async ifRoomExist(owner) {
            try {
                const response = await postData('game/findRoom', { owner: owner });
                console.log('Response from POST:', response);
                if(response == true){
                    this.roomExist = true;
                }else if(response == false){
                    this.roomExist = false;
                }
            } catch (error) {
                console.error('Error during POST:', error);
            }
        },

        async startGame(owner) {
            try {
                // 使用封装的 postData 函数发起 POST 请求
                const response = await postData('game/startGame', { owner: owner });
                console.log('Response from POST:', response);
                if(response == "游戏开始"){
                    this.canStart = true;
                    this.$router.push({path: '/GameTable'});
                } else if(response == "人数不足"){
                    this.canStart = false;
                }
            } catch (error) {
                console.error('Error during POST:', error);
                this.canStart = false;
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

    },
}

</script>

<style scoped>
.background {
    background-image: url('@/assets/background.png');
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
    position: fixed;
    top:0;
    left:0;
    width: 100%;
    height: 100vh;
}
</style>
