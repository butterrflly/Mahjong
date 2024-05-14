<script setup>
defineProps({
  msg: {
    type: String,
    required: true
  }
})
</script>

<template>
  <div class="greetings">
    <h1 class="red">{{ msg }} <sub>  (Beijing Version) </sub></h1>
  </div>
</template>

<style scoped>
h1 {
  font-weight: 700;
  font-size: 3rem;
  position: fixed;
  top: 45px;
  left: 500px;
  text-align: center;
}

.red {
  text-align: center;
}
</style>
