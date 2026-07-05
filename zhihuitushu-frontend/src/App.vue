<script setup>
import { onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getCurrentUser } from '@/api/auth.js'
import { setUser, clearToken } from '@/stores/auth.js'
import BottomNavBar from './components/BottomNavBar.vue'

const route = useRoute()

// 管理端路由不使用 H5 移动端容器
const isAdminRoute = computed(() => route.path.startsWith('/admin'))
const hideNavRoutes = ['login', 'register', 'admin-login']

// 判断是否显示底部导航栏：排除指定路由和管理端路由
const showBottomNav = computed(() => {
  return !hideNavRoutes.includes(route.name) && !isAdminRoute.value
})

onMounted(async () => {
  try {
    const res = await getCurrentUser()
    if (res.data) {
      setUser(res.data)
    }
  } catch (e) {
    clearToken()
  }
})
</script>

<template>
  <!-- 管理端：全宽 Web 布局 -->
  <div v-if="isAdminRoute" class="admin-root min-h-full">
    <main class="min-h-full">
      <RouterView />
    </main>
  </div>

  <!-- 用户端（H5）：使用受限制的移动端容器 -->
  <div v-else class="mobile-container relative min-h-full overflow-x-hidden">
    <!-- Soft green background blobs -->
    <div class="fixed inset-0 z-[-1] pointer-events-none overflow-hidden">
      <div class="absolute top-[-10%] left-[-10%] w-[60%] h-[50%] bg-primary-container opacity-25 blur-[100px] rounded-full mix-blend-multiply" />
      <div class="absolute bottom-[-10%] right-[-10%] w-[70%] h-[60%] bg-tertiary-container opacity-20 blur-[120px] rounded-full mix-blend-multiply" />
      <div class="absolute top-[30%] right-[10%] w-[40%] h-[40%] bg-secondary-container opacity-20 blur-[90px] rounded-full mix-blend-overlay" />
    </div>

    <main :class="showBottomNav ? 'pb-20' : 'pb-0'">
      <RouterView v-slot="{ Component }">
        <transition name="page" mode="out-in">
          <component :is="Component" />
        </transition>
      </RouterView>
    </main>

    <BottomNavBar v-if="showBottomNav" />
  </div>
</template>
