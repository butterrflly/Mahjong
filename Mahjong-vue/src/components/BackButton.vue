<template>
  <el-button type="primary" id="button" size="small" color="#a6efaa" round text>
    <el-icon><Back id="back"/></el-icon>
  </el-button>
</template>

<script>
import {defineComponent} from "vue";
import {Back} from "@element-plus/icons-vue";

export default defineComponent({
  components: {Back}
})
</script>

<style scoped>
#back{
  width: 30px;
  height: 30px;
  margin: -10px -10px -10px -10px;
  color: hsl(0, 8%, 5%);
}

#button {
  display: flex;
  align-items: center;
}
</style>