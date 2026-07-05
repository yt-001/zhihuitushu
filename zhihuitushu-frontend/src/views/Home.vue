<script setup>
import { ref } from 'vue'
import TopAppBar from '@/components/TopAppBar.vue'

const tabs = ['推荐', '畅销', '新书', '分类']
const activeTab = ref('推荐')

const quickActions = [
  { icon: 'auto_stories', label: '小说', color: 'text-primary' },
  { icon: 'psychology', label: '心理学', color: 'text-tertiary' },
  { icon: 'business', label: '商业', color: 'text-secondary' },
  { icon: 'science', label: '科普', color: 'text-primary-container' },
  { icon: 'palette', label: '艺术', color: 'text-on-tertiary-container' },
]

const heroBooks = [
  { title: '三体', author: '刘慈欣', tag: '科幻经典', color: 'bg-primary/80' },
  { title: '百年孤独', author: '马尔克斯', tag: '诺奖作品', color: 'bg-tertiary-container/80' },
]

const staffPicks = [
  { title: '沉默的大多数', author: '王小波', readers: '12.8万', color: 'from-secondary-container to-primary-fixed' },
  { title: '置身事内', author: '兰小欢', readers: '8.5万', color: 'from-tertiary-container to-surface-container' },
  { title: '也许你该找个人聊聊', author: '洛莉·戈特利布', readers: '6.2万', color: 'from-primary-container to-secondary-fixed' },
]
</script>

<template>
  <div class="min-h-screen flex flex-col gap-stack-gap-lg px-container-margin pt-4 pb-32">
    <TopAppBar title="智慧图书" />

    <!-- Top navigation pills -->
    <nav class="flex overflow-x-auto no-scrollbar gap-inline-gap py-2 -mt-2">
      <button
        v-for="tab in tabs"
        :key="tab"
        class="px-4 py-1 rounded-full whitespace-nowrap text-label-md transition-all"
        :class="activeTab === tab ? 'bg-primary text-on-primary shadow-sm' : 'glass-card text-on-surface-variant hover:bg-white/70'"
        @click="activeTab = tab"
      >
        {{ tab }}
      </button>
    </nav>

    <!-- Hero carousel -->
    <section class="w-full relative">
      <div class="flex overflow-x-auto gap-4 snap-x snap-mandatory no-scrollbar pb-4">
        <div
          v-for="book in heroBooks"
          :key="book.title"
          class="snap-center shrink-0 w-[85%] relative rounded-[24px] overflow-hidden glass-card group"
        >
          <div class="relative aspect-[4/3] w-full p-card-padding flex flex-col justify-end">
            <div :class="`absolute inset-0 bg-gradient-to-br ${book.color === 'bg-primary/80' ? 'from-primary/30 to-tertiary-container/30' : 'from-tertiary-container/30 to-secondary-container/30'} z-0`" />
            <div class="absolute inset-0 bg-gradient-to-t from-black/60 to-transparent z-0" />
            <div class="relative z-10 text-white">
              <span :class="`backdrop-blur-md px-3 py-1 rounded-full text-label-sm inline-block mb-2 uppercase tracking-wider ${book.color}`">{{ book.tag }}</span>
              <h2 class="text-headline-md font-bold mb-1">{{ book.title }}</h2>
              <p class="text-body-md opacity-90">{{ book.author }}</p>
            </div>
            <button class="absolute bottom-4 right-4 w-12 h-12 bg-primary rounded-full flex items-center justify-center text-white shadow-lg hover:scale-105 transition-transform z-10">
              <span class="material-symbols-outlined material-symbols-filled">play_arrow</span>
            </button>
          </div>
        </div>
      </div>
    </section>

    <!-- Quick access icon grid -->
    <section class="grid grid-cols-5 gap-2 px-2">
      <div
        v-for="action in quickActions"
        :key="action.label"
        class="flex flex-col items-center gap-2 cursor-pointer group"
      >
        <div class="w-14 h-14 rounded-full glass-card flex items-center justify-center bg-white/60 group-hover:scale-105 transition-transform">
          <span class="material-symbols-outlined text-[28px]" :class="action.color">{{ action.icon }}</span>
        </div>
        <span class="text-label-sm text-on-surface-variant">{{ action.label }}</span>
      </div>
    </section>

    <!-- Staff picks bento grid -->
    <section class="flex flex-col gap-4">
      <h3 class="text-headline-sm text-on-surface">编辑精选</h3>
      <div class="grid grid-cols-2 gap-4">
        <!-- Large card -->
        <div class="col-span-2 glass-card rounded-[20px] p-4 flex gap-4 items-center cursor-pointer hover:bg-white/50 transition-colors">
          <div class="w-20 h-20 rounded-xl overflow-hidden shrink-0 relative bg-gradient-to-br from-primary to-tertiary flex items-center justify-center">
            <span class="material-symbols-outlined text-white text-4xl">menu_book</span>
          </div>
          <div class="flex-1">
            <h4 class="text-body-lg font-bold text-on-surface mb-1">蛤蟆先生去看心理医生</h4>
            <p class="text-label-md text-on-surface-variant mb-2">罗伯特·戴博德</p>
            <div class="flex items-center gap-1 text-label-sm text-on-surface-variant opacity-70">
              <span class="material-symbols-outlined text-[14px]">headphones</span>
              <span>24.5万人在读</span>
            </div>
          </div>
          <button class="w-10 h-10 rounded-full border border-primary text-primary flex items-center justify-center hover:bg-primary hover:text-white transition-colors">
            <span class="material-symbols-outlined">play_arrow</span>
          </button>
        </div>

        <!-- Small cards -->
        <div
          v-for="book in staffPicks"
          :key="book.title"
          class="glass-card rounded-[16px] p-3 flex flex-col gap-3 relative overflow-hidden"
        >
          <div class="w-full aspect-square rounded-lg bg-surface-container overflow-hidden relative z-10 flex items-center justify-center">
            <div :class="`absolute inset-0 bg-gradient-to-br ${book.color} opacity-80`" />
            <span class="material-symbols-outlined text-white text-4xl relative z-10">book</span>
          </div>
          <div class="relative z-10 bg-white/40 backdrop-blur-sm -mt-6 p-2 rounded-lg border border-white/50">
            <h4 class="text-body-md font-bold truncate">{{ book.title }}</h4>
            <p class="text-label-sm text-on-surface-variant truncate">{{ book.author }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Bottom spacing -->
    <div class="h-[80px]" />
  </div>
</template>
