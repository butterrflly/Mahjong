import { createRouter, createWebHistory } from 'vue-router';
import GameTable from '../views/GameTable.vue'
import Menu from '../views/Menu.vue'
import GameRules from "../views/GameRules.vue";
import Matching from "../views/Matching.vue";

const routes = [
    {path:"/", redirect: "/Menu"},
    {path:"/Menu", name: Menu, component: Menu},
    {path:"/GameTable", name: GameTable, component: GameTable},
    {path:"/GameRules", name: GameRules, component: GameRules},
    {path:"/Matching", name: Matching, component: Matching}
]

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;