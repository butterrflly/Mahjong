<template>
  <div class="background">
  <div>
    <header><router-link to="/Menu"><BackButton class="backButton"/></router-link></header>
    user name:<input v-model = "userName"/><br />
    <!-- 打印username -->
    password:<input type="number" v-model = "password"><br />
    <!-- v-model 是 Vue.js 中的一个指令，用于在表单输入和应用状态之间创建双向数据绑定 -->
    <button v-on:click="register()">Register</button><button v-on:click="closeAccount()">Close Account</button><button v-on:click="login()">Login</button>
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
      }
    }
  },
  methods:{
    register(){
      this.registerIf = !this.registerIf;
      this.closeAccountIf = false;
    },
    closeAccount(){
      this.closeAccountIf = !this.closeAccountIf;
      this.registerIf = false;


    },
    login(){
      this.loginIf = !this.loginIf;

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

