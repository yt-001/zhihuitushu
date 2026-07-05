<script setup>
import { ref } from 'vue'
import TopAppBar from '@/components/TopAppBar.vue'
import GlassCard from '@/components/GlassCard.vue'

const stats = [
  { icon: 'favorite', label: '收藏', count: '128', color: 'text-primary' },
  { icon: 'folder', label: '本地', count: '32', color: 'text-tertiary' },
  { icon: 'menu_book', label: '在读', count: '12', color: 'text-secondary' },
  { icon: 'shopping_bag', label: '已购', count: '45', color: 'text-on-surface-variant' },
]

const recentlyRead = [
  { title: '命运', author: '蔡崇达', color: 'from-primary/30 to-secondary/30' },
  { title: '长安的荔枝', author: '马伯庸', color: 'from-secondary/30 to-tertiary/30' },
  { title: '始于极限', author: '上野千鹤子', color: 'from-tertiary/30 to-primary/30' },
]

const bookshelves = [
  { title: '睡前读物', count: 12, icon: 'bedtime', color: 'from-primary to-secondary' },
  { title: '通勤路上', count: 8, icon: 'directions_transit', color: 'from-secondary to-tertiary' },
  { title: '深度学习', count: 24, icon: 'school', color: 'from-tertiary to-primary' },
]
</script>

<template>
  <div class="min-h-screen flex flex-col gap-stack-gap-lg px-container-margin pt-3 pb-28">
    <TopAppBar title="我的书架" :show-search="false" />

    <!-- User profile header -->
    <GlassCard class="rounded-[20px] flex items-center gap-inline-gap shadow-[0_6px_20px_rgba(6,78,59,0.05)]">
      <div class="relative">
        <div class="w-16 h-16 rounded-full overflow-hidden border-2 border-white shadow-sm bg-gradient-to-br from-primary-container to-tertiary-container flex items-center justify-center">
          <span class="material-symbols-outlined text-white text-[28px]">person</span>
        </div>
        <div class="absolute -bottom-0.5 -right-0.5 bg-secondary-container text-on-secondary-container text-label-sm px-1.5 py-0.5 rounded-full border border-white font-bold">
          VIP
        </div>
      </div>
      <div class="flex flex-col">
        <h2 class="text-headline-md">书友小雅</h2>
        <p class="text-body-md text-on-surface-variant mt-0.5">读万卷书，行万里路</p>
        <div class="flex gap-2 mt-1.5">
          <span class="glass-card px-2 py-0.5 rounded-full text-label-sm flex items-center gap-1">
            <span class="material-symbols-outlined text-[12px]">book</span> Lv.8
          </span>
          <span class="glass-card px-2 py-0.5 rounded-full text-label-sm flex items-center gap-1">
            <span class="material-symbols-outlined text-[12px]">group</span> 1.2k 关注
          </span>
        </div>
      </div>
    </GlassCard>

    <!-- Stats row -->
    <section class="grid grid-cols-4 gap-2">
      <div
        v-for="stat in stats"
        :key="stat.label"
        class="glass-card rounded-[14px] p-2.5 flex flex-col items-center justify-center text-center gap-0.5 hover:scale-[1.02] transition-transform cursor-pointer"
      >
        <span class="material-symbols-outlined material-symbols-filled text-[18px]" :class="stat.color">{{ stat.icon }}</span>
        <span class="text-label-sm text-on-surface-variant">{{ stat.label }}</span>
        <span class="text-body-md font-bold">{{ stat.count }}</span>
      </div>
    </section>

    <!-- Recently read -->
    <section class="flex flex-col gap-stack-gap-sm">
      <div class="flex justify-between items-center">
        <h3 class="text-headline-sm text-on-surface">最近阅读</h3>
        <button class="text-primary text-label-md">更多</button>
      </div>
      <div class="flex overflow-x-auto no-scrollbar gap-inline-gap pb-3 -mx-container-margin px-container-margin">
        <div
          v-for="book in recentlyRead"
          :key="book.title"
          class="flex-none w-28 flex flex-col gap-1.5"
        >
          <div class="w-28 h-36 rounded-[14px] overflow-hidden relative shadow-sm bg-gradient-to-br flex items-center justify-center" :class="book.color">
            <span class="material-symbols-outlined text-white text-[32px] opacity-80">book</span>
            <div class="absolute bottom-2 right-2 bg-black/40 backdrop-blur-md rounded-full p-1 flex items-center justify-center">
              <span class="material-symbols-outlined text-white text-[14px] material-symbols-filled">play_arrow</span>
            </div>
          </div>
          <div class="flex flex-col">
            <span class="text-body-md font-semibold truncate">{{ book.title }}</span>
            <span class="text-label-sm text-on-surface-variant truncate">{{ book.author }}</span>
          </div>
        </div>
      </div>
    </section>

    <!-- Bookshelves -->
    <section class="flex flex-col gap-stack-gap-md pb-6">
      <div class="flex justify-between items-center">
        <div class="flex items-center gap-2">
          <h3 class="text-headline-sm text-on-surface">书单</h3>
          <span class="text-label-sm text-on-surface-variant bg-surface-container-high px-2 py-0.5 rounded-full">3</span>
        </div>
        <button class="w-8 h-8 rounded-full bg-primary-container text-on-primary-container flex items-center justify-center hover:scale-105 transition-transform shadow-sm">
          <span class="material-symbols-outlined text-[18px]">add</span>
        </button>
      </div>
      <div class="flex flex-col gap-2.5">
        <div
          v-for="shelf in bookshelves"
          :key="shelf.title"
          class="glass-card rounded-[14px] p-2 flex items-center gap-2.5 hover:scale-[1.01] transition-transform cursor-pointer"
        >
          <div class="w-14 h-14 rounded-[12px] flex-shrink-0 flex items-center justify-center bg-gradient-to-br" :class="shelf.color">
            <span class="material-symbols-outlined text-white text-[28px] material-symbols-filled">{{ shelf.icon }}</span>
          </div>
          <div class="flex flex-col flex-grow justify-center">
            <h4 class="text-body-lg font-bold">{{ shelf.title }}</h4>
            <p class="text-label-md text-on-surface-variant">
              <span>{{ shelf.count }} 本书</span>
            </p>
          </div>
          <button class="p-2 text-on-surface-variant hover:text-primary transition-colors touch-target">
            <span class="material-symbols-outlined">more_vert</span>
          </button>
        </div>
      </div>
    </section>
  </div>
</template>
