<script setup>
import { ref, onMounted, watch } from 'vue'
import TopAppBar from '@/components/TopAppBar.vue'
import { getBanners, getHomeCategories } from '@/api/home.js'
import { getBooks } from '@/api/book.js'

const tabs = [
  { label: '推荐', value: 'recommend' },
  { label: '畅销', value: 'hot' },
  { label: '新书', value: 'new' },
]
const activeTab = ref('recommend')

const banners = ref([])
const categories = ref([])
const books = ref([])
const loading = ref(false)
const errorMsg = ref('')

const categoryIconMap = {
  '文学小说': 'auto_stories',
  '历史传记': 'history',
  '科普读物': 'science',
  '自我提升': 'self_improvement',
  '经济管理': 'business',
  '少儿读物': 'child_care',
  '艺术设计': 'palette',
  '全部图书': 'library_books',
}

function getCategoryIcon(name) {
  return categoryIconMap[name] || 'menu_book'
}

function formatReadCount(count) {
  if (count >= 10000) {
    return (count / 10000).toFixed(1) + '万'
  }
  return count.toString()
}

async function loadBanners() {
  try {
    const res = await getBanners(5)
    banners.value = res.data || []
  } catch (e) {
    console.error('加载 Banner 失败', e)
  }
}

async function loadCategories() {
  try {
    const res = await getHomeCategories(5)
    categories.value = res.data || []
  } catch (e) {
    console.error('加载分类失败', e)
  }
}

async function loadBooks(type) {
  loading.value = true
  errorMsg.value = ''
  try {
    const res = await getBooks(type, 10)
    books.value = res.data || []
  } catch (e) {
    errorMsg.value = e.message || '加载书籍失败'
    books.value = []
  } finally {
    loading.value = false
  }
}

watch(activeTab, (newVal) => {
  loadBooks(newVal)
})

onMounted(() => {
  loadBanners()
  loadCategories()
  loadBooks(activeTab.value)
})
</script>

<template>
  <div class="min-h-screen flex flex-col gap-stack-gap-lg px-container-margin pt-3 pb-28">
    <TopAppBar title="智慧图书" />

    <!-- Top navigation pills -->
    <nav class="flex overflow-x-auto no-scrollbar gap-inline-gap py-1 -mt-1">
      <button
        v-for="tab in tabs"
        :key="tab.value"
        class="px-3.5 py-1 rounded-full whitespace-nowrap text-label-md transition-all"
        :class="activeTab === tab.value ? 'bg-primary text-on-primary shadow-sm' : 'glass-card text-on-surface-variant hover:bg-white/70'"
        @click="activeTab = tab.value"
      >
        {{ tab.label }}
      </button>
    </nav>

    <!-- Hero carousel (Banner) -->
    <section v-if="banners.length" class="w-full relative">
      <div class="flex overflow-x-auto gap-3 snap-x snap-mandatory no-scrollbar pb-3">
        <div
          v-for="banner in banners"
          :key="banner.id"
          class="snap-center shrink-0 w-[82%] relative rounded-[20px] overflow-hidden glass-card group"
        >
          <div class="relative aspect-[16/10] w-full p-card-padding flex flex-col justify-end">
            <img
              v-if="banner.imageUrl"
              :src="banner.imageUrl"
              alt="banner"
              class="absolute inset-0 w-full h-full object-cover z-0"
            />
            <div v-else class="absolute inset-0 bg-gradient-to-br from-primary/30 to-secondary/30 z-0" />
            <div class="absolute inset-0 bg-gradient-to-t from-black/60 to-transparent z-0" />
            <div class="relative z-10 text-white">
              <span v-if="banner.tag" class="backdrop-blur-md px-2.5 py-0.5 rounded-full text-label-sm inline-block mb-1.5 uppercase tracking-wider bg-primary/80">{{ banner.tag }}</span>
              <h2 class="text-headline-md font-bold mb-0.5">{{ banner.title }}</h2>
              <p class="text-body-md opacity-90">{{ banner.subtitle }}</p>
            </div>
            <button class="absolute bottom-3.5 right-3.5 w-10 h-10 bg-primary rounded-full flex items-center justify-center text-white shadow-md hover:scale-105 transition-transform z-10">
              <span class="material-symbols-outlined material-symbols-filled text-[20px]">play_arrow</span>
            </button>
          </div>
        </div>
      </div>
    </section>

    <!-- Category quick access -->
    <section v-if="categories.length" class="grid grid-cols-5 gap-2 px-1">
      <div
        v-for="category in categories"
        :key="category.id"
        class="flex flex-col items-center gap-1.5 cursor-pointer group"
      >
        <div class="w-12 h-12 rounded-full glass-card flex items-center justify-center bg-white/60 group-hover:scale-105 transition-transform">
          <span class="material-symbols-outlined text-[22px] text-primary">{{ getCategoryIcon(category.name) }}</span>
        </div>
        <span class="text-label-sm text-on-surface-variant truncate w-full text-center">{{ category.name }}</span>
      </div>
    </section>

    <!-- Book list for active tab -->
    <section class="flex flex-col gap-3">
      <div class="flex justify-between items-end">
        <h3 class="text-headline-sm text-on-surface">
          {{ tabs.find(t => t.value === activeTab)?.label }}书籍
        </h3>
        <span v-if="loading" class="text-label-md text-on-surface-variant">加载中...</span>
      </div>

      <div v-if="errorMsg" class="text-center text-label-md text-error bg-error-container/50 rounded-lg py-2">{{ errorMsg }}</div>

      <div class="grid grid-cols-2 gap-3">
        <div
          v-for="book in books"
          :key="book.id"
          class="glass-card rounded-[14px] p-2.5 flex flex-col gap-2 relative overflow-hidden cursor-pointer hover:scale-[1.02] transition-transform"
        >
          <div class="w-full aspect-[3/4] rounded-lg bg-surface-container overflow-hidden relative z-10 flex items-center justify-center">
            <img
              v-if="book.cover"
              :src="book.cover"
              :alt="book.title"
              class="absolute inset-0 w-full h-full object-cover"
            />
            <div v-else class="absolute inset-0 bg-gradient-to-br from-primary/30 to-secondary/30" />
            <span v-if="!book.cover" class="material-symbols-outlined text-white text-[32px] relative z-10">book</span>
          </div>
          <div class="relative z-10 bg-white/45 backdrop-blur-sm -mt-5 p-1.5 rounded-lg border border-white/50">
            <h4 class="text-body-md font-bold truncate">{{ book.title }}</h4>
            <p class="text-label-sm text-on-surface-variant truncate">{{ book.author }}</p>
            <div class="flex items-center gap-1 mt-1 text-label-sm text-on-surface-variant">
              <span class="material-symbols-outlined text-[12px] text-secondary">trending_up</span>
              <span>{{ formatReadCount(book.readCount || 0) }}人读过</span>
            </div>
          </div>
        </div>
      </div>

      <div v-if="!loading && !books.length && !errorMsg" class="text-center text-label-md text-on-surface-variant py-8">
        暂无书籍
      </div>
    </section>

    <!-- Bottom spacing -->
    <div class="h-[60px]" />
  </div>
</template>
