<template>
  <div class="background">
    <div>
      <header><BackButton @click="this.$router.go(-1)" class="backButton"/></header>
    </div>
    <!-- buttons-->
    <div>
      <button v-if="registerButtonIf" v-on:click="register()">Register</button>
      <button v-if="registerIf" v-on:click="signup(userName, password)">Register！</button>

      <button v-if="deleteAccountButtonIf" v-on:click="deleteAccount()">Delete Account</button>
      <button v-if="deleteAccountIf" v-on:click="deleteUser(userName, password)">Delete！</button>

      <button v-if="loginButtonIf" v-on:click="loginAccount()">Login</button>
      <button v-if="loginIf" v-on:click="login(userName, password)">Login！</button>

      <button v-if="loginButtonIf" v-on:click="logoutAccount()">Logout</button>
      <button v-if="logoutIf" v-on:click="logout(userName)">Logout！</button>

      <button v-if="changePasswordButtonIf" v-on:click="changeNewPassword()">Change Password</button>
      <button v-if="changePasswordIf" v-on:click="changePassword(userName, password, newPassword)">Change Password！</button>
    </div>

    <div>
      <!--是否注册-->
      <div v-if="registerIf">
        <h2>Set your userName and password</h2>
        new user name:<input v-model="userName"/><br />
        new password:<input type="password" v-model="password"><br />
      </div>

      <!--是否删除账户-->
      <div v-if="deleteAccountIf">
        <h2>Make sure you want to Close Account</h2>
        user name want to delete:<input v-model="userName"/><br />
        enter the password:<input type="password" v-model="password"><br />
      </div>

      <!--是否登陆-->
      <div v-if="loginIf">
        <h2>Please enter your user name and password</h2>
        user name:<input v-model="userName"/><br />
        password:<input type="password" v-model="password"><br />
      </div>

      <!--是否登出-->
      <div v-if="logoutIf">
        <h2>Please enter your user name </h2>
        user name:<input v-model="userName"/><br />
      </div>

      <!--是否改密码-->
      <div v-if="changePasswordIf">
        <h2>Please enter your user name, old password and new password</h2>
        user name:<input v-model="userName"/><br />
        old password:<input type="password" v-model="password"><br />
        new password:<input type="password" v-model="newPassword"><br />
      </div>

      <!-- 在 errorMessage 不为空时显示错误消息 -->
      <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
      <!-- 在 successMessage 不为空时显示成功消息 -->
      <div v-if="successMessage" class="success-message">{{ successMessage }}</div>
      <button v-if="errorMessage || successMessage" v-on:click="back()">Back</button>
    </div>
  </div>
</template>


<script lang ='ts'>
import BackButton from "../components/BackButton.vue";
import { postData } from '../api.js';
import WebSocketService from '../websocket.js';

export default {
    components: {BackButton},

    data(){
        return{
            userName:'',
            password:'',
            newPassword:'',
            successMessage:'',
            errorMessage:'',
            // 控制四个表单的出现
            registerIf:false,
            deleteAccountIf:false,
            loginIf:false,
            logoutIf:false,
            changePasswordIf: false,
            // 控制四个按钮的出现
            registerButtonIf:true,
            deleteAccountButtonIf:true,
            loginButtonIf:true,
            logoutButtonIf:true,
            changePasswordButtonIf:true,
        }
    },

    methods: {

        register() {
            // 初始化，控制四个表单和按钮的出现
            this.successMessage = '';
            this.errorMessage = '';
            this.registerButtonIf = false;
            this.registerIf = true;
            this.deleteAccountButtonIf = false;
            this.deleteAccountIf = false;
            this.loginButtonIf = false;
            this.loginIf = false;
            this.logoutIf = false;
            this.logoutButtonIf = false;
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
            this.logoutIf = false;
            this.logoutButtonIf = false;
            this.changePasswordButtonIf = false;
            this.changePasswordIf = false;
        },

        loginAccount() {
            this.successMessage = '';
            this.errorMessage = '';
            this.loginButtonIf = false;
            this.loginIf = true;
            this.logoutIf = false;
            this.logoutButtonIf = false;
            this.registerButtonIf = false;
            this.registerIf = false;
            this.deleteAccountButtonIf = false;
            this.deleteAccountIf = false;
            this.changePasswordButtonIf = false;
            this.changePasswordIf = false;
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
            this.logoutIf = false;
            this.logoutButtonIf = false;
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
            this.logoutIf = false;
            this.logoutButtonIf = false;
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
          this.logoutIf = true;
          this.logoutButtonIf = false;
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
                    // this.$router.push({path: '/'});
                } else if (response === "密码错误") {
                    this.errorMessage = "wrong password！";
                } else if (response === "用户不存在") {
                    this.errorMessage = "account does not exist!";
                }else if (response === "用户已登录") {
                  this.errorMessage = "You have already logged in successfully, you do not need to log in again!";
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
          WebSocketService.sendMessage(JSON.stringify({operation: "logout", playerName: name}))
          try {
            // 使用封装的 postData 函数发起 POST 请求
            const response = await postData('user/login', { name: name });
            console.log('Response from POST:', response);
            if (response=="退出成功") {
              this.successMessage = "success!"
              // this.$router.push({path: '/'});
            } else if (response === "用户未登录") {
              this.errorMessage = "user have not been login！";
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

    // 在生命钩子函数中处理websocket服务
    // 连接 WebSocket 并注册消息处理回调函数
    // created() {
    //     WebSocketService.addMessageListener(this.handleMessage);
    // },

    // // 移除消息处理回调函数
    // beforeUnmount() {
    //     WebSocketService.removeMessageListener(this.handleMessage);
    // }
}

</script>


<style scoped>
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

.error-message {
    color: red;
    margin-top: 10px;
}
</style>

