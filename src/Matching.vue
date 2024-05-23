<template>
  <div className="background">
    <h2>test</h2>
    <div className="room">

      <button v-on:click="fillIn(),createRoom()">creatRoom</button><button v-on:click="joinRoom()">Join in the Room</button>
    </div>
    <div v-if="createRoomIf">
      {{users[0]}}
      <h2>are the host of the room</h2>
      <!--创建房间进行初始化 -->
    </div>
    <div v-if="joinRoomIf">
    {{users[valueOfUser]}}<h2>join in  the room</h2>

    {{valueOfUser}}<h2>of players is in the room</h2>

    {{users}}<h2>is waiting in the room</h2>
    <!--加入房间 -->
  </div>
  </div>
</template>

<script lang = 'ts'>
//创建房间
export default {
  data(){
    return{
      valueOfUser:0,
      createRoomIf:false,
      joinRoomIf:false,
      users:['', '', '', ''],//这是存储用户姓名的数组
      usernames:{//这是暂存后端输出的String格式用户姓名的集合
        username1:'userA',
        username2:'userB',
        username3:'userC',
        username4:'userD',
      }
    }
  },

  methods:{
    createRoom(){
       this.createRoomIf = !this.createRoomIf;
       this.valueOfUser++;
       this.users[0] = this.usernames.username1;
       //创建房间的代码
    },
    joinRoom(){
      if(this.valueOfUser == 4){
        this.startGame();
      }
      //加入房间的代码
      this.joinRoomIf = true;
     this.valueOfUser++;
    //这个方法的输出结果就是数组包含所有房间内的成员,其中第一个是房主,所以还缺少一个判断,不让房主按这个按钮
      // 在这个界面中我还需打印究竟是谁已经加入了房间,这个很重要
      // 最终改动是每个人只允许按一次join
    },
    startGame(){
      //这是一个启动方法用来启动程序进入游戏界面,
      this.$router.push({ path: '/GameTable' });
    },
    fillIn(){//将暂存的usernames全部加入到数组中,这样的作用是方便遍历提取
      this.users[0] = this.usernames.username1;
      this.users[1] = this.usernames.username2;
      this.users[2] = this.usernames.username3;
      this.users[3] = this.usernames.username4;
    }
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
