<template>
  <!-- Loading -->
  <div v-if="productosStore.loading" class="flex flex-col items-center justify-center py-28 gap-4">
    <div class="w-10 h-10 border-2 border-indigo-500/30 border-t-indigo-500 rounded-full animate-spin"></div>
    <p class="text-sm text-gray-500">Cargando subasta...</p>
  </div>

  <!-- Contenido -->
  <div v-else-if="producto" class="animate-fade-in-up">

    <!-- Back link -->
    <RouterLink
      to="/"
      class="inline-flex items-center gap-1.5 text-sm text-gray-500 hover:text-indigo-400 transition-colors mb-6 group"
    >
      <svg
        class="w-4 h-4 transition-transform group-hover:-translate-x-0.5"
        fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"
      >
        <path stroke-linecap="round" stroke-linejoin="round" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
      </svg>
      Volver a subastas
    </RouterLink>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">

      <!-- Columna izquierda: info + historial -->
      <div class="lg:col-span-2 space-y-5">

        <!-- Tarjeta principal del producto -->
        <div class="bg-gray-900 border border-gray-800 rounded-2xl overflow-hidden">
          <div class="h-1.5 bg-gradient-to-r from-indigo-500 via-violet-500 to-purple-500"></div>

          <div class="p-6">
            <!-- Título + badge -->
            <div class="flex items-start justify-between gap-3 mb-3">
              <h1 class="text-2xl font-black text-white leading-tight">{{ producto.nombre }}</h1>
              <span class="shrink-0 flex items-center gap-1.5 text-xs text-green-400 bg-green-950/60 border border-green-900/50 px-2.5 py-1 rounded-full font-medium">
                <span class="w-1.5 h-1.5 rounded-full bg-green-400 animate-pulse"></span>
                {{ producto.estado }}
              </span>
            </div>

            <p class="text-gray-400 leading-relaxed mb-6">{{ producto.descripcion }}</p>

            <!-- Precios -->
            <div class="grid grid-cols-2 gap-3 mb-5">
              <div class="bg-gray-800/50 border border-gray-700/40 rounded-xl p-4">
                <p class="text-xs text-gray-500 uppercase tracking-wide mb-1.5">Puja más alta</p>
                <p class="text-3xl font-black text-amber-400">
                  ${{ producto.precioActual.toLocaleString() }}
                </p>
              </div>
              <div class="bg-gray-800/50 border border-gray-700/40 rounded-xl p-4">
                <p class="text-xs text-gray-500 uppercase tracking-wide mb-1.5">Precio base</p>
                <p class="text-3xl font-black text-gray-500">
                  ${{ producto.precioBase.toLocaleString() }}
                </p>
              </div>
            </div>

            <!-- Countdown -->
            <div class="flex items-center justify-between bg-gray-800/40 border border-gray-700/30 rounded-xl px-4 py-3">
              <div class="flex items-center gap-2 text-sm text-gray-400">
                <svg class="w-4 h-4 text-indigo-400 shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                Cierra en:
              </div>
              <CountdownTimer :date="producto.fechaCierre" />
            </div>
          </div>
        </div>

        <!-- Historial de pujas -->
        <div class="bg-gray-900 border border-gray-800 rounded-2xl p-6">
          <div class="flex items-center justify-between mb-5">
            <h2 class="font-bold text-white">Historial de pujas</h2>
            <span class="text-xs text-gray-500 bg-gray-800 rounded-full px-2.5 py-1">
              {{ pujasStore.pujas.length }} {{ pujasStore.pujas.length === 1 ? 'puja' : 'pujas' }}
            </span>
          </div>

          <!-- Sin pujas -->
          <div
            v-if="pujasStore.pujas.length === 0"
            class="flex flex-col items-center justify-center py-12 gap-3 text-center"
          >
            <div class="w-12 h-12 rounded-xl bg-gray-800 border border-gray-700 flex items-center justify-center text-xl">
              🔨
            </div>
            <p class="text-gray-300 text-sm font-medium">Sé el primero en pujar</p>
            <p class="text-xs text-gray-600">Nadie ha pujado aún en esta subasta</p>
          </div>

          <!-- Lista de pujas -->
          <div v-else class="space-y-2">
            <div
              v-for="(puja, index) in pujasStore.pujas"
              :key="puja.id"
              class="flex items-center justify-between p-3 rounded-xl border transition-colors"
              :class="index === 0
                ? 'bg-indigo-950/40 border-indigo-800/30'
                : 'bg-gray-800/20 border-transparent'"
            >
              <div class="flex items-center gap-3">
                <span
                  class="w-7 h-7 rounded-full flex items-center justify-center text-xs font-black shrink-0"
                  :class="index === 0 ? 'bg-amber-400 text-gray-950' : 'bg-gray-700 text-gray-400'"
                >
                  {{ index + 1 }}
                </span>
                <div>
                  <p class="text-sm font-semibold" :class="index === 0 ? 'text-white' : 'text-gray-300'">
                    {{ puja.username }}
                  </p>
                  <p class="text-xs text-gray-600">
                    {{ new Date(puja.createdAt).toLocaleTimeString('es-CO') }}
                  </p>
                </div>
              </div>

              <div class="text-right">
                <p class="font-bold" :class="index === 0 ? 'text-amber-400' : 'text-gray-500'">
                  ${{ puja.monto.toLocaleString() }}
                </p>
                <p v-if="index === 0" class="text-xs text-indigo-400 font-medium">Ganando</p>
              </div>
            </div>
          </div>
        </div>

      </div>

      <!-- Columna derecha: formulario de puja -->
      <div class="lg:col-span-1">
        <div class="bg-gray-900 border border-gray-800 rounded-2xl overflow-hidden sticky top-24">
          <div class="h-1 bg-gradient-to-r from-amber-500 to-orange-500"></div>

          <div class="p-6">
            <h2 class="font-bold text-white mb-1">Realizar puja</h2>
            <p class="text-xs text-gray-500 mb-6">
              Mínimo requerido:
              <span class="text-amber-400 font-semibold">
                ${{ producto.precioActual.toLocaleString() }}
              </span>
            </p>

            <div class="space-y-4">

              <!-- Usuario ID -->
              <div>
                <label class="block text-xs font-medium text-gray-400 mb-1.5">Usuario ID</label>
                <input
                  v-model.number="form.usuarioId"
                  type="number"
                  placeholder="Ej: 1"
                  class="w-full bg-gray-800 border border-gray-700 rounded-xl px-4 py-3 text-white text-sm placeholder-gray-600 focus:outline-none focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500/20 transition-all"
                />
              </div>

              <!-- Monto -->
              <div>
                <label class="block text-xs font-medium text-gray-400 mb-1.5">Tu puja</label>
                <div class="relative">
                  <span class="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 text-sm font-semibold">$</span>
                  <input
                    v-model.number="form.monto"
                    type="number"
                    placeholder="0"
                    class="w-full bg-gray-800 border border-gray-700 rounded-xl pl-8 pr-4 py-3 text-white text-sm placeholder-gray-600 focus:outline-none focus:border-amber-500 focus:ring-1 focus:ring-amber-500/20 transition-all"
                  />
                </div>
              </div>

              <!-- Error -->
              <div
                v-if="pujasStore.error"
                class="flex items-start gap-2 bg-red-950/40 border border-red-900/50 rounded-xl p-3"
              >
                <svg class="w-4 h-4 text-red-400 shrink-0 mt-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <p class="text-sm text-red-400">{{ pujasStore.error }}</p>
              </div>

              <!-- Éxito -->
              <div
                v-if="pujasStore.exito"
                class="flex items-start gap-2 bg-green-950/40 border border-green-900/50 rounded-xl p-3"
              >
                <svg class="w-4 h-4 text-green-400 shrink-0 mt-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <p class="text-sm text-green-400">{{ pujasStore.exito }}</p>
              </div>

              <!-- Botón -->
              <button
                @click="pujar"
                :disabled="!form.usuarioId || !form.monto || pujasStore.loading"
                class="w-full bg-gradient-to-r from-indigo-600 to-violet-600 hover:from-indigo-500 hover:to-violet-500 disabled:from-gray-700 disabled:to-gray-700 disabled:text-gray-500 disabled:cursor-not-allowed text-white font-bold py-3.5 rounded-xl transition-all duration-200 shadow-lg shadow-indigo-950 hover:shadow-indigo-900/60"
              >
                <span v-if="pujasStore.loading" class="flex items-center justify-center gap-2">
                  <span class="w-4 h-4 border-2 border-white/20 border-t-white rounded-full animate-spin"></span>
                  Enviando...
                </span>
                <span v-else>Pujar ahora</span>
              </button>

            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import { useProductosStore } from '../stores/productos'
import { usePujasStore } from '../stores/pujas'
import CountdownTimer from '../components/CountdownTimer.vue'

const route = useRoute()
const productosStore = useProductosStore()
const pujasStore = usePujasStore()

const producto = computed(() => productosStore.productoActual)

const form = ref({ usuarioId: null, monto: null })

async function pujar() {
  await pujasStore.realizarPuja(route.params.id, form.value.usuarioId, form.value.monto)
  if (!pujasStore.error) {
    await productosStore.cargarProducto(route.params.id)
    form.value.monto = null
  }
}

onMounted(() => {
  const productoId = Number(route.params.id)
  productosStore.cargarProducto(productoId)
  pujasStore.cargarPujas(productoId)

  pujasStore.conectarWebSocket(productoId, (nuevaPuja) => {
    pujasStore.pujas.unshift(nuevaPuja)
    if (productosStore.productoActual) {
      productosStore.productoActual.precioActual = nuevaPuja.monto
    }
  })
})

onUnmounted(() => {
  pujasStore.desconectarWebSocket()
})
</script>
