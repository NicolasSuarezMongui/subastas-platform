<template>
  <div class="min-h-screen bg-gray-950 flex items-center justify-center px-4 relative overflow-hidden">

    <!-- Background decorations -->
    <div class="absolute inset-0 pointer-events-none">
      <div class="absolute top-0 right-1/4 w-[500px] h-[300px] bg-violet-600/10 rounded-full blur-3xl"></div>
      <div class="absolute bottom-0 left-0 w-[400px] h-[300px] bg-indigo-600/8 rounded-full blur-3xl"></div>
    </div>

    <div class="w-full max-w-sm relative">

      <!-- Brand -->
      <div class="text-center mb-8">
        <RouterLink to="/" class="inline-flex items-center gap-2.5 mb-5 group">
          <div class="w-10 h-10 rounded-xl bg-gradient-to-br from-indigo-500 to-violet-600 flex items-center justify-center font-black text-base shadow-xl shadow-indigo-950/60">
            S
          </div>
          <span class="font-bold text-xl tracking-tight">
            Subasta<span class="text-indigo-400">Pro</span>
          </span>
        </RouterLink>
        <h1 class="text-2xl font-bold text-white">Crea tu cuenta</h1>
        <p class="text-gray-500 mt-1.5 text-sm">Empieza a pujar en segundos</p>
      </div>

      <!-- Card -->
      <div class="bg-gray-900/80 border border-gray-800/80 rounded-2xl p-7 backdrop-blur-sm shadow-2xl shadow-black/40">
        <div class="space-y-4">

          <!-- Username -->
          <div>
            <label class="block text-xs font-medium text-gray-400 mb-1.5">Usuario</label>
            <div class="relative">
              <span class="absolute left-3.5 top-1/2 -translate-y-1/2 text-gray-500">
                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                </svg>
              </span>
              <input
                v-model="form.username"
                type="text"
                placeholder="tu_usuario"
                autocomplete="username"
                class="w-full bg-gray-800/80 border border-gray-700/80 rounded-xl pl-10 pr-4 py-2.5 text-white text-sm placeholder-gray-600 focus:outline-none focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500/20 transition-all"
              />
            </div>
          </div>

          <!-- Email -->
          <div>
            <label class="block text-xs font-medium text-gray-400 mb-1.5">Correo electrónico</label>
            <div class="relative">
              <span class="absolute left-3.5 top-1/2 -translate-y-1/2 text-gray-500">
                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                </svg>
              </span>
              <input
                v-model="form.email"
                type="email"
                placeholder="tu@email.com"
                autocomplete="email"
                class="w-full bg-gray-800/80 border border-gray-700/80 rounded-xl pl-10 pr-4 py-2.5 text-white text-sm placeholder-gray-600 focus:outline-none focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500/20 transition-all"
              />
            </div>
          </div>

          <!-- Password -->
          <div>
            <label class="block text-xs font-medium text-gray-400 mb-1.5">Contraseña</label>
            <div class="relative">
              <span class="absolute left-3.5 top-1/2 -translate-y-1/2 text-gray-500">
                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                </svg>
              </span>
              <input
                v-model="form.password"
                :type="showPassword ? 'text' : 'password'"
                placeholder="••••••••"
                autocomplete="new-password"
                @keyup.enter="handleRegister"
                class="w-full bg-gray-800/80 border border-gray-700/80 rounded-xl pl-10 pr-10 py-2.5 text-white text-sm placeholder-gray-600 focus:outline-none focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500/20 transition-all"
              />
              <button
                type="button"
                @click="showPassword = !showPassword"
                class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-500 hover:text-gray-300 transition-colors"
              >
                <svg v-if="!showPassword" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                  <path stroke-linecap="round" stroke-linejoin="round" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                </svg>
                <svg v-else class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 5.411m0 0L21 21" />
                </svg>
              </button>
            </div>
          </div>

          <!-- Error -->
          <div v-if="error" class="flex items-center gap-2 bg-red-950/50 border border-red-900/60 rounded-xl px-3.5 py-2.5">
            <svg class="w-4 h-4 text-red-400 shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <p class="text-sm text-red-400">{{ error }}</p>
          </div>

          <!-- Submit -->
          <button
            @click="handleRegister"
            :disabled="loading || !form.username || !form.email || !form.password"
            class="w-full bg-gradient-to-r from-indigo-600 to-violet-600 hover:from-indigo-500 hover:to-violet-500 disabled:from-gray-700 disabled:to-gray-700 disabled:text-gray-500 disabled:cursor-not-allowed text-white font-semibold py-2.5 rounded-xl transition-all duration-200 shadow-lg shadow-indigo-950/50 mt-1"
          >
            <span v-if="loading" class="flex items-center justify-center gap-2">
              <span class="w-4 h-4 border-2 border-white/20 border-t-white rounded-full animate-spin"></span>
              Creando cuenta...
            </span>
            <span v-else>Crear cuenta</span>
          </button>

        </div>

        <!-- Divider -->
        <div class="flex items-center gap-3 my-5">
          <div class="flex-1 h-px bg-gray-800"></div>
          <span class="text-xs text-gray-600">¿Ya tienes cuenta?</span>
          <div class="flex-1 h-px bg-gray-800"></div>
        </div>

        <RouterLink
          to="/login"
          class="block w-full text-center text-sm bg-gray-800/60 hover:bg-gray-800 border border-gray-700/60 hover:border-gray-700 text-gray-300 hover:text-white py-2.5 rounded-xl font-medium transition-all"
        >
          Iniciar sesión
        </RouterLink>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useAuthStore } from '../stores/auth'

const authStore = useAuthStore()

const form = ref({ username: '', email: '', password: '' })
const loading = ref(false)
const error = ref(null)
const showPassword = ref(false)

async function handleRegister() {
  if (!form.value.username || !form.value.email || !form.value.password) return
  error.value = null
  loading.value = true
  try {
    await authStore.register(form.value)
  } catch (e) {
    error.value = e.response?.data?.error || 'Error al crear la cuenta'
  } finally {
    loading.value = false
  }
}
</script>
