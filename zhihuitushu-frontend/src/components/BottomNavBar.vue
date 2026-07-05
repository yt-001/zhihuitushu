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
    class="fixed bottom-0 left-0 w-full z-50 flex justify-around items-center px-3 py-2 pb-safe rounded-t-[20px] shadow-[0_-4px_20px_rgba(6,78,59,0.06)] glass-nav transition-all duration-300 ease-out"
  >
    <button
      v-for="item in navItems"
      :key="item.name"
      class="relative flex flex-col items-center justify-center transition-all duration-300 ease-out touch-target"
      :class="[
        route.name === item.name
          ? 'text-primary scale-105'
          : 'text-on-surface-variant opacity-70 hover:opacity-100',
      ]"
      @click="goTo(item.name)"
    >
      <span
        class="material-symbols-outlined text-[22px]"
        :class="route.name === item.name ? 'material-symbols-filled' : ''"
      >
        {{ item.icon }}
      </span>
      <span class="text-[10px] font-medium mt-0.5">{{ item.label }}</span>
      <div
        v-if="route.name === item.name"
        class="absolute -bottom-0.5 w-1.5 h-1.5 bg-primary rounded-full"
      />
    </button>
  </nav>
</template>
