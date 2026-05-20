import { defineStore } from "pinia";
import { ref } from "vue";
import { pujaApi } from "../api";

export const usePujasStore = defineStore("pujas", () => {
  const pujas = ref([]);
  const loading = ref(false);
  const error = ref(null);
  const exito = ref(null);
  let socket = null;

  async function cargarPujas(productoId) {
    loading.value = true;
    try {
      const res = await pujaApi.listar(productoId);
      pujas.value = res.data;
    } catch (e) {
      error.value = e.response?.data?.error || "Error cargando pujas";
    } finally {
      loading.value = false;
    }
  }

  async function realizarPuja(productoId, usuarioId, monto) {
    error.value = null;
    exito.value = null;
    try {
      await pujaApi.pujar(productoId, { usuarioId, monto });
      exito.value = "¡Puja realizada con éxito!";
      await cargarPujas(productoId); // Recargar pujas después de realizar una
    } catch (e) {
      error.value = e.response?.data?.error || "Error realizando puja";
    }
  }

  function conectarWebSocket(productoId, onPujaRecibida) {
    if (socket) socket.close();

    socket = new WebSocket("ws://localhost:8081/ws/pujas");

    socket.onopen = () => {
      console.log("WebSocket conectado");
    };

    socket.onmessage = (event) => {
      const puja = JSON.parse(event.data);
      if (puja.productoId === productoId) {
        onPujaRecibida(puja);
      }
    };

    socket.onerror = (e) => console.error("WebSocket error:", e);

    socket.onclose = () => console.log("WebSocket desconectado");
  }

  function desconectarWebSocket() {
    if (socket) {
      socket.close();
      socket = null;
    }
  }

  return {
    pujas,
    loading,
    error,
    exito,
    cargarPujas,
    realizarPuja,
    conectarWebSocket,
    desconectarWebSocket,
  };
});
