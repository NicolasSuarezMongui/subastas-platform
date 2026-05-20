<template>
  <div v-if="productosStore.loading" class="flex justify-center py-20">
    <div
      class="w-8 h-8 border-4 border-indigo-500 border-t-transparent rounded-full animate-spin"
    ></div>
  </div>

  <div v-else-if="producto" class="grid grid-cols-1 lg:grid-cols-3 gap-8">
    <!-- Info del producto -->
    <div class="lg:col-span-2 space-y-6">
      <RouterLink
        to="/"
        class="text-sm text-gray-500 hover:text-indigo-400 transition-colors"
      >
        ← Volver a subastas
      </RouterLink>

      <div class="bg-gray-900 border border-gray-800 rounded-xl p-6">
        <div class="flex items-start justify-between mb-4">
          <h1 class="text-2xl font-bold text-white">{{ producto.nombre }}</h1>
          <span
            class="text-xs bg-green-900 text-green-400 px-2 py-1 rounded-full"
          >
            {{ producto.estado }}
          </span>
        </div>

        <p class="text-gray-400 mb-6">{{ producto.descripcion }}</p>

        <div class="grid grid-cols-2 gap-4">
          <div class="bg-gray-800 rounded-lg p-4">
            <p class="text-xs text-gray-500 mb-1">Precio actual</p>
            <p class="text-3xl font-bold text-indigo-400">
              ${{ producto.precioActual.toLocaleString() }}
            </p>
          </div>
          <div class="bg-gray-800 rounded-lg p-4">
            <p class="text-xs text-gray-500 mb-1">Precio base</p>
            <p class="text-3xl font-bold text-gray-300">
              ${{ producto.precioBase.toLocaleString() }}
            </p>
          </div>
        </div>

        <div class="mt-4 text-sm text-gray-500">
          Cierra:
          {{
            new Date(producto.fechaCierre).toLocaleDateString("es-CO", {
              day: "2-digit",
              month: "long",
              year: "numeric",
            })
          }}
        </div>
      </div>

      <!-- Historial de pujas -->
      <div class="bg-gray-900 border border-gray-800 rounded-xl p-6">
        <h2 class="font-semibold text-white mb-4">Historial de pujas</h2>

        <div
          v-if="pujasStore.pujas.length === 0"
          class="text-center py-8 text-gray-500 text-sm"
        >
          Sé el primero en pujar
        </div>

        <div v-else class="space-y-3">
          <div
            v-for="(puja, index) in pujasStore.pujas"
            :key="puja.id"
            class="flex items-center justify-between py-3 border-b border-gray-800 last:border-0"
          >
            <div class="flex items-center gap-3">
              <span
                class="w-6 h-6 rounded-full flex items-center justify-center text-xs font-bold"
                :class="
                  index === 0
                    ? 'bg-indigo-600 text-white'
                    : 'bg-gray-800 text-gray-400'
                "
              >
                {{ index + 1 }}
              </span>
              <span class="text-sm text-gray-300">{{ puja.username }}</span>
            </div>
            <div class="text-right">
              <p
                class="font-semibold"
                :class="index === 0 ? 'text-indigo-400' : 'text-gray-400'"
              >
                ${{ puja.monto.toLocaleString() }}
              </p>
              <p class="text-xs text-gray-600">
                {{ new Date(puja.createdAt).toLocaleTimeString("es-CO") }}
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Formulario de puja -->
    <div class="lg:col-span-1">
      <div
        class="bg-gray-900 border border-gray-800 rounded-xl p-6 sticky top-6"
      >
        <h2 class="font-semibold text-white mb-6">Realizar puja</h2>

        <div class="space-y-4">
          <div>
            <label class="block text-xs text-gray-500 mb-1"
              >Tu usuario ID</label
            >
            <input
              v-model.number="form.usuarioId"
              type="number"
              placeholder="Ej: 1"
              class="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-2.5 text-white text-sm focus:outline-none focus:border-indigo-500 transition-colors"
            />
          </div>

          <div>
            <label class="block text-xs text-gray-500 mb-1">
              Tu puja (mínimo ${{ producto.precioActual.toLocaleString() }})
            </label>
            <div class="relative">
              <span
                class="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 text-sm"
                >$</span
              >
              <input
                v-model.number="form.monto"
                type="number"
                placeholder="0.00"
                class="w-full bg-gray-800 border border-gray-700 rounded-lg pl-7 pr-4 py-2.5 text-white text-sm focus:outline-none focus:border-indigo-500 transition-colors"
              />
            </div>
          </div>

          <div
            v-if="pujasStore.error"
            class="bg-red-900/30 border border-red-800 rounded-lg px-4 py-3 text-sm text-red-400"
          >
            {{ pujasStore.error }}
          </div>

          <div
            v-if="pujasStore.exito"
            class="bg-green-900/30 border border-green-800 rounded-lg px-4 py-3 text-sm text-green-400"
          >
            {{ pujasStore.exito }}
          </div>

          <button
            @click="pujar"
            :disabled="!form.usuarioId || !form.monto"
            class="w-full bg-indigo-600 hover:bg-indigo-500 disabled:bg-gray-700 disabled:text-gray-500 disabled:cursor-not-allowed text-white font-semibold py-3 rounded-lg transition-colors"
          >
            Pujar ahora
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from "vue";
import { useRoute } from "vue-router";
import { useProductosStore } from "../stores/productos";
import { usePujasStore } from "../stores/pujas";

const route = useRoute();
const productosStore = useProductosStore();
const pujasStore = usePujasStore();

const producto = computed(() => productosStore.productoActual);

const form = ref({
  usuarioId: null,
  monto: null,
});

async function pujar() {
  await pujasStore.realizarPuja(
    route.params.id,
    form.value.usuarioId,
    form.value.monto,
  );
  if (!pujasStore.error) {
    await productosStore.cargarProducto(route.params.id);
    form.value.monto = null;
  }
}

onMounted(() => {
  const productoId = Number(route.params.id);
  productosStore.cargarProducto(productoId);
  pujasStore.cargarPujas(productoId);

  pujasStore.conectarWebSocket(productoId, (nuevaPuja) => {
    // Agregar la nueva puja al inicio de la lista
    pujasStore.pujas.unshift(nuevaPuja);
    // Actualizar precio del producto en tiempo real
    if (productosStore.productoActual) {
      productosStore.productoActual.precioActual = nuevaPuja.monto;
    }
  });
});

onUnmounted(() => {
  pujasStore.desconectarWebSocket();
});
</script>
