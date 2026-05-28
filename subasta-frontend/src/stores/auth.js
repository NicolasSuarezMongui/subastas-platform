import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '../api'
import router from '../router'

export const useAuthStore = defineStore('auth', () => {
    const accessToken = ref(localStorage.getItem('accessToken') || null)
    const refreshToken = ref(localStorage.getItem('refreshToken') || null)
    const username = ref(localStorage.getItem('username') || null)
    const role = ref(localStorage.getItem('role') || null)

    const isAuthenticated = computed(() => !!accessToken.value)
    const isAdmin = computed(() => role.value === 'ROLE_ADMIN')

    function setAuth(data) {
        accessToken.value = data.accessToken
        refreshToken.value = data.refreshToken
        username.value = data.username
        role.value = data.role

        localStorage.setItem('accessToken', data.accessToken)
        localStorage.setItem('refreshToken', data.refreshToken)
        localStorage.setItem('username', data.username)
        localStorage.setItem('role', data.role)
    }

    function clearAuth() {
        accessToken.value = null
        refreshToken.value = null
        username.value = null
        role.value = null

        localStorage.removeItem('accessToken')
        localStorage.removeItem('refreshToken')
        localStorage.removeItem('username')
        localStorage.removeItem('role')
    }

    async function login(credentials) {
        const res = await authApi.login(credentials)
        setAuth(res.data)
        router.push('/')
    }

    async function register(data) {
        const res = await authApi.register(data)
        setAuth(res.data)
        router.push('/')
    }

    async function logout() {
        try {
            if (refreshToken.value) {
                await authApi.logout({ refreshToken: refreshToken.value })
            }
        } finally {
            clearAuth()
            router.push('/login')
        }
    }

    async function refresh() {
        try {
            const res = await authApi.refresh({ refreshToken: refreshToken.value })
            setAuth(res.data)
            return res.data.accessToken
        } catch {
            clearAuth()
            router.push('/login')
            return null
        }
    }

    return {
        accessToken, refreshToken, username, role,
        isAuthenticated, isAdmin,
        login, register, logout, refresh
    }
})