<template>
    <el-button type="success" id="button" size="large" color="#73ef84" plain>
        <el-icon><Search id="document"/></el-icon>
        <h1 id="title">Start Matching!</h1>
    </el-button>
</template>

<script>
import {defineComponent} from "vue";
import {Search} from "@element-plus/icons-vue";

export default defineComponent({
    components: {Search}
})
</script>

<style scoped>
    #document{
        width: 40px;
        height: 40px;
        margin-left: -40px;
        color: hsl(0, 3%, 33%);
    }

    #title{
        color: hsl(0, 3%, 33%);
        margin-right: -40px;
        font-size: 40px;
        font-family: "Times New Roman",serif;
    }

    #button {
        display: flex;
        align-items: center;
    }
</style>