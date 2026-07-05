<script setup>
defineProps({
  title: { type: String, default: '智慧图书' },
  showBack: { type: Boolean, default: false },
  showSearch: { type: Boolean, default: true },
  showNotice: { type: Boolean, default: true },
})

const emit = defineEmits(['back', 'search', 'notice'])
</script>

<template>
  <header class="sticky top-0 z-40 flex flex-col gap-stack-gap-sm px-container-margin pt-3 bg-transparent">
    <div class="flex items-center justify-between w-full">
      <button
        v-if="showBack"
        class="text-primary hover:opacity-80 scale-105 active:scale-95 transition-transform touch-target"
        @click="emit('back')"
      >
        <span class="material-symbols-outlined text-2xl">arrow_back</span>
      </button>
      <h1 v-else class="text-display-lg font-extrabold text-on-surface">{{ title }}</h1>

      <div class="flex items-center gap-inline-gap text-primary">
        <button
          v-if="showSearch"
          class="hover:opacity-80 scale-105 active:scale-95 transition-transform touch-target"
          @click="emit('search')"
        >
          <span class="material-symbols-outlined">search</span>
        </button>
        <button
          v-if="showNotice"
          class="hover:opacity-80 scale-105 active:scale-95 transition-transform touch-target"
          @click="emit('notice')"
        >
          <span class="material-symbols-outlined">notifications</span>
        </button>
      </div>
    </div>
  </header>
</template>
