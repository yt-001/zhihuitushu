<script setup>
import { useRouter } from 'vue-router'
import { logout } from '@/api/auth.js'
import { clearToken, getUser } from '@/stores/auth.js'
import TopAppBar from '@/components/TopAppBar.vue'
import GlassCard from '@/components/GlassCard.vue'

const router = useRouter()

const user = getUser()

const menuGroups = [
  {
    title: '账号',
    items: [
      { icon: 'person', label: '个人资料', color: 'text-primary' },
      { icon: 'notifications', label: '消息通知', color: 'text-tertiary' },
      { icon: 'bookmark', label: '阅读偏好', color: 'text-secondary' },
    ],
  },
  {
    title: '阅读',
    items: [
      { icon: 'history', label: '阅读记录', color: 'text-on-surface-variant' },
      { icon: 'download', label: '下载管理', color: 'text-primary' },
      { icon: 'schedule', label: '阅读计划', color: 'text-tertiary' },
    ],
  },
  {
    title: '其他',
    items: [
      { icon: 'help', label: '帮助与反馈', color: 'text-secondary' },
      { icon: 'settings', label: '设置', color: 'text-on-surface-variant' },
    ],
  },
]

async function handleLogout() {
  try {
    await logout()
  } catch (e) {
    // ignore
  }
  clearToken()
  router.replace({ name: 'login' })
}
</script>

<template>
  <div class="min-h-screen flex flex-col gap-stack-gap-lg px-container-margin pt-3 pb-28">
    <TopAppBar title="个人中心" :show-search="false" />

    <!-- Profile header -->
    <GlassCard class="rounded-[20px] flex items-center gap-inline-gap shadow-[0_6px_20px_rgba(6,78,59,0.05)]">
      <div class="relative">
        <div class="w-16 h-16 rounded-full overflow-hidden border-2 border-white shadow-sm bg-gradient-to-br from-primary-container to-tertiary-container flex items-center justify-center">
          <span class="material-symbols-outlined text-white text-[28px]">person</span>
        </div>
        <div class="absolute -bottom-0.5 -right-0.5 bg-secondary-container text-on-secondary-container text-label-sm px-1.5 py-0.5 rounded-full border border-white font-bold">
          VIP
        </div>
      </div>
      <div class="flex-1">
        <h2 class="text-headline-md">{{ user?.username || '书友小雅' }}</h2>
        <p class="text-body-md text-on-surface-variant mt-0.5">ID: {{ user?.id || '13800138000' }}</p>
      </div>
      <button class="w-9 h-9 rounded-full glass-card flex items-center justify-center text-on-surface-variant hover:text-primary transition-colors touch-target">
        <span class="material-symbols-outlined text-[18px]">edit</span>
      </button>
    </GlassCard>

    <!-- Reading stats -->
    <section class="grid grid-cols-3 gap-2.5">
      <div class="glass-card rounded-[14px] p-3 flex flex-col items-center text-center gap-0.5">
        <span class="text-headline-md font-bold text-primary">128</span>
        <span class="text-label-sm text-on-surface-variant">已读书</span>
      </div>
      <div class="glass-card rounded-[14px] p-3 flex flex-col items-center text-center gap-0.5">
        <span class="text-headline-md font-bold text-tertiary">86</span>
        <span class="text-label-sm text-on-surface-variant">在读时</span>
      </div>
      <div class="glass-card rounded-[14px] p-3 flex flex-col items-center text-center gap-0.5">
        <span class="text-headline-md font-bold text-secondary">12</span>
        <span class="text-label-sm text-on-surface-variant">连续天</span>
      </div>
    </section>

    <!-- Menu groups -->
    <section class="flex flex-col gap-stack-gap-md">
      <div
        v-for="group in menuGroups"
        :key="group.title"
        class="flex flex-col gap-stack-gap-sm"
      >
        <h3 class="text-headline-sm text-on-surface px-1">{{ group.title }}</h3>
        <GlassCard class="rounded-[16px] overflow-hidden">
          <div class="flex flex-col">
            <button
              v-for="(item, index) in group.items"
              :key="item.label"
              class="flex items-center gap-inline-gap py-2.5 px-2 hover:bg-white/40 transition-colors"
              :class="index !== group.items.length - 1 ? 'border-b border-outline-variant/30' : ''"
            >
              <span class="material-symbols-outlined text-[18px]" :class="item.color">{{ item.icon }}</span>
              <span class="flex-1 text-body-lg text-left">{{ item.label }}</span>
              <span class="material-symbols-outlined text-on-surface-variant text-[18px]">chevron_right</span>
            </button>
          </div>
        </GlassCard>
      </div>
    </section>

    <!-- Logout -->
    <button
      class="w-full py-2.5 rounded-full border border-outline-variant text-on-surface-variant text-headline-sm hover:bg-white/50 transition-colors"
      @click="handleLogout"
    >
      退出登录
    </button>
  </div>
</template>
