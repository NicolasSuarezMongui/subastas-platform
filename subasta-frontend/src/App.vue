<template>
  <div class="min-h-screen bg-gray-950 text-white">

    <!-- Auth pages (login/register) render without navbar -->
    <RouterView v-if="route.meta.guest" />

    <!-- App shell -->
    <template v-else>
    <nav class="sticky top-0 z-50 bg-gray-950/80 backdrop-blur-md border-b border-gray-800/60">
      <div class="max-w-6xl mx-auto px-6 h-16 flex items-center justify-between">

        <!-- Logo -->
        <RouterLink to="/" class="flex items-center gap-2.5 group">
          <div class="w-8 h-8 rounded-lg bg-gradient-to-br from-indigo-500 to-violet-600 flex items-center justify-center font-black text-sm shadow-lg shadow-indigo-950/50 group-hover:shadow-indigo-800/60 transition-shadow">
            S
          </div>
          <span class="font-bold text-lg tracking-tight">
            Subasta<span class="text-indigo-400">Pro</span>
          </span>
        </RouterLink>

        <!-- Right side -->
        <div class="flex items-center gap-3">

          <!-- Live badge -->
          <span class="hidden sm:flex items-center gap-1.5 text-xs text-green-400 bg-green-950/60 border border-green-900/50 px-3 py-1.5 rounded-full font-medium">
            <span class="w-1.5 h-1.5 rounded-full bg-green-400 animate-pulse"></span>
            En vivo
          </span>

          <!-- Authenticated -->
          <template v-if="authStore.isAuthenticated">
            <div class="h-5 w-px bg-gray-800 hidden sm:block"></div>

            <!-- User chip -->
            <div class="flex items-center gap-2 bg-gray-900 border border-gray-800 rounded-full pl-1 pr-3 py-1">
              <div class="w-6 h-6 rounded-full bg-gradient-to-br from-indigo-500 to-violet-600 flex items-center justify-center text-xs font-bold shrink-0">
                {{ authStore.username?.charAt(0).toUpperCase() }}
              </div>
              <span class="text-sm text-gray-300 font-medium max-w-[100px] truncate">{{ authStore.username }}</span>
              <span v-if="authStore.isAdmin" class="text-xs bg-indigo-900/80 text-indigo-300 border border-indigo-800/50 px-1.5 py-0.5 rounded-full leading-none">Admin</span>
            </div>

            <!-- Logout -->
            <button
              @click="authStore.logout"
              title="Cerrar sesión"
              class="flex items-center gap-1.5 text-sm text-gray-500 hover:text-red-400 transition-colors group"
            >
              <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
              </svg>
              <span class="hidden sm:inline text-xs">Salir</span>
            </button>
          </template>

          <!-- Guest -->
          <template v-else>
            <RouterLink to="/login" class="text-sm text-gray-400 hover:text-white transition-colors px-1">
              Iniciar sesión
            </RouterLink>
            <RouterLink to="/register" class="text-sm bg-indigo-600 hover:bg-indigo-500 text-white px-3.5 py-1.5 rounded-lg font-medium transition-colors">
              Registrarse
            </RouterLink>
          </template>

        </div>
      </div>
    </nav>

    <!-- Page content -->
    <main class="max-w-6xl mx-auto px-6 py-8">
      <RouterView />
    </main>
    </template>

  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
import { useAuthStore } from "./stores/auth";
const route = useRoute()
const authStore = useAuthStore();
</script>
