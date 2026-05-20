<template>
  <div>
    <div class="mb-8">
      <h1 class="text-3xl font-bold text-white">Subastas activas</h1>
      <p class="text-gray-400 mt-1">Encuentra productos y haz tu puja</p>
    </div>

    <div v-if="store.loading" class="flex justify-center py-20">
      <div
        class="w-8 h-8 border-4 border-indigo-500 border-t-transparent rounded-full animate-spin"
      ></div>
    </div>

    <div
      v-else-if="store.productos.length === 0"
      class="text-center py-20 text-gray-500"
    >
      No hay subastas activas en este momento.
    </div>

    <div v-else class="grid grid-cols-1 md:grid-col-2 lg:grid-cols-3 gap-6">
      <RouterLink
        v-for="producto in store.productos"
        :key="producto.id"
        :to="`/producto/${producto.id}`"
        class="bg-gray-900 border border-gray-800 rounded-xl p-5 hover:border-indigo-500 transition-colors group"
      >
        <div class="flex items-start justify-between mb-3">
          <h2
            class="font-semibold text-white group-hover:text-indigo-400 transition-colors"
          >
            {{ producto.nombre }}
          </h2>
          <span
            class="text-xs bg-green-900 text-green-400 px-2 py-1 rounded-full"
          >
            {{ producto.estado }}
          </span>
        </div>

        <p class="text-gray-400 text-sm mb-4 line-clamp-2">
          {{ producto.descripcion }}
        </p>

        <div class="flex items-end justify-between">
          <div>
            <p class="text-xs text-gray-500">Precio actual</p>
            <p class="text-2xl font-bold text-indigo-400">
              ${{ producto.precioActual.toLocaleString() }}
            </p>
          </div>
          <div class="text-right">
            <p class="text-xs text-gray-500">Precio base</p>
            <p class="text-sm text-gray-400">
              ${{ producto.precioBase.toLocaleString() }}
            </p>
          </div>
        </div>

        <div class="mt-4 pt-4 border-t border-gray-800 text-xs text-gray-500">
          Cierra:
          {{
            new Date(producto.fechaCierre).toLocaleDateString("es-CO", {
              day: "2-digit",
              month: "short",
              year: "numeric",
            })
          }}
        </div>
      </RouterLink>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import { useProductosStore } from "../stores/productos";

const store = useProductosStore();

onMounted(() => store.cargarActivos());
</script>
