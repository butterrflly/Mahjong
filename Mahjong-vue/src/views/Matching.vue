<template>
    <div class="background">
      <div >
        <BackButton v-on:click="backToChoose()" class="backButton"/>
      </div>
      <h2>Matching a game</h2>
        <div class="room">
            <button v-if="createRoomButtonIf" v-on:click="createRoomCheck()">creatRoom</button>
            <button v-if="joinRoomButtonIf" v-on:click="joinRoomCheck()">Join in a Room</button>
        </div>

      <div v-if="createRoomIf">
        Room ID: <input v-model="createARoom.roomId" ><br />
        <!-- Add a confirmation button -->
        <button v-on:click="createRoom()" class="confirmRoomButton" >Confirm</button>
      </div>


<!--      if user try to join a room as a player-->
      <div v-if="joinRoomIf" >

        <h2>Total Room number: {{ roomNum}}</h2>

<!--            <h2>Enter the ID of the room:</h2>-->
<!--            Room Id:<input v-model = "this.joinARoom.roomId"/><br />-->
<!--            <button v-on:click="join()">Join</button>-->

        <div class="RoomGather">
          <button v-for="room in roomsArray" :key="room.roomId" v-on:click="chooseRoom(room.roomId)" class="chooseRoomButton">
            {{ room.roomId }}
          </button>
          <h2>If error message: {{this.joinARoom.errorMessage}}</h2>
        </div>

      </div>
    </div>
</template>

<script lang = 'ts'>
//创建房间
import WebSocketService from '../websocket.js';
import RulesButton from "../components/RulesButton.vue";
import BackButton from "../components/BackButton.vue";

export default {
  components: {BackButton, RulesButton},
    data(){
      return {
        owner: '',
        // conditions for judgement
        HaveCreateRoomIf:false,
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
        //put room message in to room1-10
        roomsArray : [
          { roomId: '', player: null },
          { roomId: '', player: null },
          { roomId: '', player: null },
          { roomId: '', player: null },
          { roomId: '', player: null },
          { roomId: '', player: null },
          { roomId: '', player: null },
          { roomId: '', player: null },
          { roomId: '', player: null },
          { roomId: '', player: null }
        ],
        // message to join a room
        joinARoom: {
          roomId: null,
          successMessage:'',
          errorMessage:'',
        },
        // message of preparing
        preparing:{
          successMessage:'',
          errorMessage:'',
        },
        //message to check if game can start
        signal: null,
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

      // handle the data received by listener
      /**
       * @return {"operation" : "getGameRooms", "msg" : {"name" : String, "room number" : int, "room message" : {"room id_1" : player number, "room id_2" : player number, ...}}}
       */

      handleMessage(data) {
        this.roomNum = JSON.stringify(data.msg["room number"]);
        this.roomInfo = JSON.stringify(data.msg["room message"]);
        this.getMessage(this.roomInfo); // The getMessage method is called when data is received
        if(data.operation == "getHandTile")
          this.signal = JSON.stringify(data.msg)
        console.log(this.signal);

      },
      backToChoose(){
        this.HaveCreateRoomIf=false;
        this.createRoomButtonIf= true;
        this.createRoomIf= false;
        this.joinRoomButtonIf= true;
        this.joinRoomIf=false;
        this.joinedIf=false;
        this.canStart = false;
        this.roomExist= false;
      },

      createRoomCheck(){
        this.createRoomButtonIf = false;
        this.joinRoomButtonIf = false;
        this.createRoomIf = true;
      },

      chooseRoom(roomId){
        const room = this.roomsArray.find(room => room.roomId === roomId)
        if (room && parseInt(room.player) >= 4) {
          this.joinARoom.errorMessage = 'This room is full and cannot be joined.';
          return;
        }
        console.log(`Room chosen: ${roomId}`);
        this.joinARoom.roomId = roomId;
        this.join();
        this.joinARoom.errorMessage = null; // Clear any previous error message
      },

      getMessage(roomMap){
        console.log(roomMap)
        roomMap = JSON.parse(roomMap);
        console.log(roomMap);
        let counter = 0;

        for (const roomId in roomMap) {
          if (roomId != null) {
            this.roomsArray[counter].roomId = `${roomId}`;
            this.roomsArray[counter].player = `${roomMap[roomId]}`;
            counter++;
          } else {
            break;
          }
        }
      },



        //info for room
        async createRoom() {//Add a room number input and have obtained a room number,
          WebSocketService.sendMessage(JSON.stringify({operation: 'createRoom', roomID: this.createARoom.roomId}))
          try{
            this.createARoom.successMessage = "success Creating a room."
            this.HaveCreateRoomIf = true;
            this.$router.push('/GameTable');
          }catch (error) {
            console.error('Error during POST:', error);
            this.createARoom.errorMessage = "An error occurred during Creating room. Please try again later.";
          }
        },

        async joinRoomCheck() {
            this.joinRoomButtonIf = false;
            this.createRoomButtonIf = false;
            this.joinRoomIf = true;
        },


        async join() {
          try {
            WebSocketService.sendMessage(JSON.stringify({ operation: 'intoRoom', roomID: this.joinARoom.roomId}));
            this.joinARoom.successMessage = "The join in has been realize successfully !"
            this.joinedIf = true;
            this.$router.push('/GameTable');
          } catch (error) {
            console.error('Error during POST:', error);
            this.joinARoom.errorMessage = "An error occurred while joining the room. Please try again later.";
          }
        },
    },

    // Connect to the WebSocket and register the message handling callback function
    mounted() {
        WebSocketService.addMessageListener(this.handleMessage);
    },

    // Removed the message handling callback function
    beforeUnmount() {
        WebSocketService.removeMessageListener(this.handleMessage);
    }
}

</script>

<style scoped>
.background {
  background-image: url('../assets/background.png');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  text-align: center;
  font-family: 'Arial', sans-serif;
}

h2 {
  font-size: 2.5rem;
  font-weight: bold;
  color: #000000FF;
  margin: 20px 0;
}

.room, .RoomGather {
  display: flex;
  flex-direction: column;
  align-items: center;
}

button {
  padding: 10px 20px;
  margin: 10px 0;
  font-size: 1rem;
  font-weight: bold;
  color: #ffffff;
  background-color: #4CAF50;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #45a049;
}

.confirmRoomButton, .chooseRoomButton {
  padding: 10px 20px;
  margin: 10px 0;
  font-size: 1rem;
  font-weight: bold;
  color: #ffffff;
  background-color: #0f8a3c;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.confirmRoomButton:hover, .chooseRoomButton:hover {
  background-color: #1e87e0;
}

input {
  padding: 10px;
  margin: 10px 0;
  font-size: 1rem;
  border: 2px solid #ffffff;
  border-radius: 5px;
  outline: none;
  transition: border-color 0.3s ease;
}

input:focus {
  border-color: #4CAF50;
}

.backButton {
  position: absolute;
  top: 20px;
  left: 20px;
  font-size: 1.5rem;
  color: #ffffff;
  cursor: pointer;
  transition: color 0.3s ease;
}

.backButton:hover {
  color: #4CAF50;
}
</style>

