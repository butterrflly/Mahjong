<template>
  <div class="background">
    <header>
      <img alt="logo" class="logo" src="../assets/logo.png" width="150" height="150" />
      <div class="gameTitle">
        <GameTitle msg="Mahjong Game" />
      </div>
    </header>

    <div class="register">
      <!-- buttons-->
      <div>
        <button v-if="registerButtonIf" v-on:click="register">Register</button>
        <button v-if="registerIf" v-on:click="signup(userName, password)">Register！</button>

        <button v-if="deleteAccountButtonIf" v-on:click="deleteAccount">Delete Account</button>
        <button v-if="deleteAccountIf" v-on:click="deleteUser(userName, password)">Delete！</button>

        <button v-if="loginButtonIf" v-on:click="loginAccount">Login</button>
        <button v-if="loginIf" v-on:click="login(userName, password)">Login！</button>

        <button v-if="logoutButtonIf" v-on:click="logoutAccount">Logout</button>
        <button v-if="logoutIf" v-on:click="logout(userName)">Logout！</button>

        <button v-if="changePasswordButtonIf" v-on:click="changeNewPassword">Change Password</button>
        <button v-if="changePasswordIf" v-on:click="changePassword(userName, password, newPassword)">Login！</button>
      </div>

      <!-- 表单 -->
      <div>
        <!-- 是否注册 -->
        <div v-if="registerIf">
          <h2>Set your userName and password</h2>
          new user name:<input v-model="userName" /><br />
          new password:<input type="password" v-model="password" /><br />
          <div class="backButton">
            <BackButton v-on:click="backToChoose()" class="backButton"/>
          </div>

        </div>

        <!-- 是否删除账户 -->
        <div v-if="deleteAccountIf">
          <h2>Make sure you want to Close Account</h2>
          user name want to delete:<input v-model="userName" /><br />
          enter the password:<input type="password" v-model="password" /><br />
          <div class="backButton">
            <BackButton v-on:click="backToChoose()" class="backButton"/>
          </div>
        </div>

        <!-- 是否登陆 -->
        <div v-if="loginIf">
          <h2>Please enter your user name and password</h2>
          user name:<input v-model="userName" /><br />
          password:<input type="password" v-model="password" /><br />
          <div class="backButton">
            <BackButton v-on:click="backToChoose()" class="backButton"/>
          </div>
        </div>

        <!-- 是否登陆 -->
        <div v-if="logoutIf">
          <h2>Please enter your user name and password</h2>
          user name:<input v-model="userName" /><br />
          <div class="backButton">
            <BackButton v-on:click="backToChoose()" class="backButton"/>
          </div>
        </div>

        <!-- 是否改密码 -->
        <div v-if="changePasswordIf">
          <h2>Please enter your user name, old password and new password</h2>
          user name:<input v-model="userName" /><br />
          old password:<input type="password" v-model="password" /><br />
          new password:<input type="password" v-model="newPassword" /><br />
          <div class="backButton">
            <BackButton v-on:click="backToChoose()" class="backButton"/>
          </div>
        </div>

        <!-- 显示错误消息 -->
        <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
        <!-- 显示成功消息 -->
        <div v-if="successMessage"  class="success-message">{{ successMessage }}
        </div>
      </div>

      <router-link to="GameRules"><RulesButton class="rulesButton" /></router-link>
    </div>

  </div>

</template>

<script lang = 'ts'>
import {defineComponent} from "vue";
import GameTitle from "../components/GameTitle.vue";
import RulesButton from "../components/RulesButton.vue";
import MatchingButton from "../components/MatchingButton.vue";
import LoginButton from "../components/LoginButton.vue";
import { postData } from '../api.js';
import WebSocketService from '../websocket.js';
import BackButton from "../components/BackButton.vue";
import logoutButton from "../components/LogoutButton.vue";



