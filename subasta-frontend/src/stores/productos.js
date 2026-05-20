import { defineStore } from "pinia";
import { ref } from "vue";
import { productoApi } from "../api";

export const useProductosStore = defineStore("productos", () => {
  const productos = ref([]);
  const productoActual = ref(null);
  const loading = ref(false);
  const error = ref(null);

  async function cargarActivos() {
    loading.value = true;
    try {
      const res = await productoApi.listarActivos();
      productos.value = res.data;
    } catch (e) {
      error.value = e.response?.data?.error || "Error cargando productos";
    } finally {
      loading.value = false;
    }
  }

  async function cargarProducto(id) {
    loading.value = true;
    try {
      const res = await productoApi.buscar(id);
      productoActual.value = res.data;
    } catch (e) {
      error.value = e.response?.data?.error || "Error cargando producto";
    } finally {
      loading.value = false;
    }
  }

  return {
    productos,
    productoActual,
    loading,
    error,
    cargarActivos,
    cargarProducto,
  };
});
