import './assets/main.css'

import { createApp } from 'vue'
import Menu from './Menu.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

const app = createApp(Menu)
app.use(ElementPlus)
app.mount('#app')
