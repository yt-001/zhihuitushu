<script setup>
import { ref } from 'vue'
import TopAppBar from '@/components/TopAppBar.vue'
import GlassCard from '@/components/GlassCard.vue'

const categories = ['全部', '文学', '历史', '科技', '艺术', '商业']
const activeCategory = ref('全部')

const guessYouLike = [
  { title: '人类简史', author: '尤瓦尔·赫拉利', tag: '畅销推荐' },
  { title: '原则', author: '瑞·达利欧', tag: '商业经典' },
  { title: '小王子', author: '圣埃克苏佩里', tag: '治愈经典' },
]

const charts = [
  { rank: 1, title: '活着', author: '余华', color: 'text-primary' },
  { rank: 2, title: '围城', author: '钱钟书', color: 'text-secondary' },
  { rank: 3, title: '月亮与六便士', author: '毛姆', color: 'text-tertiary' },
  { rank: 4, title: '追风筝的人', author: '卡勒德·胡赛尼', color: 'text-on-surface-variant' },
  { rank: 5, title: '白夜行', author: '东野圭吾', color: 'text-on-surface-variant' },
]
</script>

<template>
  <div class="min-h-screen flex flex-col gap-stack-gap-lg px-container-margin pt-3 pb-28">
    <TopAppBar title="发现" />

    <!-- Category pills -->
    <nav class="flex overflow-x-auto no-scrollbar gap-inline-gap py-1 -mt-1">
      <button
        v-for="category in categories"
        :key="category"
        class="px-3.5 py-1 rounded-full whitespace-nowrap text-label-md transition-all"
        :class="activeCategory === category ? 'bg-primary text-on-primary shadow-sm scale-105' : 'glass-card text-on-surface-variant hover:bg-white/40'"
        @click="activeCategory = category"
      >
        {{ category }}
      </button>
    </nav>

    <main class="flex flex-col gap-stack-gap-lg">
      <!-- Guess you like -->
      <section class="flex flex-col gap-stack-gap-md">
        <div class="flex justify-between items-end">
          <h2 class="text-headline-md text-on-surface">猜你喜欢</h2>
          <button class="text-primary text-label-md hover:opacity-80">更多</button>
        </div>
        <div class="flex overflow-x-auto no-scrollbar gap-inline-gap snap-x snap-mandatory pb-3">
          <div
            v-for="book in guessYouLike"
            :key="book.title"
            class="min-w-[240px] w-[240px] snap-center glass-card rounded-[14px] p-card-padding flex flex-col gap-stack-gap-sm hover:scale-[1.02] transition-transform shadow-[0_6px_20px_rgba(6,78,59,0.05)] cursor-pointer"
          >
            <div class="w-full aspect-[4/3] rounded-lg overflow-hidden relative bg-gradient-to-br from-primary/15 to-tertiary/15 flex items-center justify-center">
              <span class="material-symbols-outlined text-5xl text-primary/50">auto_stories</span>
            </div>
            <div class="flex flex-col mt-1">
              <span class="text-label-sm text-primary mb-0.5">{{ book.tag }}</span>
              <h3 class="text-body-lg text-on-surface truncate font-bold">{{ book.title }}</h3>
              <p class="text-body-md text-on-surface-variant truncate">{{ book.author }}</p>
            </div>
          </div>
        </div>
      </section>

      <!-- Charts -->
      <section class="flex flex-col gap-stack-gap-md">
        <div class="flex justify-between items-end">
          <h2 class="text-headline-md text-on-surface">阅读榜</h2>
          <button class="text-primary text-label-md hover:opacity-80 flex items-center">
            <span class="material-symbols-outlined text-[16px]">format_list_bulleted</span>
            <span class="ml-1">全部</span>
          </button>
        </div>
        <GlassCard>
          <div class="flex flex-col gap-stack-gap-sm">
            <div
              v-for="book in charts"
              :key="book.title"
              class="flex items-center gap-inline-gap py-2 group cursor-pointer hover:bg-white/40 rounded-lg transition-colors px-2 -mx-2"
            >
              <span :class="`text-headline-md w-5 text-center font-bold ${book.color}`">{{ book.rank }}</span>
              <div class="w-10 h-10 rounded-lg overflow-hidden shrink-0 relative shadow-sm bg-gradient-to-br from-surface-container to-surface-container-high flex items-center justify-center">
                <span class="material-symbols-outlined text-primary text-[18px]">book</span>
              </div>
              <div class="flex flex-col flex-1 min-w-0">
                <h4 class="text-body-lg text-on-surface truncate group-hover:text-primary transition-colors font-semibold">{{ book.title }}</h4>
                <p class="text-body-md text-on-surface-variant truncate">{{ book.author }}</p>
              </div>
              <button class="text-tertiary hover:text-primary transition-colors touch-target">
                <span class="material-symbols-outlined material-symbols-filled text-[20px]">play_arrow</span>
              </button>
            </div>
          </div>
        </GlassCard>
      </section>
    </main>
  </div>
</template>
