<template>
  <div class="min-h-screen bg-gray-950 text-white">
    <!-- Navbar -->
    <nav class="sticky top-0 z-50 bg-gray-950/80 backdrop-blur-md border-b border-gray-800/60">
      <div class="max-w-6xl mx-auto px-6 h-16 flex items-center justify-between">

        <!-- Logo -->
        <RouterLink to="/" class="flex items-center gap-2.5 group">
          <div class="w-8 h-8 rounded-lg bg-gradient-to-br from-indigo-500 to-violet-600 flex items-center justify-center font-black text-sm shadow-lg shadow-indigo-950">
            S
          </div>
          <span class="font-bold text-lg tracking-tight">
            Subasta<span class="text-indigo-400">Pro</span>
          </span>
        </RouterLink>

        <!-- Right side -->
        <div class="flex items-center gap-3">
          <span class="hidden sm:flex items-center gap-1.5 text-xs text-green-400 bg-green-950/60 border border-green-900/50 px-3 py-1.5 rounded-full font-medium">
            <span class="w-1.5 h-1.5 rounded-full bg-green-400 animate-pulse"></span>
            En vivo
          </span>
        </div>

      </div>
    </nav>

    <!-- Page content -->
    <main class="max-w-6xl mx-auto px-6 py-8">
      <RouterView />
    </main>
  </div>
</template>
