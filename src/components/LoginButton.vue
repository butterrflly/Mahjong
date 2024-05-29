<template>
    <el-button type="primary" id="button" size="large" color="#000000" :dark="isDark" round text>
        <el-icon><UserFilled id="user"/></el-icon>
        <h1 id="title">Login</h1>
    </el-button>
</template>

<script>
import {UserFilled} from "@element-plus/icons-vue";
import {defineComponent} from "vue";

export default defineComponent ({
    components: {UserFilled}
})
</script>

<style scoped>
#user{
    width: 40px;
    height: 40px;
    margin-left: -40px;
    color: hsl(0, 8%, 5%);
}

#title{
    color: hsl(0, 0%, 0%);
    margin-right: -40px;
    font-size: 40px;
    font-family: "Times New Roman",serif;
}

#button {
    display: flex;
    align-items: center;
}
</style>