<template>
    <div class="background">
        <h2>Matching a game</h2>
        <div class="room">
            <button v-if="createRoomButtonIf" v-on:click="createRoomCheck()">creatRoom</button>
            <button v-if="joinRoomButtonIf" v-on:click="joinRoom()">Join in the Room</button>
             <h2>Room number: {{ roomNum }}</h2>
              <h2>Room Info:  {{ displayRoomMap(roomInfo)}}</h2>
        </div>

      <div v-if="createRoomIf">
        <!-- if the room is created and can't start -->
        <h2 v-if="!canStart">Waiting for players to join...</h2>

        Room ID: <input v-model="createARoom.roomId" ><br />




        <!-- 添加确认按钮 -->
        <button v-on:click="createRoom()" class="confirmRoomButton" >Confirm</button>

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
      return {
        owner: '',
        // conditions for judgement
        confirmRoomId: false,
        createRoomButtonIf: true,
        createRoomIf: false,
        joinRoomButtonIf: true,
        joinRoomIf: false,
        joinedIf: false,
        canStart: false,
        roomExist: false,
        // message to create room
        createARoom: {
          roomId: null,
          successMessage:'',
          errorMessage:'',
        },
        // message to join a room
        joinARoom: {
          roomId: null,
          successMessage:'',
          errorMessage:'',
        },
        // message to display
        message: null,
        roomNum: null,
        roomInfo: null,
        // info of four players, {"name" : String, "prepare" : boolean, "score" : int}
        selfInfo: null,
        nextInfo: null,
        oppoInfo: null,
        prevInfo: null
      };
    },




    methods:{

      createRoomCheck(){
        this.createRoomButtonIf = false;
        this.joinRoomButtonIf = false;
        this.createRoomIf = true;
      },


      displayRoomMap(roomMap) {
        roomMap = JSON.parse(roomMap);
        console.log(roomMap);
        let displayString = '';
        for (const roomId in roomMap) {
          if (roomMap.hasOwnProperty(roomId)) {
            displayString += `Room ID: ${roomId}, Player Number: ${roomMap[roomId]}\n`;
          }
        }
        return displayString;
      },
        // handle the data received by listener
      /**
       * @return {"operation" : "getGameRooms", "msg" : {"name" : String, "room number" : int, "room message" : {"room id_1" : player number, "room id_2" : player number, ...}}}
       */

        handleMessage(data) {
            if (data.operation === "getGameRooms") {
                this.roomNum = JSON.stringify(data.msg["room number"]);
                this.roomInfo = JSON.stringify(data.msg["room message"]);
                console.log(this.roomInfo);
            }else if (data.operation === "Duplicate room number") {
                this.message = "Duplicate room number!"
            }else if (data.operation === "getRoomPlayerMessage") {
                this.selfInfo = data.msg["self"];
                this.nextInfo = data.msg["nextPlayer"];
                this.oppoInfo = data.msg["oppositePlayer"];
                this.prevInfo = data.msg["prevPlayer"];
            }
        },

        //info for room
        async createRoom() {//添加一个房间号输入,已获得房间号,
          WebSocketService.sendMessage(JSON.stringify({operation: 'createRoom', roomID: this.createARoom.roomId}))
          try{
            this.createARoom.successMessage = "success Creating a room."
          }catch (error) {
            console.error('Error during POST:', error);
            this.createARoom.errorMessage = "An error occurred during Creating room. Please try again later.";
          }
        },

        async joinRoom() {
            this.joinRoomButtonIf = false;
            this.createRoomButtonIf = false;
            this.joinRoomIf = true;
             WebSocketService.sendMessage(JSON.stringify({ operation: 'intoRoom', roomID: this.joinARoom.roomId }));
             try {

             } catch (error) {
               console.error('Error during POST:', error);
               this.joinARoom.errorMessage = "An error occurred while joining the room. Please try again later.";
             }
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
