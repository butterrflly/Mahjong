import './assets/main.css'

import { createApp } from 'vue'
import GameTable from './GameTable.vue'
import Login from './Login.vue'
import Menu from './Menu.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import {createRouter, createWebHistory} from 'vue-router'
import GameRules from "./GameRules.vue";

//1.配置基本路由规则
const routes = [
{path:"/Menu", component: Menu},
{path:"/GameTable", component: GameTable},
{path:"/Login", component: Login},
{path:"/GameRules", component: GameRules},

]
//2.创建路由器
const router = createRouter(
    {
        history:createWebHistory(),
        routes
    }
)
//3.加载路由器

const app = createApp(Menu)
app.use(router)
app.use(ElementPlus)
app.mount('#app')
