<template>
  <div class="min-h-screen bg-gray-950 text-white">
    <nav class="bg-gray-900 border-b border-gray-800 px-6 py-4">
      <div class="max-w-6xl mx-auto flex items-center justify-between">
        <RouterLink to="/" class="text-xl font-bold text-indigo-400 tracking-tight">
          SubastasPro
        </RouterLink>
        <span class="text-sm text-gray-400">Subastas en tiempo real</span>
      </div>
    </nav>
    <main class="max-w-6xl mx-auto px-6 py-8">
      <RouterView />
    </main>
  </div>
</template>