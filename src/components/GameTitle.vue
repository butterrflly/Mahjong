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
    <h1 class="black">{{ msg }} </h1>
  </div>
</template>

<style scoped>
h1 {
  font-weight: 700;
  font-size: 5rem;
  position: fixed;
  top: 80px;
  left: 480px;
  text-align: center;
}
</style>
