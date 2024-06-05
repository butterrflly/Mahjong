<template>
    <div>
        <router-view></router-view>
    </div>
</template>

<script>
import WebSocketService from "@/websocket.js";

export default {
    name: "App",

    // 组件挂载时建立 WebSocket 连接
    mounted() {
        WebSocketService.connect();
    },

    // 组件销毁之前关闭 WebSocket 连接
    beforeUnmount() {
        WebSocketService.close();
    }
}
</script>

<style scoped>

</style>