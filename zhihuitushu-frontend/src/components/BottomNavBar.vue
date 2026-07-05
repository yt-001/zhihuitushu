<script setup>
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const navItems = [
  { name: 'home', label: '首页', icon: 'home' },
  { name: 'explore', label: '发现', icon: 'search' },
  { name: 'ai', label: 'AI', icon: 'auto_awesome' },
  { name: 'library', label: '书架', icon: 'menu_book' },
  { name: 'profile', label: '我的', icon: 'person' },
]

function goTo(name) {
  router.push({ name })
}
</script>

<template>
  <nav
    class="fixed bottom-0 left-0 w-full z-50 flex justify-around items-center px-4 py-3 pb-safe rounded-t-2xl shadow-lg glass-nav transition-all duration-300 ease-out"
  >
    <button
      v-for="item in navItems"
      :key="item.name"
      class="flex flex-col items-center gap-1 transition-all duration-300 ease-out"
      :class="[
        route.name === item.name
          ? 'text-primary scale-110'
          : 'text-on-surface-variant opacity-70 hover:opacity-100',
      ]"
      @click="goTo(item.name)"
    >
      <span
        class="material-symbols-outlined text-[24px]"
        :class="route.name === item.name ? 'material-symbols-filled' : ''"
      >
        {{ item.icon }}
      </span>
      <span class="text-label-sm font-medium">{{ item.label }}</span>
      <div
        v-if="route.name === item.name"
        class="absolute -bottom-1 w-1.5 h-1.5 bg-primary rounded-full"
      />
    </button>
  </nav>
</template>
