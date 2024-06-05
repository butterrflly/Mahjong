import './assets/main.css'
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from "./App.vue";
import router from './router';


//加载路由器
const app = createApp(App)
app.use(router)
app.use(ElementPlus)
app.mount('#app')
