import './assets/main.css'

import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import GameTable from './views/GameTable.vue'
import Login from './views/Login.vue'
import Menu from './views/Menu.vue'
import GameRules from "./views/GameRules.vue";
import Matching from "./views/Matching.vue";
import 'element-plus/dist/index.css'
import {createRouter, createWebHistory} from 'vue-router'
import App from "@/App.vue";

//配置基本路由规则
const routes = [
    {path:"/", redirect: "/Menu"},
    {path:"/Menu", name: Menu, component: Menu},
    {path:"/GameTable", name: GameTable, component: GameTable},
    {path:"/Login", name: Login, component: Login},
    {path:"/GameRules", name: GameRules, component: GameRules},
    {path:"/Matching", name: Matching, component: Matching}
]

//创建路由器
const router = createRouter(
    {
        history:createWebHistory(),
        routes,
    }
)

//加载路由器
const app = createApp(App)
app.use(router)
app.use(ElementPlus)
app.mount('#app')
