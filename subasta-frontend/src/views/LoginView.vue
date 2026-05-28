<template>
    <div class="min-h-screen bg-gray-950 flex items-center justify-center px-4">
        <div class="w-full max-w-md">

            <div class="text-center mb-8">
                <h1 class="text-3xl font-bold text-indigo-400">SubastasPro</h1>
                <p class="text-gray-400 mt-2">Inicia sesión para continuar</p>
            </div>

            <div class="bg-gray-900 border border-gray-800 rounded-xl p-8">
                <div class="space-y-5">

                    <div>
                        <label class="block text-xs text-gray-500 mb-1">Usuario</label>
                        <input
                            v-model="form.username" 
                            type="text"
                            placeholder="tu_usuario"
                            class="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-2.5 text-white focus:outline-none focus:border-indigo-500 transition-colors"
                        >
                    </div>

                    <div>
                        <label class="block text-xs text-gray-500 mb-1">Contraseña</label>
                        <input
                        v-model="form.password"
                        type="password"
                        placeholder="••••••••"
                        @keyup.enter="handleLogin"
                        class="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-2.5 text-white text-sm focus:outline-none focus:border-indigo-500 transition-colors"
                        />
                    </div>

                    <div v-if="error" class="bg-red-900/30 border border-red-800 rounded-lg px-4 py-3 text-sm text-red-400">
                        {{ error }}
                    </div>

                    <button
                        @click="handleLogin"
                        :disabled="loading || !form.username || !form.password"
                        class="w-full bg-indigo-600 hover:bg-indigo-500 disabled:bg-gray-700 disabled:text-gray-500 disabled:cursor-not-allowed text-white font-semibold py-3 rounded-lg transition-colors"
                    >
                        {{ loading ? 'Iniciando sesión...' : 'Iniciar sesión' }}
                    </button>

                </div>

                <p class="text-center text-sm text-gray-500 mt-6">
                    ¿No tienes cuenta?
                    <RouterLink to="/register" class="text-indigo-400 hover:text-indigo-300 transition-colors">
                        Regístrate
                    </RouterLink>
                </p>
            </div>

        </div>
    </div>
</template>

<script setup>
    import { ref } from 'vue';
    import { useAuthStore } from '../stores/auth';

    const authStore = useAuthStore()

    const form = ref({ username: '', password: '' })
    const loading = ref(false)
    const error = ref(null)

    async function handleLogin() {
        if (!form.value.username || !form.value.password) return
        error.value = null
        loading.value = true
        try {
            await authStore.login(form.value)
        } catch (e) {
            error.value = e.response?.data?.error || 'Credenciales incorrectas'
        } finally {
            loading.value = false
        }
    }
</script>