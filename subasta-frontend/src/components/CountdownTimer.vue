<template>
  <div
    class="flex items-center gap-1 font-mono tabular-nums text-sm"
    :class="isUrgent ? 'text-red-400' : 'text-gray-400'"
  >
    <template v-if="!isExpired">
      <span v-if="days > 0">{{ days }}d&nbsp;</span>
      <span v-if="days > 0 || hours > 0">{{ pad(hours) }}h&nbsp;</span>
      <span>{{ pad(minutes) }}m&nbsp;</span>
      <span :class="isUrgent ? 'animate-pulse' : ''">{{ pad(seconds) }}s</span>
    </template>
    <span v-else class="text-red-500 font-semibold">Cerrada</span>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'

const props = defineProps({
  date: { type: String, required: true },
})

const remaining = ref(0)
let timer = null

function update() {
  remaining.value = Math.max(0, new Date(props.date) - new Date())
}

onMounted(() => {
  update()
  timer = setInterval(update, 1000)
})

onUnmounted(() => clearInterval(timer))

const days = computed(() => Math.floor(remaining.value / 86400000))
const hours = computed(() => Math.floor((remaining.value % 86400000) / 3600000))
const minutes = computed(() => Math.floor((remaining.value % 3600000) / 60000))
const seconds = computed(() => Math.floor((remaining.value % 60000) / 1000))
const isExpired = computed(() => remaining.value === 0)
const isUrgent = computed(() => remaining.value > 0 && remaining.value < 3600000)

function pad(n) {
  return String(n).padStart(2, '0')
}
</script>
