<script setup>
import { ref } from 'vue'
import TopAppBar from '@/components/TopAppBar.vue'
import GlassCard from '@/components/GlassCard.vue'

const prompt = ref('')
const selectedStyle = ref('深度解读')
const styles = ['深度解读', '章节总结', '人物分析', '金句摘录', '阅读计划']

const inspirations = [
  { title: '《活着》人物命运分析', tag: '深度解读', icon: 'psychology', color: 'bg-secondary-container text-on-secondary-container' },
  { title: '《三体》黑暗森林法则', tag: '概念解析', icon: 'science', color: 'bg-tertiary-container text-on-tertiary-container' },
  { title: '7天读完《百年孤独》', tag: '阅读计划', icon: 'calendar_today', color: 'bg-primary-container text-on-primary-container' },
]

function generate() {
  // placeholder
}
</script>

<template>
  <div class="min-h-screen flex flex-col gap-stack-gap-lg px-container-margin pt-4 pb-32">
    <TopAppBar title="AI 读书助手" />

    <!-- Tab switcher -->
    <div class="flex overflow-x-auto no-scrollbar gap-inline-gap -mt-2 pb-2">
      <button
        v-for="style in styles"
        :key="style"
        class="px-4 py-1.5 rounded-full whitespace-nowrap text-label-md transition-all"
        :class="selectedStyle === style ? 'bg-primary text-on-primary shadow-md scale-105' : 'glass-card text-on-surface-variant hover:bg-white/40'"
        @click="selectedStyle = style"
      >
        {{ style }}
      </button>
    </div>

    <main class="flex flex-col gap-stack-gap-lg relative">
      <!-- Decorative illustration -->
      <div class="absolute -top-10 -right-4 w-24 h-24 opacity-80 pointer-events-none z-0">
        <div class="w-full h-full rounded-full bg-gradient-to-br from-tertiary-container to-secondary-container flex items-center justify-center">
          <span class="material-symbols-outlined text-white text-4xl">auto_awesome</span>
        </div>
      </div>

      <!-- Composition card -->
      <GlassCard class="rounded-[24px] relative shadow-[0_10px_30px_rgba(0,0,0,0.05)]">
        <div class="mb-6">
          <h2 class="text-headline-sm text-on-surface mb-2">让 AI 帮你读透一本书</h2>
          <p class="text-body-md text-on-surface-variant">输入书名或阅读问题，AI 为你生成专属解读。</p>
        </div>

        <div class="flex flex-col gap-stack-gap-md">
          <div>
            <label class="block text-label-md text-on-surface-variant mb-2">书名 / 问题</label>
            <textarea
              v-model="prompt"
              placeholder="例如：帮我总结一下《三体》的核心主题..."
              class="w-full bg-white/60 border border-white/40 rounded-xl p-3 focus:outline-none focus:ring-2 focus:ring-primary/50 resize-none h-24"
            />
          </div>

          <div>
            <label class="block text-label-md text-on-surface-variant mb-2">输出风格</label>
            <div class="flex flex-wrap gap-2">
              <button
                v-for="style in styles"
                :key="style"
                class="px-3 py-1.5 rounded-full text-label-md border transition-colors"
                :class="selectedStyle === style ? 'bg-primary/10 border-primary/30 text-primary' : 'glass-card border-white/40 text-on-surface-variant hover:border-primary/50'"
                @click="selectedStyle = style"
              >
                {{ style }}
              </button>
            </div>
          </div>
        </div>

        <button
          class="w-full mt-stack-gap-lg bg-primary-container text-on-primary-container rounded-full py-4 text-headline-sm flex justify-center items-center gap-2 hover:opacity-90 active:scale-95 transition-all shadow-[0_4px_14px_rgba(255,94,94,0.3)]"
          @click="generate"
        >
          <span class="material-symbols-outlined material-symbols-filled">auto_awesome</span>
          生成解读
        </button>
      </GlassCard>

      <!-- Inspiration section -->
      <section>
        <h3 class="text-headline-sm text-on-surface mb-stack-gap-md">灵感推荐</h3>
        <div class="flex overflow-x-auto no-scrollbar gap-inline-gap pb-4 -mx-container-margin px-container-margin">
          <div
            v-for="item in inspirations"
            :key="item.title"
            class="min-w-[160px] glass-card rounded-[16px] p-3 flex flex-col gap-3 relative overflow-hidden group"
          >
            <div class="absolute -right-2 -top-2 w-12 h-12 rounded-full bg-secondary-container/20 blur-xl" />
            <div :class="`w-10 h-10 rounded-full flex items-center justify-center ${item.color}`">
              <span class="material-symbols-outlined material-symbols-filled">{{ item.icon }}</span>
            </div>
            <div>
              <p class="text-label-md text-on-surface font-semibold line-clamp-1">{{ item.title }}</p>
              <p class="text-label-sm text-on-surface-variant line-clamp-1">{{ item.tag }}</p>
            </div>
            <button class="w-full py-1.5 rounded-full bg-white/50 text-label-sm text-on-surface hover:bg-white/80 transition-colors">试试这个</button>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>
