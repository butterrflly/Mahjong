<template>
    <div>
        <router-view></router-view>
    </div>
</template>

<script>
import WebSocketService from "./websocket.js";

export default {
    name: "App",

    // A WebSocket connection is established when a component is mounted
    mounted() {
        WebSocketService.connect();
    },

    //Close the WebSocket connection before the component is destroyed
    beforeUnmount() {
        WebSocketService.close();
    }
}
</script>

<style scoped>

</style>