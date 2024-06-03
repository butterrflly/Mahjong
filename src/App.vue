<template>
    <div>
        <router-view></router-view>
    </div>
</template>

<script>
import WebSocketService from "@/websocket.js";

export default {
    name: "App",

    method: {

    },

    // 组件挂载时建立 WebSocket 连接
    mounted() {
        console.log("1111111");
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