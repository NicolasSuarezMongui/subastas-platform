import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api",
});

export const usuarioApi = {
  listar: () => api.get("/usuarios"),
  crear: (data) => api.post("/usuarios", data),
};

export const productoApi = {
  listarActivos: () => api.get("/productos"),
  buscar: (id) => api.get(`/productos/${id}`),
  crear: (data) => api.post("/productos", data),
};

export const pujaApi = {
  listar: (productoId) => api.get(`/productos/${productoId}/pujas`),
  pujar: (productoId, data) => api.post(`/productos/${productoId}/pujas`, data),
};
