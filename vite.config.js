import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import ipConfig from "@/ipConfig.js";

const ip = ipConfig.LAN_IP_BACK

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    host: '0.0.0.0',
    https:false,
    proxy: {
      '/api/user': {
        target: `http://${ip}`,
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/user/, '/user')
      },
    },
  },
})