export default defineComponent({
    components: {BackButton, LoginButton, logoutButton,MatchingButton, RulesButton, GameTitle},

  data(){
    return{
      userName:'',
      password:'',
      newPassword:'',
      successMessage:'',
      errorMessage:'',
      // 控制5个表单的出现
      registerIf:false,
      deleteAccountIf:false,
      loginIf:false,
      changePasswordIf: false,
      logoutIf:false,
      // 控制5个按钮的出现
      registerButtonIf:true,
      deleteAccountButtonIf:true,
      loginButtonIf:true,
      changePasswordButtonIf:true,
      logoutButtonIf:true,
    }
  },

  methods: {
    backToChoose(){
      // 控制5个表单的出现
          this.registerIf=false;
          this.deleteAccountIf=false;
          this.loginIf=false;
          this.changePasswordIf= false;
          this.logoutIf=false;
          // 控制5个按钮的出现
          this.registerButtonIf=true;
          this.deleteAccountButtonIf=true;
          this.loginButtonIf=true;
          this.changePasswordButtonIf=true;
          this.logoutButtonIf=true;
          this.errorMessage ='';
          this.successMessage = '';
    },

    register() {
      this.successMessage = '';
      this.errorMessage = '';
      this.registerButtonIf = false;
      this.registerIf = true;
      this.deleteAccountButtonIf = false;
      this.deleteAccountIf = false;
      this.loginButtonIf = false;
      this.loginIf = false;
      this.logoutButtonIf = false;
      this.logoutIf = false;
      this.changePasswordButtonIf = false;
      this.changePasswordIf = false;
    },

    deleteAccount() {
      this.successMessage = '';
      this.errorMessage = '';
      this.deleteAccountButtonIf = false;
      this.deleteAccountIf = true;
      this.registerButtonIf = false;
      this.registerIf = false;
      this.loginButtonIf = false;
      this.loginIf = false;
      this.logoutButtonIf = false;
      this.logoutIf = false;
      this.changePasswordButtonIf = false;
      this.changePasswordIf = false;
    },

    loginAccount() {
      this.successMessage = '';
      this.errorMessage = '';
      this.loginButtonIf = false;
      this.loginIf = true;
      this.registerButtonIf = false;
      this.registerIf = false;
      this.deleteAccountButtonIf = false;
      this.deleteAccountIf = false;
      this.changePasswordButtonIf = false;
      this.changePasswordIf = false;
      this.logoutButtonIf = false;
      this.logoutIf = false;
    },

    changeNewPassword() {
      this.successMessage = '';
      this.errorMessage = '';
      this.changePasswordButtonIf = false;
      this.changePasswordIf = true;
      this.registerButtonIf = false;
      this.registerIf = false;
      this.deleteAccountButtonIf = false;
      this.deleteAccountIf = false;
      this.loginButtonIf = false;
      this.loginIf = false;
      this.logoutButtonIf = false;
      this.logoutIf = false;
    },

    back() {
      this.successMessage = '';
      this.errorMessage = '';
      this.registerButtonIf = true;
      this.registerIf = false;
      this.deleteAccountButtonIf = true;
      this.deleteAccountIf = false;
      this.loginButtonIf = true;
      this.loginIf = false;
      this.changePasswordButtonIf = true;
      this.changePasswordIf = false;
      this.logoutButtonIf = true;
      this.logoutIf = false;
    },

    logoutAccount() {
      this.successMessage = '';
      this.errorMessage = '';
      this.loginButtonIf = false;
      this.loginIf = false;
      this.registerButtonIf = false;
      this.registerIf = false;
      this.deleteAccountButtonIf = false;
      this.deleteAccountIf = false;
      this.changePasswordButtonIf = false;
      this.changePasswordIf = false;
      this.logoutButtonIf = false;
      this.logoutIf = false;
      this.logoutButtonIf = false;
      this.logoutIf = true;
    },



    async signup(name, password) {
      try {
        // 使用封装的 postData 函数发起 POST 请求
        const response = await postData('user/signup', { name: name, password: password });
        console.log('Response from POST:', response);
        if (response=="注册成功") {
          this.successMessage = "success!"
        } else if (response === "用户已存在") {
          this.errorMessage = "account already exist!";
        }
      } catch (error) {
        console.error('Error during POST:', error);
        this.errorMessage = "An error occurred during login. Please try again later.";
      }
      this.userName = '';
      this.password = '';
    },

    async login(name, password) {
      // 向服务器保存name，并在之后的页面接收返回信息

      try {
        // 使用封装的 postData 函数发起 POST 请求
        const response = await postData('user/login', { name: name, password: password });
        console.log('Response from POST:', response);
        if (response=="登录成功") {
          this.successMessage = "success!"
          WebSocketService.sendMessage(JSON.stringify({operation: "login", playerName: name}))
          this.$router.push('/Matching');
          // this.$router.push({path: '/'});
        } else if (response === "密码错误") {
          this.errorMessage = "wrong password！";
        } else if (response === "用户不存在") {
          this.errorMessage = "account does not exist!";
        } else if (response === "用户已登录") {
          this.errorMessage = "account has been login!";
        }
      } catch (error) {
        console.error('Error during POST:', error);
        this.errorMessage = "An error occurred during login. Please try again later.";
      }
      this.userName = '';
      this.password = '';
    },

    async logout(name) {
      // 向服务器保存name，并在之后的页面接收返回信息
      try {
        // 使用封装的 postData 函数发起 POST 请求
        const response = await postData('user/logout', { name: name});
        console.log('Response from POST:', response);
        if (response=="退出成功") {
          this.successMessage = "success logout!"
          // this.$router.push({path: '/'});
        } else if (response === "用户未登录") {
          this.errorMessage = "user have not been log in！";
        }else if (response === "用户不存在") {
          this.errorMessage = "account does not exist!";
        }
      } catch (error) {
        console.error('Error during POST:', error);
        this.errorMessage = "An error occurred during login. Please try again later.";
      }
      this.userName = '';
    },

    async deleteUser(name, password) {
      try {
        // 使用封装的 postData 函数发起 POST 请求
        const response = await postData('user/deleteUser', { name: name, password: password });
        console.log('Response from POST:', response);
        return response;
      } catch (error) {
        console.error('Error during POST:', error);
        this.errorMessage = "An error occurred during login. Please try again later.";
      }
      this.userName = '';
      this.password = '';
    },

    async changePassword(name, oldPassword, newPassword) {
      try {
        // 使用封装的 postData 函数发起 POST 请求
        const response = await postData('user/changePassword', { name: name, oldPassword: oldPassword, newPassword: newPassword });
        console.log('Response from POST:', response);
        return response;
      } catch (error) {
        console.error('Error during POST:', error);
        this.errorMessage = "An error occurred during login. Please try again later.";
      }
      this.userName = '';
      this.password = '';
      this.newPassword = '';
    }

  },
});
</script>



