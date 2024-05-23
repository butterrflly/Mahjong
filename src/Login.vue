<template>
  <div class="background">
  <div>
    <header><router-link to="/Menu"><BackButton class="backButton"/></router-link></header>
    user name:<input v-model = "userName"/><br />
    <!-- 打印username -->
    password:<input type="number" v-model = "password"><br />
    <!-- v-model 是 Vue.js 中的一个指令，用于在表单输入和应用状态之间创建双向数据绑定 -->
    <button v-on:click="register()">Register</button><button v-on:click="closeAccount()">Close Account</button><button v-on:click="Login()">Login</button>
    <!--v-on 是一个指令，用于监听 DOM 事件并在事件触发时执行一些 JavaScript 代码 -->
  </div>
  <div v-if="registerIf" >
    <h2>Register your userName and password</h2>
    <!--需要检查与之前的用户名是否重合 -->
    new user name:<input v-model = "newUserInfo.newUserName"/><br />
    new password:<input type="number" v-model = "newUserInfo.newUserPassword"><br />
  </div>
  <div v-if="closeAccountIf" >
    <h2>Make sure you want to Close Account</h2>
    delete user name:<input v-model = "deleteUserInfo.deleteUserName"/><br />
    delete password:<input type="number" v-model = "deleteUserInfo.deleteUserPassword"><br />
  </div>
  </div>
</template>

<script lang ='ts'>
import BackButton from "@/components/BackButton.vue";

export default {
  components: {BackButton},
//export default 是 ES6 语法中的一个特性，用于在一个模块或文件中导出一个“默认”值，这样其他文件就可以通过 import 语句来引入这个值
  data(){
    return{
      userName:'',
      password:null,
      registerIf:false,
      closeAccountIf:false,
      loginIf:false,
      newUserInfo:{
        newUserName:'',
        newUserPassword:null,
      },
      deleteUserInfo: {
        deleteUserName:'',
        deleteUserPassword:null,
      }
    }
  },
  methods: {
    register() {
      this.registerIf = !this.registerIf;
      this.closeAccountIf = false;
      // 当注册表单提交时，调用 signup 方法
      if (this.registerIf === false) {
        this.signup(this.newUserInfo.newUserName, this.newUserInfo.newUserPassword);
      }
    },

    closeAccount() {
      this.closeAccountIf = !this.closeAccountIf;
      this.registerIf = false;
      // 当关闭账户表单提交时，调用 deleteUser 方法
      if (this.closeAccountIf === false) {
        this.deleteUser(this.deleteUserInfo.deleteUserName,this.deleteUserInfo.deleteUserPassword);
      }
    },

    Login() {
      this.loginIf = !this.loginIf;
      // 调用 login 方法,得到用户名与密码是否正确de返回值
      this.login(this.userName,this.password);
      //这里通过login方法接受返回值字符串,对loginIf进行最终修改






      if (this.loginIf) {
        this.$router.push({ path: '/Matching' });//进行页面跳转的方法
      }
    },


    signup(name, password) {
      fetch('http://localhost:5173/Login/signup', {//说实话我不确定这个url该填什么,后期拜托了
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name, password })
      })
          .then(response => response.json())
          .then(data => console.log(data))
          .catch(error => console.error('Error:', error));
    },

    login(name, password) {
      fetch('http://localhost:5173/Login/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name, password })
      })
          .then(response => response.json())
          .then(data => {
            console.log(data);
          //在这里接受后端的返回值,暂且可能是'yes'或者'no',需要一个检测方法来进行判断,这里是合代码之后的主要方向
          })
          .catch(error => console.error('Error:', error));

    },

    deleteUser(name, password) {
      fetch('http://localhost:5173/Login/deleteUser', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name, password })
      })
          .then(response => response.json())
          .then(data => console.log(data))
          .catch(error => console.error('Error:', error));
    }
  }
}</script>


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

button {
  background-color: #4CAF50; /* 按钮背景颜色 */
  border: none;
  color: white;
  padding: 10px 20px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin: 4px 2px;
  cursor: pointer;
  border-radius: 4px;
}

input[type='number'] {
  margin: 8px 0;
  display: inline-block;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  padding: 12px 20px;
}

input[type='text'] {
  margin: 8px 0;
  display: inline-block;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  padding: 12px 20px;
}

h2 {
  color: #333;
}
</style>

