<template>
  <div>

    <!-- Hero -->
    <div class="mb-10 animate-fade-in-up">
      <div class="inline-flex items-center gap-2 text-xs text-indigo-400 bg-indigo-950/60 border border-indigo-900/50 rounded-full px-3 py-1 mb-4 font-medium">
        <span class="w-1.5 h-1.5 rounded-full bg-indigo-400 animate-pulse"></span>
        Subastas activas ahora
      </div>

      <h1 class="text-4xl sm:text-5xl font-black leading-tight text-white">
        Compite, puja,<br />
        <span class="text-transparent bg-clip-text bg-gradient-to-r from-indigo-400 to-violet-400">
          gana.
        </span>
      </h1>

      <p class="text-gray-500 mt-3 text-base max-w-md leading-relaxed">
        Encuentra los mejores productos en subasta y haz tu puja en tiempo real.
      </p>

      <div v-if="!store.loading && store.productos.length > 0" class="flex items-center gap-4 mt-6">
        <div class="flex items-center gap-2 text-sm">
          <span class="w-2 h-2 rounded-full bg-green-400 animate-pulse"></span>
          <span class="text-green-400 font-bold">{{ store.productos.length }}</span>
          <span class="text-gray-600">subastas activas</span>
        </div>
        <div class="w-px h-4 bg-gray-800"></div>
        <div class="flex items-center gap-2 text-sm">
          <span class="text-amber-400 font-bold">{{ endingSoon }}</span>
          <span class="text-gray-600">{{ endingSoon === 1 ? 'cierra pronto' : 'cierran pronto' }}</span>
        </div>
      </div>
    </div>

    <!-- Loading -->
    <div v-if="store.loading" class="flex flex-col items-center justify-center py-28 gap-4">
      <div class="w-10 h-10 border-2 border-indigo-500/30 border-t-indigo-500 rounded-full animate-spin"></div>
      <p class="text-sm text-gray-600">Cargando subastas...</p>
    </div>

    <!-- Empty -->
    <div v-else-if="store.productos.length === 0" class="flex flex-col items-center justify-center py-28 gap-3 text-center">
      <div class="w-16 h-16 rounded-2xl bg-gray-900 border border-gray-800 flex items-center justify-center text-3xl">
        🔨
      </div>
      <p class="font-semibold text-gray-300">No hay subastas activas</p>
      <p class="text-sm text-gray-600">Vuelve pronto para ver nuevas subastas</p>
    </div>

    <!-- Grid de subastas -->
    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
      <RouterLink
        v-for="(producto, i) in store.productos"
        :key="producto.id"
        :to="`/producto/${producto.id}`"
        class="group relative bg-gray-900 border border-gray-800 rounded-2xl overflow-hidden hover:border-indigo-500/40 hover:shadow-2xl hover:shadow-indigo-950/30 transition-all duration-300 animate-fade-in-up flex flex-col"
        :style="{ animationDelay: `${i * 60}ms` }"
      >
        <!-- Color accent top bar -->
        <div class="h-1 shrink-0" :class="accentGradients[i % accentGradients.length]"></div>

        <div class="p-5 flex flex-col flex-1">

          <!-- Header -->
          <div class="flex items-start justify-between mb-2 gap-2">
            <h2 class="font-bold text-white group-hover:text-indigo-300 transition-colors leading-snug">
              {{ producto.nombre }}
            </h2>
            <!-- Ending soon badge -->
            <span
              v-if="isEndingSoon(producto.fechaCierre)"
              class="shrink-0 flex items-center gap-1 text-xs text-amber-400 bg-amber-950/60 border border-amber-900/40 px-2 py-0.5 rounded-full font-medium"
            >
              <span class="w-1.5 h-1.5 rounded-full bg-amber-400 animate-pulse"></span>
              Cierra pronto
            </span>
            <span v-else class="shrink-0 text-xs text-green-400 bg-green-950/60 border border-green-900/40 px-2 py-0.5 rounded-full font-medium">
              {{ producto.estado }}
            </span>
          </div>

          <p class="text-gray-500 text-sm mb-4 line-clamp-2 leading-relaxed flex-1">
            {{ producto.descripcion }}
          </p>

          <!-- Precios -->
          <div class="bg-gray-800/40 border border-gray-700/30 rounded-xl px-4 py-3 mb-4">
            <div class="flex items-end justify-between">
              <div>
                <p class="text-xs text-gray-600 mb-0.5">Puja más alta</p>
                <p class="text-2xl font-black text-amber-400 leading-none">
                  ${{ producto.precioActual.toLocaleString() }}
                </p>
              </div>
              <div class="text-right">
                <p class="text-xs text-gray-600 mb-0.5">Base</p>
                <p class="text-sm text-gray-700 line-through">
                  ${{ producto.precioBase.toLocaleString() }}
                </p>
              </div>
            </div>
          </div>

          <!-- Footer con countdown -->
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-1.5 text-gray-600">
              <svg class="w-3.5 h-3.5 shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              <CountdownTimer :date="producto.fechaCierre" />
            </div>
            <span class="flex items-center gap-1 text-xs text-indigo-400 group-hover:text-indigo-300 font-medium transition-colors">
              Ver subasta
              <svg class="w-3.5 h-3.5 transition-transform group-hover:translate-x-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2.5">
                <path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7" />
              </svg>
            </span>
          </div>

        </div>
      </RouterLink>
    </div>

  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useProductosStore } from '../stores/productos'
import CountdownTimer from '../components/CountdownTimer.vue'

const store = useProductosStore()

const accentGradients = [
  'bg-gradient-to-r from-indigo-500 to-violet-500',
  'bg-gradient-to-r from-amber-500 to-orange-500',
  'bg-gradient-to-r from-emerald-500 to-teal-500',
  'bg-gradient-to-r from-pink-500 to-rose-500',
  'bg-gradient-to-r from-sky-500 to-blue-500',
  'bg-gradient-to-r from-purple-500 to-fuchsia-500',
]

function isEndingSoon(fechaCierre) {
  const remaining = new Date(fechaCierre) - new Date()
  return remaining > 0 && remaining < 3600000
}

const endingSoon = computed(() =>
  store.productos.filter(p => isEndingSoon(p.fechaCierre)).length
)

onMounted(() => store.cargarActivos())
</script>