<style scoped>
    header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
    font-family: Arial, sans-serif;
    }

    .logo {
      margin: 10rem 0 0 0;
      position: fixed;
      left: 300px;
      top: -100px;
    }

    .background {
        background-image: url('../assets/background.png');
        background-size: cover;
        background-repeat: no-repeat;
        background-position: center;
        position: fixed;
        top:0;
        left:0;
        width: 100%;
        height: 100vh;
    }

    .register {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      width: 50%;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      z-index: 10;
    }

    .register header {
      width: 100%;
      display: flex;
      justify-content: flex-start;
      align-items: center;
      margin-bottom: 20px;
    }

    .register button {
      background-color: #0f8a3c;
      color: white;
      border: none;
      padding: 10px 20px;
      margin: 10px 0;
      border-radius: 5px;
      cursor: pointer;
      width: 100%;
    }

    .register button:hover {
      background-color: #0f8a3c;
    }

    .register h2 {
      font-size: 2em;
      margin-bottom: 10px;
    }

    .register input {
      width: calc(100% - 22px);
      padding: 10px;
      margin-bottom: 10px;
      border: 1px solid #ccc;
      border-radius: 5px;
    }

    .register .error-message {
      color: #d9534f;
      font-weight: bold;
      margin-bottom: 10px;
    }

    .register .success-message {
      color: #5cb85c;
      font-weight: bold;
      margin-bottom: 10px;
    }

    .rulesButton {
      position: absolute;
      top: calc(100% + 20px); /* 20px的间距，你可以根据需要调整 */
      left: 50%;
      transform: translateX(-50%);
      background-color: #5bc0de;
      color: white;
      border: none;
      padding: 10px 20px;
      border-radius: 5px;
      cursor: pointer;
      z-index: 20;
    }

</style>

