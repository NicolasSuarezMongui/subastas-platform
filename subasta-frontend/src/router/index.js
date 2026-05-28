import { createRouter, createWebHistory } from "vue-router";
import { useAuthStore } from "../stores/auth.js";
import HomeView from "../views/HomeView.vue";
import ProductoView from "../views/ProductoView.vue";
import LoginView from "../views/LoginView.vue";
import RegisterView from "../views/RegisterView.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: "/", name: "home", component: HomeView , meta: { requiresAuth: true }},
    { path: "/producto/:id", name: "producto", component: ProductoView, meta: { requiresAuth: true }},
    { path: "/login", name: "login", component: LoginView, meta: { guest: true }},
    { path: "/register", component: RegisterView, meta: { guest: true }}
  ],
});

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next('/login')
  } else if (to.meta.guest && authStore.isAuthenticated) {
    next('/')
  } else {
    next()
  }
})

export default router;
