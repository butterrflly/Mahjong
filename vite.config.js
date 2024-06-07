import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import ipAddress from "./ipAddress.js";

const ip = ipAddress.LAN_IP_BACK

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],

  server: {
    host: '0.0.0.0',
    https:false,
    proxy: {
      '/api/user': {
        target: `http://${ip}/`,
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/user/, '/user')
      },
    },
  },
})
