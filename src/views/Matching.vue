<template>
    <div class="background">
        <h2>Matching a game</h2>
        <div class="room">
            <button v-if="createRoomButtonIf" v-on:click="createRoom()">creatRoom</button>
            <button v-if="joinRoomButtonIf" v-on:click="joinRoom()">Join in the Room</button>
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
import { mapState, mapActions, mapGetters } from 'vuex';
import { postData } from '../api.js';

export default {
    computed: {
        ...mapState(['name', 'owner'])
    },

    data(){
        return{
            selfName: this.getName(),
            owner: '',
            createRoomButtonIf: true,
            createRoomIf: false,
            joinRoomButtonIf: true,
            joinRoomIf: false,
            joinedIf: false,
            canStart: false,
            roomExist: false,
            message: null,
        }
    },

    methods:{
        ...mapActions(['updateName', 'updateOwner']),
        ...mapGetters(['getName', 'getOwner']),

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
